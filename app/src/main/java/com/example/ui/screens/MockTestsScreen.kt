package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CurriculumCatalog
import com.example.data.local.TestAttemptEntity
import com.example.model.AppLanguage
import com.example.model.MockTestItem
import com.example.model.pick
import com.example.viewmodel.AiTaskState
import kotlinx.coroutines.delay

@Composable
fun MockTestsScreen(
    language: AppLanguage,
    selectedClass: Int,
    customMockTests: List<MockTestItem>,
    testAttempts: List<TestAttemptEntity>,
    aiQuizGenState: AiTaskState<MockTestItem>,
    onGenerateAiMockTest: (Int, String) -> Unit,
    onSubmitTestResult: (MockTestItem, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTest by remember { mutableStateOf<MockTestItem?>(null) }
    var customTopic by remember { mutableStateOf("Trigonometry & Light Reflection") }

    // Automatically open newly generated AI test when ready
    LaunchedEffect(aiQuizGenState) {
        if (aiQuizGenState is AiTaskState.Success) {
            activeTest = aiQuizGenState.data
        }
    }

    val currentActiveTest = activeTest
    if (currentActiveTest != null) {
        ActiveMockTestRunner(
            test = currentActiveTest,
            language = language,
            onFinishTest = { score ->
                onSubmitTestResult(currentActiveTest, score)
            },
            onExit = { activeTest = null },
            modifier = modifier
        )
        return
    }

    val allTests = remember(customMockTests) {
        customMockTests + CurriculumCatalog.mockTests
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("mock_tests_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = language.pick(
                        "Mock Tests & AI Quiz Generator (Class 1–12)",
                        "मॉक टेस्ट और AI प्रश्नोत्तरी जनरेटर (कक्षा 1–12)"
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = language.pick(
                        "Practice bilingual (Hindi & English) mock tests with step-by-step solutions or generate a new AI Mock Test on any topic!",
                        "हिंदी और अंग्रेजी में चरण-दर-चरण समाधान के साथ मॉक टेस्ट दें या किसी भी विषय पर नया AI मॉक टेस्ट बनाएं!"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 2. AI Custom Mock Test Generator Card
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "✨ " + language.pick(
                            "Generate Custom AI Mock Test (Class ${if (selectedClass in 1..12) selectedClass else 10})",
                            "किसी भी अध्याय पर नया AI मॉक टेस्ट बनाएं (कक्षा ${if (selectedClass in 1..12) selectedClass else 10})"
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedTextField(
                        value = customTopic,
                        onValueChange = { customTopic = it },
                        label = {
                            Text(
                                language.pick(
                                    "Subject / Chapter (e.g. Fractions, Cell, Calculus, Economics, Grammar)",
                                    "विषय / अध्याय लिखें (जैसे: भिन्न, कोशिका, त्रिकोणमिति, अर्थशास्त्र, व्याकरण)"
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_mock_topic_input"),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            onGenerateAiMockTest(
                                if (selectedClass in 1..12) selectedClass else 10,
                                customTopic.ifBlank { "General Science & Mathematics" }
                            )
                        },
                        enabled = aiQuizGenState !is AiTaskState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("generate_ai_mock_button")
                    ) {
                        if (aiQuizGenState is AiTaskState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(language.pick("Creating Bilingual Mock Test...", "द्विभाषी मॉक टेस्ट बन रहा है..."))
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                language.pick(
                                    "Generate & Start AI Mock Test (+25 XP)",
                                    "AI मॉक टेस्ट बनाएं और शुरू करें (+25 XP)"
                                )
                            )
                        }
                    }

                    if (aiQuizGenState is AiTaskState.Error) {
                        Text(
                            text = aiQuizGenState.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // 3. Available Mock Tests List
        item {
            Text(
                text = language.pick(
                    "Available Mock Tests (${allTests.size})",
                    "उपलब्ध मॉक टेस्ट (${allTests.size})"
                ),
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(allTests, key = { it.id }) { testItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { activeTest = testItem }
                    .testTag("mock_test_card_${testItem.id}"),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.Quiz,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = language.pick(testItem.titleEn, testItem.titleHi),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${testItem.questions.size} Questions • ⏱ ${testItem.durationMinutes} min • 🏆 +${testItem.xpReward} XP",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    FilledTonalButton(
                        onClick = { activeTest = testItem },
                        modifier = Modifier.testTag("start_test_button_${testItem.id}")
                    ) {
                        Text(language.pick("Start", "शुरू करें"))
                    }
                }
            }
        }

        // 4. Recent Attempts History
        if (testAttempts.isNotEmpty()) {
            item {
                Text(
                    text = language.pick(
                        "Recent Test Results (${testAttempts.size})",
                        "हाल के टेस्ट परिणाम (${testAttempts.size})"
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(testAttempts.take(5), key = { it.id }) { attempt ->
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(attempt.testTitle, style = MaterialTheme.typography.titleSmall)
                            Text(
                                "Class ${attempt.classNumber} • ${attempt.subject}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "Score: ${attempt.score}/${attempt.totalQuestions} (+${attempt.xpEarned} XP)",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveMockTestRunner(
    test: MockTestItem,
    language: AppLanguage,
    onFinishTest: (Int) -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onExit() }

    var currentIndex by remember { mutableIntStateOf(0) }
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    var isSubmitted by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableIntStateOf(test.durationMinutes * 60) }

    LaunchedEffect(isSubmitted) {
        while (!isSubmitted && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
        }
    }

    val currentQuestion = test.questions[currentIndex]
    val totalQuestions = test.questions.size
    val chosenOption = selectedAnswers[currentIndex]

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("active_mock_test_runner"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar with Back & Timer
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onExit,
                    modifier = Modifier.testTag("exit_mock_test_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(language.pick("Exit Test", "बाहर निकलें"))
                }

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = "Timer",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val mins = remainingSeconds / 60
                        val secs = remainingSeconds % 60
                        Text(
                            text = "%02d:%02d".format(mins, secs),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }

        // Progress Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = language.pick(test.titleEn, test.titleHi),
                    style = MaterialTheme.typography.titleLarge
                )
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / totalQuestions.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
                Text(
                    text = language.pick(
                        "Question ${currentIndex + 1} of $totalQuestions",
                        "प्रश्न ${currentIndex + 1} / $totalQuestions"
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Question Card & Options
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = language.pick(
                            currentQuestion.questionEn,
                            currentQuestion.questionHi,
                            "${currentQuestion.questionEn}\n(${currentQuestion.questionHi})"
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )

                    for (idx in 0 until 4) {
                        val optEn = currentQuestion.optionsEn.getOrElse(idx) { "" }
                        val optHi = currentQuestion.optionsHi.getOrElse(idx) { optEn }
                        val isChosen = chosenOption == idx
                        val isCorrect = currentQuestion.correctIndex == idx

                        val containerColor = when {
                            isSubmitted && isCorrect -> MaterialTheme.colorScheme.tertiaryContainer
                            isSubmitted && isChosen && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                            isChosen -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                        }

                        Surface(
                            color = containerColor,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !isSubmitted) {
                                    selectedAnswers[currentIndex] = idx
                                }
                                .testTag("quiz_option_$idx")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A' + idx)}.",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = language.pick(optEn, optHi),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSubmitted && isCorrect) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }
                        }
                    }

                    // Show Explanation once option selected or submitted
                    if (chosenOption != null || isSubmitted) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "💡 " + language.pick("Step-by-Step Solution:", "चरण-दर-चरण हल (Explanation):"),
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = language.pick(
                                        currentQuestion.explanationEn,
                                        currentQuestion.explanationHi
                                    ),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Prev / Next / Submit Controls
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { if (currentIndex > 0) currentIndex-- },
                    enabled = currentIndex > 0,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(language.pick("Previous", "पिछला"))
                }

                if (currentIndex < totalQuestions - 1) {
                    Button(
                        onClick = { currentIndex++ },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("next_question_button")
                    ) {
                        Text(language.pick("Next Question", "अगला प्रश्न"))
                    }
                } else if (!isSubmitted) {
                    Button(
                        onClick = {
                            isSubmitted = true
                            val score = test.questions.indices.count { i ->
                                selectedAnswers[i] == test.questions[i].correctIndex
                            }
                            onFinishTest(score)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("submit_mock_test_button")
                    ) {
                        Text(language.pick("Submit Test", "टेस्ट सबमिट करें"))
                    }
                }
            }
        }

        // Final Score Summary Banner when Submitted
        if (isSubmitted) {
            item {
                val finalScore = test.questions.indices.count { i ->
                    selectedAnswers[i] == test.questions[i].correctIndex
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("test_score_result_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            modifier = Modifier.size(44.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = language.pick(
                                "Test Completed! Score: $finalScore / $totalQuestions",
                                "टेस्ट पूरा हुआ! आपका स्कोर: $finalScore / $totalQuestions"
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            text = language.pick(
                                "You earned +${(finalScore * 25) + 20} XP towards your next scholar badge!",
                                "आपने +${(finalScore * 25) + 20} XP अंक अर्जित किए!"
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Button(onClick = onExit) {
                            Text(language.pick("Back to All Mock Tests", "सभी मॉक टेस्ट पर वापस जाएं"))
                        }
                    }
                }
            }
        }
    }
}
