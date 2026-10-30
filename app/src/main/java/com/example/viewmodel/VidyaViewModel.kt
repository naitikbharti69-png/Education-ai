package com.example.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CurriculumCatalog
import com.example.data.local.DownloadedLessonEntity
import com.example.data.local.SavedDiagramNoteEntity
import com.example.data.local.StudyTaskEntity
import com.example.data.local.TestAttemptEntity
import com.example.data.local.VidyaDatabase
import com.example.data.local.VidyaRepository
import com.example.model.AiGeneratedSummary
import com.example.model.AppLanguage
import com.example.model.ChatMessage
import com.example.model.DiagramType
import com.example.model.DrawnStroke
import com.example.model.DrawnStrokePoint
import com.example.model.GamifiedBadge
import com.example.model.LectureItem
import com.example.model.MockQuestion
import com.example.model.MockTestItem
import com.example.model.SubjectStream
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import java.util.UUID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "VidyaViewModel"

sealed interface AiTaskState<out T> {
    data object Idle : AiTaskState<Nothing>
    data object Loading : AiTaskState<Nothing>
    data class Success<T>(val data: T) : AiTaskState<T>
    data class Error(val message: String) : AiTaskState<Nothing>
}

class VidyaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VidyaRepository

    init {
        val db = VidyaDatabase.getDatabase(application)
        repository = VidyaRepository(db.vidyaDao())
        seedDefaultStudyPlanIfNeeded()
    }

    // Persistent Flows from Room
    val downloadedLessons: StateFlow<List<DownloadedLessonEntity>> = repository.downloadedLessons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedNotes: StateFlow<List<SavedDiagramNoteEntity>> = repository.savedNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val testAttempts: StateFlow<List<TestAttemptEntity>> = repository.testAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studyTasks: StateFlow<List<StudyTaskEntity>> = repository.studyTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // App Filters & Preferences
    private val _selectedLanguage = MutableStateFlow(AppLanguage.HINDI_ENGLISH)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _selectedClass = MutableStateFlow(10) // Default Class 10, supports 1..12 or 0 for All
    val selectedClass: StateFlow<Int> = _selectedClass.asStateFlow()

    private val _selectedStream = MutableStateFlow(SubjectStream.ALL)
    val selectedStream: StateFlow<SubjectStream> = _selectedStream.asStateFlow()

    private val _isOfflineModeOnly = MutableStateFlow(false)
    val isOfflineModeOnly: StateFlow<Boolean> = _isOfflineModeOnly.asStateFlow()

    // Active Lecture & AI Summary State
    private val _selectedLecture = MutableStateFlow<LectureItem>(
        CurriculumCatalog.lectures.first { it.classNumber == 10 }
    )
    val selectedLecture: StateFlow<LectureItem> = _selectedLecture.asStateFlow()

    private val _lectureAiState = MutableStateFlow<AiTaskState<AiGeneratedSummary>>(AiTaskState.Idle)
    val lectureAiState: StateFlow<AiTaskState<AiGeneratedSummary>> = _lectureAiState.asStateFlow()

    // Active Diagram Canvas State
    private val _activeDiagramType = MutableStateFlow(DiagramType.TRIGONOMETRY_CIRCLE)
    val activeDiagramType: StateFlow<DiagramType> = _activeDiagramType.asStateFlow()

    private val _canvasStrokes = MutableStateFlow<List<DrawnStroke>>(emptyList())
    val canvasStrokes: StateFlow<List<DrawnStroke>> = _canvasStrokes.asStateFlow()

    private val _aiDiagramStepsState = MutableStateFlow<AiTaskState<List<String>>>(AiTaskState.Idle)
    val aiDiagramStepsState: StateFlow<AiTaskState<List<String>>> = _aiDiagramStepsState.asStateFlow()

    // Mock Tests & AI Custom Test State
    private val _customMockTests = MutableStateFlow<List<MockTestItem>>(emptyList())
    val customMockTests: StateFlow<List<MockTestItem>> = _customMockTests.asStateFlow()

    private val _aiQuizGenState = MutableStateFlow<AiTaskState<MockTestItem>>(AiTaskState.Idle)
    val aiQuizGenState: StateFlow<AiTaskState<MockTestItem>> = _aiQuizGenState.asStateFlow()

    // Study Planner AI State
    private val _aiStudyPlanState = MutableStateFlow<AiTaskState<String>>(AiTaskState.Idle)
    val aiStudyPlanState: StateFlow<AiTaskState<String>> = _aiStudyPlanState.asStateFlow()

    // Doubt-Solving Chatbot State
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "welcome_msg",
                isUser = false,
                text = "नमस्ते! Welcome to VidyaSetu AI Guru! Ask me any doubt from Class 1st to 12th (Maths, Science, Physics, Chemistry, Biology, Commerce, History, Hindi or English) in Hindi, English, or Hinglish!",
                subjectTag = "Vidya Guru AI"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatGenerating = MutableStateFlow(false)
    val isChatGenerating: StateFlow<Boolean> = _isChatGenerating.asStateFlow()

    // Gamification XP & Watched Lectures
    private val _watchedLectureIds = MutableStateFlow<Set<String>>(setOf("lec_c10_math"))
    val watchedLectureIds: StateFlow<Set<String>> = _watchedLectureIds.asStateFlow()

    private val _bonusXp = MutableStateFlow(120)
    val bonusXp: StateFlow<Int> = _bonusXp.asStateFlow()

    // Lazy Firebase AI Models (Initialized via by lazy as mandated)
    private val summarySchema = Schema.obj(
        properties = mapOf(
            "summaryBullets" to Schema.array(
                items = Schema.string(),
                description = "4 to 5 concise revision bullet points in the requested language"
            ),
            "keyFormulasOrFacts" to Schema.array(
                items = Schema.string(),
                description = "Key formulas, equations, or definitions"
            ),
            "diagramDrawingSteps" to Schema.array(
                items = Schema.string(),
                description = "Step-by-step instructions to draw and label the concept diagram"
            ),
            "examTip" to Schema.string(description = "High-scoring board/school exam tip")
        )
    )

    private val summaryModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = summarySchema
            }
        )
    }

    private val quizSchema = Schema.obj(
        properties = mapOf(
            "titleEn" to Schema.string(description = "Quiz title in English"),
            "titleHi" to Schema.string(description = "Quiz title in Hindi"),
            "questions" to Schema.array(
                items = Schema.obj(
                    properties = mapOf(
                        "questionEn" to Schema.string(description = "Question in English"),
                        "questionHi" to Schema.string(description = "Question in Hindi"),
                        "optionsEn" to Schema.array(items = Schema.string(), description = "4 options in English"),
                        "optionsHi" to Schema.array(items = Schema.string(), description = "4 options in Hindi"),
                        "correctIndex" to Schema.integer(description = "0-based index of correct option (0 to 3)"),
                        "explanationEn" to Schema.string(description = "Step-by-step explanation in English"),
                        "explanationHi" to Schema.string(description = "Step-by-step explanation in Hindi")
                    )
                )
            )
        )
    )

    private val quizGeneratorModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = quizSchema
            }
        )
    }

    private val studyPlanSchema = Schema.obj(
        properties = mapOf(
            "coachAdvice" to Schema.string(description = "Motivational advice for the student"),
            "tasks" to Schema.array(
                items = Schema.obj(
                    properties = mapOf(
                        "dayLabel" to Schema.string(description = "Day name e.g. Day 1 - Monday"),
                        "subject" to Schema.string(description = "Subject name"),
                        "topicEn" to Schema.string(description = "Study task in English"),
                        "topicHi" to Schema.string(description = "Study task in Hindi"),
                        "durationMinutes" to Schema.integer(description = "Duration in minutes e.g. 45")
                    )
                )
            )
        )
    )

    private val studyPlanModel by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = studyPlanSchema
            }
        )
    }

    private val chatAndGeneralModel by lazy {
        Firebase.ai.generativeModel("gemini-3.8-flash")
    }

    // Actions & State Updaters
    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
    }

    fun setSelectedClass(classNum: Int) {
        _selectedClass.value = classNum
        val matching = CurriculumCatalog.lectures.firstOrNull { classNum == 0 || it.classNumber == classNum }
        if (matching != null) {
            _selectedLecture.value = matching
            _activeDiagramType.value = matching.diagramType
            _lectureAiState.value = AiTaskState.Idle
        }
    }

    fun setSelectedStream(stream: SubjectStream) {
        _selectedStream.value = stream
    }

    fun toggleOfflineMode(enabled: Boolean) {
        _isOfflineModeOnly.value = enabled
    }

    fun selectLecture(lecture: LectureItem) {
        _selectedLecture.value = lecture
        _activeDiagramType.value = lecture.diagramType
        _lectureAiState.value = AiTaskState.Idle
        _watchedLectureIds.value = _watchedLectureIds.value + lecture.id
        _bonusXp.value += 15
    }

    fun selectDiagramType(diagramType: DiagramType) {
        _activeDiagramType.value = diagramType
    }

    // Offline Download Management
    fun downloadLectureOffline(lecture: LectureItem, aiSummary: AiGeneratedSummary? = null) {
        viewModelScope.launch {
            val summaryEn = aiSummary?.summaryBullets?.joinToString("\n• ", prefix = "• ")
                ?: lecture.defaultSummaryEn.joinToString("\n• ", prefix = "• ")
            val summaryHi = lecture.defaultSummaryHi.joinToString("\n• ", prefix = "• ")
            val formulas = (aiSummary?.keyFormulasOrFacts ?: lecture.keyFormulas).joinToString("\n")

            repository.saveOfflineLesson(
                DownloadedLessonEntity(
                    lectureId = lecture.id,
                    classNumber = lecture.classNumber,
                    subjectEn = lecture.subjectEn,
                    subjectHi = lecture.subjectHi,
                    titleEn = lecture.titleEn,
                    titleHi = lecture.titleHi,
                    cachedSummaryEn = summaryEn,
                    cachedSummaryHi = summaryHi,
                    cachedFormulas = formulas,
                    diagramTypeName = lecture.diagramType.name
                )
            )
            _bonusXp.value += 25
        }
    }

    fun removeOfflineLecture(lectureId: String) {
        viewModelScope.launch {
            repository.removeOfflineLesson(lectureId)
        }
    }

    // Canvas Drawing Management
    fun addCanvasStroke(stroke: DrawnStroke) {
        _canvasStrokes.value = _canvasStrokes.value + stroke
    }

    fun undoLastStroke() {
        val current = _canvasStrokes.value
        if (current.isNotEmpty()) {
            _canvasStrokes.value = current.dropLast(1)
        }
    }

    fun clearCanvasStrokes() {
        _canvasStrokes.value = emptyList()
    }

    fun saveDiagramAndNoteToRoom(title: String, noteText: String) {
        viewModelScope.launch {
            val lecture = _selectedLecture.value
            val serialized = serializeStrokes(_canvasStrokes.value)
            repository.saveDiagramNote(
                SavedDiagramNoteEntity(
                    lectureId = lecture.id,
                    title = title.ifBlank { "${_activeDiagramType.value.titleEn} Notes" },
                    classNumber = lecture.classNumber,
                    subject = lecture.subjectEn,
                    noteContent = noteText,
                    diagramTypeName = _activeDiagramType.value.name,
                    strokesSerialized = serialized
                )
            )
            _bonusXp.value += 30
        }
    }

    fun loadSavedDiagramNote(saved: SavedDiagramNoteEntity) {
        val diagram = DiagramType.entries.firstOrNull { it.name == saved.diagramTypeName }
            ?: DiagramType.TRIGONOMETRY_CIRCLE
        _activeDiagramType.value = diagram
        _canvasStrokes.value = deserializeStrokes(saved.strokesSerialized)
    }

    fun deleteSavedDiagramNote(id: Int) {
        viewModelScope.launch {
            repository.deleteDiagramNote(id)
        }
    }

    // 1. AI Lecture Summary, Smart Notes & Diagram Guide
    fun generateLectureSummaryWithAi(lecture: LectureItem, customFocus: String = "") {
        viewModelScope.launch {
            _lectureAiState.value = AiTaskState.Loading
            val langInstruction = when (_selectedLanguage.value) {
                AppLanguage.ENGLISH -> "Respond in clear English."
                AppLanguage.HINDI -> "Respond in clear Hindi (देवनागरी लिपि)."
                AppLanguage.HINDI_ENGLISH -> "Respond in bilingual Hinglish / English + Hindi (द्विभाषी हिंदी और अंग्रेजी) so both Hindi and English medium students understand easily."
            }
            val prompt = """
                You are an expert Indian school teacher for Class ${lecture.classNumber} (${lecture.subjectEn} / ${lecture.subjectHi}).
                Lecture Title: ${lecture.titleEn} (${lecture.titleHi})
                Transcript / Context: ${lecture.transcriptEn} ${lecture.transcriptHi}
                Key Formulas: ${lecture.keyFormulas.joinToString("; ")}
                Diagram Topic: ${lecture.diagramType.titleEn} (${lecture.diagramType.titleHi})
                ${if (customFocus.isNotBlank()) "Student special focus request: $customFocus" else ""}
                $langInstruction
                Generate structured revision notes, key formulas/facts, step-by-step instructions to draw the concept diagram neatly in exams, and an exam scoring tip.
            """.trimIndent()

            try {
                val response = withTimeout(60_000L) {
                    summaryModel.generateContent(prompt)
                }
                val jsonString = response.text ?: throw IllegalStateException("Empty AI response.")
                val json = JSONObject(jsonString)

                val bullets = json.getJSONArray("summaryBullets").toStringList()
                val formulas = json.getJSONArray("keyFormulasOrFacts").toStringList()
                val diagramSteps = json.getJSONArray("diagramDrawingSteps").toStringList()
                val examTip = json.optString("examTip", "Practice drawing the labeled diagram neatly for full marks!")

                val result = AiGeneratedSummary(
                    lectureId = lecture.id,
                    summaryBullets = bullets,
                    keyFormulasOrFacts = formulas,
                    diagramDrawingSteps = diagramSteps,
                    examTip = examTip
                )
                _lectureAiState.value = AiTaskState.Success(result)
                _bonusXp.value += 20
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "AI lecture summary timed out", e)
                _lectureAiState.value = AiTaskState.Error("Generation timed out after 60 seconds. Please try again.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "AI lecture summary failed", e)
                _lectureAiState.value = AiTaskState.Error(e.localizedMessage ?: "Failed to generate AI notes.")
            }
        }
    }

    // 2. AI Diagram Drawing Coach
    fun generateDiagramStepsWithAi(topicName: String) {
        viewModelScope.launch {
            _aiDiagramStepsState.value = AiTaskState.Loading
            val langInstruction = when (_selectedLanguage.value) {
                AppLanguage.ENGLISH -> "in English"
                AppLanguage.HINDI -> "in Hindi (देवनागरी)"
                AppLanguage.HINDI_ENGLISH -> "in bilingual English + Hindi"
            }
            val prompt = """
                Give 5 concise, numbered step-by-step instructions $langInstruction on how a student (Class ${_selectedClass.value.coerceAtLeast(1)}) can draw and label a neat scientific/mathematical diagram of: "$topicName" (${_activeDiagramType.value.titleEn}).
                Keep each line short, practical, and easy to follow on a drawing canvas.
            """.trimIndent()

            try {
                val response = withTimeout(60_000L) {
                    chatAndGeneralModel.generateContent(prompt)
                }
                val text = response.text
                if (!text.isNullOrBlank()) {
                    val lines = text.lines()
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }
                    _aiDiagramStepsState.value = AiTaskState.Success(lines)
                    _bonusXp.value += 15
                } else {
                    _aiDiagramStepsState.value = AiTaskState.Error("Model returned an empty response.")
                }
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Diagram coach timed out", e)
                _aiDiagramStepsState.value = AiTaskState.Error("Diagram guide timed out after 60s.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Diagram coach failed", e)
                _aiDiagramStepsState.value = AiTaskState.Error(e.localizedMessage ?: "Failed to generate diagram steps.")
            }
        }
    }

    // 3. AI Custom Mock Test Generator (Structured JSON)
    fun generateAiMockTest(classNumber: Int, subjectTopic: String) {
        viewModelScope.launch {
            _aiQuizGenState.value = AiTaskState.Loading
            val targetClass = if (classNumber in 1..12) classNumber else 10
            val prompt = """
                Create a 4-question multiple-choice Mock Test for Indian school Class $targetClass on the subject/topic: "$subjectTopic".
                Provide every question, all 4 options, and the explanation in BOTH English and Hindi (देवनागरी).
                Ensure correctIndex is between 0 and 3 and matches the right option.
            """.trimIndent()

            try {
                val response = withTimeout(60_000L) {
                    quizGeneratorModel.generateContent(prompt)
                }
                val jsonString = response.text ?: throw IllegalStateException("Empty quiz JSON.")
                val json = JSONObject(jsonString)
                val titleEn = json.optString("titleEn", "Class $targetClass: $subjectTopic AI Mock")
                val titleHi = json.optString("titleHi", "कक्षा $targetClass: $subjectTopic एआई मॉक टेस्ट")
                val qArray = json.getJSONArray("questions")
                val parsedQuestions = mutableListOf<MockQuestion>()

                for (i in 0 until qArray.length()) {
                    val qObj = qArray.getJSONObject(i)
                    val optsEn = qObj.getJSONArray("optionsEn").toStringList()
                    val optsHi = qObj.getJSONArray("optionsHi").toStringList()
                    if (optsEn.size >= 4 && optsHi.size >= 4) {
                        parsedQuestions.add(
                            MockQuestion(
                                id = "ai_q_${UUID.randomUUID()}",
                                questionEn = qObj.getString("questionEn"),
                                questionHi = qObj.getString("questionHi"),
                                optionsEn = optsEn.take(4),
                                optionsHi = optsHi.take(4),
                                correctIndex = qObj.optInt("correctIndex", 0).coerceIn(0, 3),
                                explanationEn = qObj.optString("explanationEn", ""),
                                explanationHi = qObj.optString("explanationHi", "")
                            )
                        )
                    }
                }

                if (parsedQuestions.isEmpty()) {
                    throw IllegalStateException("No valid questions returned by model.")
                }

                val newTest = MockTestItem(
                    id = "ai_test_${System.currentTimeMillis()}",
                    classNumber = targetClass,
                    subjectEn = subjectTopic,
                    subjectHi = subjectTopic,
                    stream = SubjectStream.ALL,
                    titleEn = titleEn,
                    titleHi = titleHi,
                    durationMinutes = 10,
                    xpReward = 100,
                    questions = parsedQuestions
                )
                _customMockTests.value = listOf(newTest) + _customMockTests.value
                _aiQuizGenState.value = AiTaskState.Success(newTest)
                _bonusXp.value += 25
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "AI Mock Test timed out", e)
                _aiQuizGenState.value = AiTaskState.Error("Mock test generation timed out after 60s.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "AI Mock Test generation failed", e)
                _aiQuizGenState.value = AiTaskState.Error(e.localizedMessage ?: "Failed to generate mock test.")
            }
        }
    }

    fun submitMockTestAttempt(test: MockTestItem, score: Int) {
        viewModelScope.launch {
            val earnedXp = (score * 25) + 20
            repository.recordTestAttempt(
                TestAttemptEntity(
                    testId = test.id,
                    testTitle = test.titleEn,
                    classNumber = test.classNumber,
                    subject = test.subjectEn,
                    score = score,
                    totalQuestions = test.questions.size,
                    xpEarned = earnedXp
                )
            )
            _bonusXp.value += earnedXp
        }
    }

    // 4. Interactive Doubt-Solving AI Chatbot (Streaming)
    fun askDoubtStream(questionText: String, subjectContext: String) {
        if (questionText.isBlank() || _isChatGenerating.value) return

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            isUser = true,
            text = questionText.trim(),
            subjectTag = subjectContext
        )
        val botMsgId = UUID.randomUUID().toString()
        val placeholderBotMsg = ChatMessage(
            id = botMsgId,
            isUser = false,
            text = "Thinking / सोच रहा हूँ...",
            subjectTag = "Vidya Guru AI",
            isStreaming = true
        )

        _chatMessages.value = _chatMessages.value + userMsg + placeholderBotMsg
        _isChatGenerating.value = true

        viewModelScope.launch {
            val langMode = when (_selectedLanguage.value) {
                AppLanguage.ENGLISH -> "Answer in clear English."
                AppLanguage.HINDI -> "Answer in simple, encouraging Hindi (देवनागरी लिपि)."
                AppLanguage.HINDI_ENGLISH -> "Answer in student-friendly bilingual Hinglish + Hindi/English so concepts and formulas are crystal clear."
            }
            val classLabel = if (_selectedClass.value in 1..12) "Class ${_selectedClass.value}" else "Class 1 to 12"
            val prompt = """
                You are 'Vidya Guru AI', a warm, brilliant Indian school tutor for $classLabel ($subjectContext).
                $langMode
                Break down the explanation step-by-step with formulas, examples, and a quick memory trick if applicable.
                Student Doubt: $questionText
            """.trimIndent()

            val accumulated = StringBuilder()
            try {
                withTimeout(60_000L) {
                    chatAndGeneralModel.generateContentStream(prompt).collect { chunk ->
                        val part = chunk.text.orEmpty()
                        accumulated.append(part)
                        updateBotMessage(botMsgId, accumulated.toString(), isStreaming = true, isError = false)
                    }
                }
                if (accumulated.isNotBlank()) {
                    updateBotMessage(botMsgId, accumulated.toString(), isStreaming = false, isError = false)
                    _bonusXp.value += 15
                } else {
                    updateBotMessage(
                        botMsgId,
                        "Model returned an empty response. Please try asking again.",
                        isStreaming = false,
                        isError = true
                    )
                }
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Doubt chatbot streaming timed out", e)
                updateBotMessage(
                    botMsgId,
                    "Doubt response timed out after 60 seconds. Please try again.",
                    isStreaming = false,
                    isError = true
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Doubt chatbot streaming failed", e)
                updateBotMessage(
                    botMsgId,
                    e.localizedMessage ?: "Failed to generate answer.",
                    isStreaming = false,
                    isError = true
                )
            } finally {
                _isChatGenerating.value = false
            }
        }
    }

    private fun updateBotMessage(id: String, newText: String, isStreaming: Boolean, isError: Boolean) {
        _chatMessages.value = _chatMessages.value.map { msg ->
            if (msg.id == id) {
                msg.copy(text = newText, isStreaming = isStreaming, isError = isError)
            } else {
                msg
            }
        }
    }

    // 5. Personalized AI Study Planner (Structured JSON -> Saved to Room)
    fun generatePersonalizedStudyPlan(
        classNumber: Int,
        focusSubjects: String,
        dailyHours: Int,
        examGoal: String
    ) {
        viewModelScope.launch {
            _aiStudyPlanState.value = AiTaskState.Loading
            val targetClass = if (classNumber in 1..12) classNumber else 10
            val prompt = """
                Create a 6-day personalized study plan for a Class $targetClass student.
                Focus Subjects: $focusSubjects
                Daily Study Hours Available: $dailyHours hours/day
                Target Goal: $examGoal
                Provide 6 actionable study tasks (one for each day from Day 1 to Day 6) with both English (topicEn) and Hindi (topicHi) descriptions, plus a short motivational coachAdvice.
            """.trimIndent()

            try {
                val response = withTimeout(60_000L) {
                    studyPlanModel.generateContent(prompt)
                }
                val jsonString = response.text ?: throw IllegalStateException("Empty study plan JSON.")
                val json = JSONObject(jsonString)
                val advice = json.optString("coachAdvice", "Stay consistent every day to master your subjects!")
                val tasksArray = json.getJSONArray("tasks")
                val entities = mutableListOf<StudyTaskEntity>()

                for (i in 0 until tasksArray.length()) {
                    val obj = tasksArray.getJSONObject(i)
                    entities.add(
                        StudyTaskEntity(
                            dayLabel = obj.optString("dayLabel", "Day ${i + 1}"),
                            classNumber = targetClass,
                            subject = obj.optString("subject", focusSubjects),
                            topicEn = obj.optString("topicEn", "Concept Revision & Practice"),
                            topicHi = obj.optString("topicHi", "अवधारणा रिवीजन और अभ्यास"),
                            durationMinutes = obj.optInt("durationMinutes", 45).coerceIn(15, 180),
                            isCompleted = false,
                            xpReward = 40
                        )
                    )
                }

                if (entities.isNotEmpty()) {
                    repository.replaceStudyPlan(entities)
                }
                _aiStudyPlanState.value = AiTaskState.Success(advice)
                _bonusXp.value += 30
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Study plan generation timed out", e)
                _aiStudyPlanState.value = AiTaskState.Error("Study plan generation timed out after 60s.")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Study plan generation failed", e)
                _aiStudyPlanState.value = AiTaskState.Error(e.localizedMessage ?: "Failed to generate study plan.")
            }
        }
    }

    fun toggleStudyTaskCompletion(task: StudyTaskEntity) {
        viewModelScope.launch {
            val newStatus = !task.isCompleted
            repository.setStudyTaskCompleted(task.id, newStatus)
            if (newStatus) {
                _bonusXp.value += task.xpReward
            }
        }
    }

    private fun seedDefaultStudyPlanIfNeeded() {
        viewModelScope.launch {
            repository.studyTasks.collect { current ->
                if (current.isEmpty()) {
                    val defaults = listOf(
                        StudyTaskEntity(
                            dayLabel = "Day 1 • Monday",
                            classNumber = 10,
                            subject = "Mathematics",
                            topicEn = "Revise Trigonometric Ratios & solve 10 identities",
                            topicHi = "त्रिकोणमितीय अनुपात का रिवीजन और 10 सर्वसमिका प्रश्न हल करें",
                            durationMinutes = 45,
                            isCompleted = true,
                            xpReward = 40
                        ),
                        StudyTaskEntity(
                            dayLabel = "Day 2 • Tuesday",
                            classNumber = 10,
                            subject = "Science (Biology)",
                            topicEn = "Draw & label Human Heart Double Circulation diagram",
                            topicHi = "मानव हृदय के दोहरे परिसंचरण का नामांकित चित्र बनाएं",
                            durationMinutes = 40,
                            isCompleted = false,
                            xpReward = 40
                        ),
                        StudyTaskEntity(
                            dayLabel = "Day 3 • Wednesday",
                            classNumber = 10,
                            subject = "Physics",
                            topicEn = "Practice Ray Optics Convex Lens numericals & sign rules",
                            topicHi = "उत्तल लेंस के आंकिक प्रश्न और चिह्न परिपाटी का अभ्यास करें",
                            durationMinutes = 50,
                            isCompleted = false,
                            xpReward = 45
                        ),
                        StudyTaskEntity(
                            dayLabel = "Day 4 • Thursday",
                            classNumber = 10,
                            subject = "Mock Test & AI Revision",
                            topicEn = "Attempt 1 Full Mock Test & clear doubts with Vidya Guru AI",
                            topicHi = "1 मॉक टेस्ट दें और विद्या गुरु AI से अपने डाउट क्लियर करें",
                            durationMinutes = 35,
                            isCompleted = false,
                            xpReward = 50
                        )
                    )
                    repository.replaceStudyPlan(defaults)
                }
                return@collect
            }
        }
    }

    fun computeBadges(
        totalXp: Int,
        downloadedCount: Int,
        savedNotesCount: Int,
        attemptsCount: Int
    ): List<GamifiedBadge> {
        return listOf(
            GamifiedBadge(
                id = "badge_starter",
                titleEn = "Vidya Aarambh Scholar",
                titleHi = "विद्या आरंभ विद्वान",
                descEn = "Started learning journey & watched lectures",
                descHi = "अपनी पढ़ाई यात्रा शुरू की और लेक्चर देखे",
                requiredXp = 50,
                unlocked = totalXp >= 50
            ),
            GamifiedBadge(
                id = "badge_offline",
                titleEn = "Offline Master",
                titleHi = "ऑफलाइन मास्टर",
                descEn = "Downloaded lessons for anytime offline study",
                descHi = "बिना इंटरनेट पढ़ाई के लिए लेक्चर डाउनलोड किए",
                requiredXp = 100,
                unlocked = downloadedCount >= 1
            ),
            GamifiedBadge(
                id = "badge_artist",
                titleEn = "Diagram Architect",
                titleHi = "चित्र एवं आरेख कलाकार",
                descEn = "Drew and saved concept diagrams on the canvas",
                descHi = "कैनवास पर विज्ञान/गणित के आरेख बनाकर सेव किए",
                requiredXp = 150,
                unlocked = savedNotesCount >= 1
            ),
            GamifiedBadge(
                id = "badge_champion",
                titleEn = "Mock Test Champion",
                titleHi = "मॉक टेस्ट चैंपियन",
                descEn = "Completed mock tests with high accuracy",
                descHi = "मॉक टेस्ट सफलतापूर्वक पूरे किए",
                requiredXp = 200,
                unlocked = attemptsCount >= 1
            ),
            GamifiedBadge(
                id = "badge_aryabhata",
                titleEn = "Aryabhata Gold Medalist",
                titleHi = "आर्यभट्ट स्वर्ण पदक",
                descEn = "Crossed 300+ XP across classes & subjects",
                descHi = "सभी विषयों में 300+ XP अंक प्राप्त किए",
                requiredXp = 300,
                unlocked = totalXp >= 300
            )
        )
    }

    private fun JSONArray.toStringList(): List<String> {
        return (0 until length()).map { getString(it) }
    }

    private fun serializeStrokes(strokes: List<DrawnStroke>): String {
        val arr = JSONArray()
        strokes.forEach { s ->
            val obj = JSONObject()
            obj.put("colorHex", s.colorHex)
            obj.put("strokeWidth", s.strokeWidth.toDouble())
            obj.put("shapeType", s.shapeType)
            obj.put("labelText", s.labelText)
            val ptsArr = JSONArray()
            s.points.forEach { p ->
                val pObj = JSONObject()
                pObj.put("x", p.xRatio.toDouble())
                pObj.put("y", p.yRatio.toDouble())
                ptsArr.put(pObj)
            }
            obj.put("points", ptsArr)
            arr.put(obj)
        }
        return arr.toString()
    }

    private fun deserializeStrokes(serialized: String): List<DrawnStroke> {
        if (serialized.isBlank()) return emptyList()
        return try {
            val arr = JSONArray(serialized)
            val list = mutableListOf<DrawnStroke>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val ptsArr = obj.getJSONArray("points")
                val pts = mutableListOf<DrawnStrokePoint>()
                for (j in 0 until ptsArr.length()) {
                    val pObj = ptsArr.getJSONObject(j)
                    pts.add(
                        DrawnStrokePoint(
                            xRatio = pObj.getDouble("x").toFloat(),
                            yRatio = pObj.getDouble("y").toFloat()
                        )
                    )
                }
                list.add(
                    DrawnStroke(
                        points = pts,
                        colorHex = obj.getLong("colorHex"),
                        strokeWidth = obj.getDouble("strokeWidth").toFloat(),
                        shapeType = obj.optString("shapeType", "FREEHAND"),
                        labelText = obj.optString("labelText", "")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}
