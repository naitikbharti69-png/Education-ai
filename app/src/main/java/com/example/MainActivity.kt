package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.OndemandVideo
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.model.pick
import com.example.ui.screens.AiDoubtAndPlannerScreen
import com.example.ui.screens.DiagramStudioScreen
import com.example.ui.screens.LecturesScreen
import com.example.ui.screens.MockTestsScreen
import com.example.ui.screens.ProgressDashboardScreen
import com.example.ui.theme.VidyaSetuTheme
import com.example.util.configureAppCheck
import com.example.viewmodel.VidyaViewModel

enum class MainTab(
    val labelEn: String,
    val labelHi: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
) {
    LECTURES("Lectures", "लेक्चर", Icons.Filled.OndemandVideo, Icons.Outlined.OndemandVideo, "tab_lectures"),
    DIAGRAMS("Diagrams", "चित्र", Icons.Filled.Brush, Icons.Outlined.Brush, "tab_diagrams"),
    MOCK_TESTS("Mock Tests", "टेस्ट", Icons.Filled.Quiz, Icons.Outlined.Quiz, "tab_mock_tests"),
    AI_TUTOR("AI Tutor", "AI गुरु", Icons.Filled.Psychology, Icons.Outlined.Psychology, "tab_ai_tutor"),
    PROGRESS("Progress", "प्रगति", Icons.Filled.EmojiEvents, Icons.Outlined.EmojiEvents, "tab_progress")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Configure App Check with launch intent before any UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        // STEP 2: Initialize UI
        setContent {
            VidyaSetuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VidyaSetuApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VidyaSetuApp(
    vidyaViewModel: VidyaViewModel = viewModel()
) {
    var currentTab by remember { mutableStateOf(MainTab.LECTURES) }

    val language by vidyaViewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedClass by vidyaViewModel.selectedClass.collectAsStateWithLifecycle()
    val selectedStream by vidyaViewModel.selectedStream.collectAsStateWithLifecycle()
    val isOfflineMode by vidyaViewModel.isOfflineModeOnly.collectAsStateWithLifecycle()
    val selectedLecture by vidyaViewModel.selectedLecture.collectAsStateWithLifecycle()
    val lectureAiState by vidyaViewModel.lectureAiState.collectAsStateWithLifecycle()

    val activeDiagramType by vidyaViewModel.activeDiagramType.collectAsStateWithLifecycle()
    val canvasStrokes by vidyaViewModel.canvasStrokes.collectAsStateWithLifecycle()
    val aiDiagramStepsState by vidyaViewModel.aiDiagramStepsState.collectAsStateWithLifecycle()

    val downloadedLessons by vidyaViewModel.downloadedLessons.collectAsStateWithLifecycle()
    val savedNotes by vidyaViewModel.savedNotes.collectAsStateWithLifecycle()
    val testAttempts by vidyaViewModel.testAttempts.collectAsStateWithLifecycle()
    val studyTasks by vidyaViewModel.studyTasks.collectAsStateWithLifecycle()

    val customMockTests by vidyaViewModel.customMockTests.collectAsStateWithLifecycle()
    val aiQuizGenState by vidyaViewModel.aiQuizGenState.collectAsStateWithLifecycle()
    val aiStudyPlanState by vidyaViewModel.aiStudyPlanState.collectAsStateWithLifecycle()

    val chatMessages by vidyaViewModel.chatMessages.collectAsStateWithLifecycle()
    val isChatGenerating by vidyaViewModel.isChatGenerating.collectAsStateWithLifecycle()
    val watchedLectureIds by vidyaViewModel.watchedLectureIds.collectAsStateWithLifecycle()
    val bonusXp by vidyaViewModel.bonusXp.collectAsStateWithLifecycle()

    if (currentTab != MainTab.LECTURES) {
        BackHandler {
            currentTab = MainTab.LECTURES
        }
    }

    val badges = remember(bonusXp, downloadedLessons.size, savedNotes.size, testAttempts.size) {
        vidyaViewModel.computeBadges(
            totalXp = bonusXp,
            downloadedCount = downloadedLessons.size,
            savedNotesCount = savedNotes.size,
            attemptsCount = testAttempts.size
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "VidyaSetu • विद्यासेतु",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = language.pick(
                                "Class 1–12 All Subjects • AI Smart Learning",
                                "कक्षा 1–12 सभी विषय • AI स्मार्ट लर्निंग"
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // XP Badge Pill
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .clickable { currentTab = MainTab.PROGRESS }
                            .testTag("top_xp_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Stars,
                                contentDescription = "XP",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$bonusXp XP",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Language Switcher Pill (Cycles Hinglish -> Hindi -> English)
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable {
                                val next = when (language) {
                                    AppLanguage.HINDI_ENGLISH -> AppLanguage.HINDI
                                    AppLanguage.HINDI -> AppLanguage.ENGLISH
                                    AppLanguage.ENGLISH -> AppLanguage.HINDI_ENGLISH
                                }
                                vidyaViewModel.setLanguage(next)
                            }
                            .testTag("language_switcher_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Language,
                                contentDescription = "Language Switcher",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = language.label,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    val selected = currentTab == tab
                    NavigationBarItem(
                        selected = selected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.labelEn
                            )
                        },
                        label = {
                            Text(
                                text = language.pick(tab.labelEn, tab.labelHi, tab.labelEn),
                                maxLines = 1
                            )
                        },
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.LECTURES -> {
                    LecturesScreen(
                        language = language,
                        selectedClass = selectedClass,
                        selectedStream = selectedStream,
                        isOfflineMode = isOfflineMode,
                        selectedLecture = selectedLecture,
                        downloadedLessons = downloadedLessons,
                        lectureAiState = lectureAiState,
                        onSelectClass = vidyaViewModel::setSelectedClass,
                        onSelectStream = vidyaViewModel::setSelectedStream,
                        onToggleOfflineMode = vidyaViewModel::toggleOfflineMode,
                        onSelectLecture = vidyaViewModel::selectLecture,
                        onDownloadLecture = vidyaViewModel::downloadLectureOffline,
                        onRemoveOfflineLecture = vidyaViewModel::removeOfflineLecture,
                        onGenerateAiSummary = { lec ->
                            vidyaViewModel.generateLectureSummaryWithAi(lec)
                        },
                        onOpenInDiagramStudio = { lec ->
                            vidyaViewModel.selectLecture(lec)
                            vidyaViewModel.selectDiagramType(lec.diagramType)
                            currentTab = MainTab.DIAGRAMS
                        }
                    )
                }

                MainTab.DIAGRAMS -> {
                    DiagramStudioScreen(
                        language = language,
                        activeDiagramType = activeDiagramType,
                        canvasStrokes = canvasStrokes,
                        savedNotes = savedNotes,
                        aiDiagramStepsState = aiDiagramStepsState,
                        onSelectDiagramType = vidyaViewModel::selectDiagramType,
                        onAddStroke = vidyaViewModel::addCanvasStroke,
                        onUndoStroke = vidyaViewModel::undoLastStroke,
                        onClearCanvas = vidyaViewModel::clearCanvasStrokes,
                        onSaveDiagramNote = vidyaViewModel::saveDiagramAndNoteToRoom,
                        onLoadSavedNote = vidyaViewModel::loadSavedDiagramNote,
                        onDeleteSavedNote = vidyaViewModel::deleteSavedDiagramNote,
                        onAskAiDiagramCoach = vidyaViewModel::generateDiagramStepsWithAi
                    )
                }

                MainTab.MOCK_TESTS -> {
                    MockTestsScreen(
                        language = language,
                        selectedClass = selectedClass,
                        customMockTests = customMockTests,
                        testAttempts = testAttempts,
                        aiQuizGenState = aiQuizGenState,
                        onGenerateAiMockTest = vidyaViewModel::generateAiMockTest,
                        onSubmitTestResult = vidyaViewModel::submitMockTestAttempt
                    )
                }

                MainTab.AI_TUTOR -> {
                    AiDoubtAndPlannerScreen(
                        language = language,
                        selectedClass = selectedClass,
                        chatMessages = chatMessages,
                        isChatGenerating = isChatGenerating,
                        studyTasks = studyTasks,
                        aiStudyPlanState = aiStudyPlanState,
                        onAskDoubt = vidyaViewModel::askDoubtStream,
                        onGenerateStudyPlan = vidyaViewModel::generatePersonalizedStudyPlan,
                        onToggleStudyTask = vidyaViewModel::toggleStudyTaskCompletion
                    )
                }

                MainTab.PROGRESS -> {
                    ProgressDashboardScreen(
                        language = language,
                        totalXp = bonusXp,
                        watchedCount = watchedLectureIds.size,
                        downloadedLessons = downloadedLessons,
                        savedNotes = savedNotes,
                        testAttempts = testAttempts,
                        studyTasks = studyTasks,
                        badges = badges,
                        onRemoveOfflineLesson = vidyaViewModel::removeOfflineLecture
                    )
                }
            }
        }
    }
}
