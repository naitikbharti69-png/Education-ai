package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.data.local.StudyTaskEntity
import com.example.model.AppLanguage
import com.example.model.ChatMessage
import com.example.model.pick
import com.example.viewmodel.AiTaskState

@Composable
fun AiDoubtAndPlannerScreen(
    language: AppLanguage,
    selectedClass: Int,
    chatMessages: List<ChatMessage>,
    isChatGenerating: Boolean,
    studyTasks: List<StudyTaskEntity>,
    aiStudyPlanState: AiTaskState<String>,
    onAskDoubt: (String, String) -> Unit,
    onGenerateStudyPlan: (Int, String, Int, String) -> Unit,
    onToggleStudyTask: (StudyTaskEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableIntStateOf(0) } // 0 = Doubt Chatbot, 1 = Study Planner

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("ai_doubt_and_planner_screen")
    ) {
        // Segmented Switcher between Doubt-Solving Chatbot & Study Planner
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SegmentedButton(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                icon = { Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("subtab_doubt_chat")
            ) {
                Text(language.pick("Doubt Solver AI", "डाउट सॉल्वर AI"))
            }
            SegmentedButton(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("subtab_study_planner")
            ) {
                Text(language.pick("AI Study Planner", "AI स्टडी प्लानर"))
            }
        }

        if (activeSubTab == 0) {
            DoubtChatbotSection(
                language = language,
                selectedClass = selectedClass,
                chatMessages = chatMessages,
                isChatGenerating = isChatGenerating,
                onAskDoubt = onAskDoubt,
                modifier = Modifier.weight(1f)
            )
        } else {
            StudyPlannerSection(
                language = language,
                selectedClass = selectedClass,
                studyTasks = studyTasks,
                aiStudyPlanState = aiStudyPlanState,
                onGenerateStudyPlan = onGenerateStudyPlan,
                onToggleStudyTask = onToggleStudyTask,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DoubtChatbotSection(
    language: AppLanguage,
    selectedClass: Int,
    chatMessages: List<ChatMessage>,
    isChatGenerating: Boolean,
    onAskDoubt: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubject by remember { mutableStateOf("Mathematics & Science") }
    var doubtInput by remember { mutableStateOf("") }

    val subjectChips = listOf(
        "Mathematics & Science",
        "Physics & Chemistry",
        "Biology & EVS",
        "Commerce & Economics",
        "History, Civics & Geography",
        "Hindi & English Grammar"
    )

    val quickPrompts = listOf(
        language.pick(
            "Explain Trigonometry sin²θ + cos²θ = 1 with trick",
            "त्रिकोणमिति sin²θ + cos²θ = 1 आसान ट्रिक से समझाएं"
        ),
        language.pick(
            "Difference between Arteries & Veins in Hindi & English",
            "धमनी (Artery) और शिरा (Vein) में अंतर बताएं"
        ),
        language.pick(
            "Explain Law of Demand with an example",
            "अर्थशास्त्र में मांग का नियम (Law of Demand) उदाहरण सहित समझाएं"
        ),
        language.pick(
            "How to solve Quadratic Equations step-by-step?",
            "द्विघात समीकरण (Quadratic Equations) हल करने का आसान तरीका"
        )
    )

    Column(modifier = modifier.fillMaxSize()) {
        // Subject Context Filter Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(subjectChips) { subj ->
                FilterChip(
                    selected = selectedSubject == subj,
                    onClick = { selectedSubject = subj },
                    label = { Text(subj) }
                )
            }
        }

        // Quick Doubt Prompts Row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickPrompts) { promptText ->
                AssistChip(
                    onClick = { onAskDoubt(promptText, selectedSubject) },
                    label = { Text(promptText) }
                )
            }
        }

        // Chat Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("doubt_chat_messages_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatMessages, key = { it.id }) { msg ->
                val alignment = if (msg.isUser) Alignment.End else Alignment.Start
                val bubbleColor = when {
                    msg.isError -> MaterialTheme.colorScheme.errorContainer
                    msg.isUser -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                val textColor = when {
                    msg.isError -> MaterialTheme.colorScheme.onErrorContainer
                    msg.isUser -> MaterialTheme.colorScheme.onPrimary
                    else -> MaterialTheme.colorScheme.onSurface
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = alignment
                ) {
                    Text(
                        text = if (msg.isUser) {
                            "You • Class ${if (selectedClass in 1..12) selectedClass else 10} (${msg.subjectTag})"
                        } else {
                            "🤖 Vidya Guru AI"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                    Surface(
                        color = bubbleColor,
                        shape = RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (msg.isUser) 18.dp else 4.dp,
                            bottomEnd = if (msg.isUser) 4.dp else 18.dp
                        ),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = msg.text,
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor
                            )
                            if (msg.isStreaming) {
                                Spacer(modifier = Modifier.height(6.dp))
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Doubt Input Bar
        Surface(
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = doubtInput,
                    onValueChange = { doubtInput = it },
                    placeholder = {
                        Text(
                            language.pick(
                                "Ask any Class 1–12 doubt in Hindi or English...",
                                "कक्षा 1–12 का कोई भी प्रश्न हिंदी या अंग्रेजी में पूछें..."
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("doubt_chat_input"),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (doubtInput.isNotBlank()) {
                            onAskDoubt(doubtInput, selectedSubject)
                            doubtInput = ""
                        }
                    },
                    enabled = !isChatGenerating && doubtInput.isNotBlank(),
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .testTag("send_doubt_button")
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send Doubt",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun StudyPlannerSection(
    language: AppLanguage,
    selectedClass: Int,
    studyTasks: List<StudyTaskEntity>,
    aiStudyPlanState: AiTaskState<String>,
    onGenerateStudyPlan: (Int, String, Int, String) -> Unit,
    onToggleStudyTask: (StudyTaskEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var focusSubjects by remember { mutableStateOf("Mathematics, Science & English") }
    var examGoal by remember { mutableStateOf("Board / School Exam 95%+ Score") }
    var dailyHours by remember { mutableIntStateOf(3) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("study_planner_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // AI Study Plan Generator Card
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "📅 " + language.pick(
                            "Personalized AI Study Plan (Class ${if (selectedClass in 1..12) selectedClass else 10})",
                            "व्यक्तिगत AI अध्ययन योजना (कक्षा ${if (selectedClass in 1..12) selectedClass else 10})"
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = focusSubjects,
                        onValueChange = { focusSubjects = it },
                        label = {
                            Text(language.pick("Focus Subjects", "मुख्य विषय (Subjects)"))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("study_plan_subjects_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = examGoal,
                        onValueChange = { examGoal = it },
                        label = {
                            Text(language.pick("Target Exam / Goal", "लक्ष्य परीक्षा (Goal)"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = language.pick("Daily Hours:", "प्रतिदिन घंटे:"),
                            style = MaterialTheme.typography.labelLarge
                        )
                        listOf(2, 3, 4, 6).forEach { hrs ->
                            FilterChip(
                                selected = dailyHours == hrs,
                                onClick = { dailyHours = hrs },
                                label = { Text("${hrs}h/day") }
                            )
                        }
                    }

                    Button(
                        onClick = {
                            onGenerateStudyPlan(
                                if (selectedClass in 1..12) selectedClass else 10,
                                focusSubjects,
                                dailyHours,
                                examGoal
                            )
                        },
                        enabled = aiStudyPlanState !is AiTaskState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_study_plan_button")
                    ) {
                        if (aiStudyPlanState is AiTaskState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(language.pick("Building Your 6-Day Timetable...", "आपका टाइमटेबल बन रहा है..."))
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                language.pick(
                                    "Generate Personalized AI Study Plan (+30 XP)",
                                    "AI से पर्सनलाइज्ड स्टडी प्लान बनाएं (+30 XP)"
                                )
                            )
                        }
                    }

                    if (aiStudyPlanState is AiTaskState.Success) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "🎯 Coach Advice: ${aiStudyPlanState.data}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    } else if (aiStudyPlanState is AiTaskState.Error) {
                        Text(
                            text = aiStudyPlanState.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Daily Study Tasks Checklist
        item {
            val completedCount = studyTasks.count { it.isCompleted }
            Text(
                text = language.pick(
                    "Your Study Schedule ($completedCount/${studyTasks.size} Completed)",
                    "आपका अध्ययन कार्यक्रम ($completedCount/${studyTasks.size} पूर्ण)"
                ),
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(studyTasks, key = { it.id }) { task ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleStudyTask(task) }
                    .testTag("study_task_card_${task.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (task.isCompleted) {
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggleStudyTask(task) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${task.dayLabel} • ${task.subject} (${task.durationMinutes} min)",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = language.pick(task.topicEn, task.topicHi),
                            style = MaterialTheme.typography.bodyLarge,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "+${task.xpReward} XP",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
