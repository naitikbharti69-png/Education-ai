package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.CurriculumCatalog
import com.example.data.local.DownloadedLessonEntity
import com.example.model.AiGeneratedSummary
import com.example.model.AppLanguage
import com.example.model.LectureItem
import com.example.model.SubjectStream
import com.example.model.pick
import com.example.ui.components.InteractiveDiagramCanvas
import com.example.viewmodel.AiTaskState
import kotlinx.coroutines.delay

@Composable
fun LecturesScreen(
    language: AppLanguage,
    selectedClass: Int,
    selectedStream: SubjectStream,
    isOfflineMode: Boolean,
    selectedLecture: LectureItem,
    downloadedLessons: List<DownloadedLessonEntity>,
    lectureAiState: AiTaskState<AiGeneratedSummary>,
    onSelectClass: (Int) -> Unit,
    onSelectStream: (SubjectStream) -> Unit,
    onToggleOfflineMode: (Boolean) -> Unit,
    onSelectLecture: (LectureItem) -> Unit,
    onDownloadLecture: (LectureItem, AiGeneratedSummary?) -> Unit,
    onRemoveOfflineLecture: (String) -> Unit,
    onGenerateAiSummary: (LectureItem) -> Unit,
    onOpenInDiagramStudio: (LectureItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val downloadedIds = remember(downloadedLessons) { downloadedLessons.map { it.lectureId }.toSet() }
    val filteredLectures = remember(selectedClass, selectedStream, isOfflineMode, downloadedIds) {
        CurriculumCatalog.lectures.filter { lec ->
            val classMatch = selectedClass == 0 || lec.classNumber == selectedClass
            val streamMatch = selectedStream == SubjectStream.ALL || lec.stream == selectedStream
            val offlineMatch = !isOfflineMode || downloadedIds.contains(lec.id)
            classMatch && streamMatch && offlineMatch
        }
    }

    var isPlaying by remember(selectedLecture.id) { mutableStateOf(false) }
    var playbackProgress by remember(selectedLecture.id) { mutableFloatStateOf(0.18f) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }

    LaunchedEffect(isPlaying, playbackSpeed, selectedLecture.id) {
        while (isPlaying) {
            delay(250L)
            val next = playbackProgress + 0.008f * playbackSpeed
            if (next >= 1f) {
                playbackProgress = 1f
                isPlaying = false
            } else {
                playbackProgress = next
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "lectureAnim")
    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("lectures_list"),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Hero Banner + Offline Mode Switch
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_study_1793288870464),
                        contentDescription = "VidyaSetu Hero Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x880F172A),
                                        Color(0xEE0F172A)
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = Color(0xFFFBBF24),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = language.pick(
                                            "Classes 1st – 12th • All Subjects",
                                            "कक्षा 1 से 12 • सभी विषय",
                                            "Class 1st–12th • सभी विषय (Hindi & English)"
                                        ),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color(0xFF0F172A),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                    )
                                }

                                // Offline Mode Switch Pill
                                Surface(
                                    color = if (isOfflineMode) Color(0xFF059669) else Color(0xAA1E293B),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .clickable { onToggleOfflineMode(!isOfflineMode) }
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                            .testTag("offline_mode_switch")
                                    ) {
                                        Icon(
                                            imageVector = if (isOfflineMode) Icons.Default.CloudOff else Icons.Default.CloudDone,
                                            contentDescription = "Offline Mode",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = language.pick(
                                                if (isOfflineMode) "Offline Mode ON" else "Online + Offline",
                                                if (isOfflineMode) "ऑफलाइन मोड चालू" else "ऑनलाइन + ऑफलाइन"
                                            ),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Column {
                                Text(
                                    text = language.pick(
                                        "Recorded Lectures, AI Notes & Live Diagrams",
                                        "रिकॉर्डेड लेक्चर, AI नोट्स और इंटरैक्टिव चित्र",
                                        "Recorded Lectures, AI Summary Notes & लाइव डायग्राम"
                                    ),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = language.pick(
                                        "Science, Maths, Commerce, Arts & Foundation in Hindi & English",
                                        "विज्ञान, गणित, वाणिज्य, कला और फाउंडेशन — हिंदी और अंग्रेजी में"
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFFCBD5E1)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Class 1st to 12th Selector Row
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = language.pick("Select Class (कक्षा चुनें):", "अपनी कक्षा चुनें (Class 1–12):"),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("class_selector_row")
                ) {
                    item {
                        FilterChip(
                            selected = selectedClass == 0,
                            onClick = { onSelectClass(0) },
                            label = { Text(language.pick("All Classes (1–12)", "सभी कक्षाएं (1–12)")) },
                            modifier = Modifier.testTag("class_chip_all")
                        )
                    }
                    items((1..12).toList()) { cNum ->
                        FilterChip(
                            selected = selectedClass == cNum,
                            onClick = { onSelectClass(cNum) },
                            label = {
                                Text(language.pick("Class $cNum", "कक्षा $cNum", "Class $cNum (कक्षा $cNum)"))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.testTag("class_chip_$cNum")
                        )
                    }
                }
            }
        }

        // 3. Subject Stream Filter Row
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(SubjectStream.entries) { stream ->
                    FilterChip(
                        selected = selectedStream == stream,
                        onClick = { onSelectStream(stream) },
                        label = {
                            Text(language.pick(stream.labelEn, stream.labelHi))
                        },
                        modifier = Modifier.testTag("stream_chip_${stream.name}")
                    )
                }
            }
        }

        // 4. Active Recorded Video Lecture Player + AI Notes & Diagram Card
        item {
            val isDownloaded = downloadedIds.contains(selectedLecture.id)
            val currentAiSummary = (lectureAiState as? AiTaskState.Success)?.data

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("active_lecture_player_card"),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Animated Video / Blackboard Canvas Display
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        InteractiveDiagramCanvas(
                            diagramType = selectedLecture.diagramType,
                            showBlueprint = true,
                            animationPhase = if (isPlaying) animPhase else playbackProgress,
                            strokes = emptyList(),
                            isDrawingEnabled = false,
                            activeColorHex = 0xFFFBBF24,
                            activeStrokeWidth = 6f,
                            activeToolShape = "FREEHAND",
                            onStrokeCompleted = {},
                            modifier = Modifier.fillMaxSize()
                        )

                        // Top Badge overlay: Class + Subject + XP
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color(0xCC0F172A),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Class ${selectedLecture.classNumber} • ${
                                        language.pick(selectedLecture.subjectEn, selectedLecture.subjectHi)
                                    }",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color(0xFFFBBF24),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            Surface(
                                color = Color(0xCC065F46),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "+${selectedLecture.xpReward} XP",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        // Bottom Captions + Play/Pause Controls Overlay
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color(0xEE0F172A))
                                    )
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = language.pick(
                                    selectedLecture.transcriptEn,
                                    selectedLecture.transcriptHi
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { isPlaying = !isPlaying },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFFBBF24), CircleShape)
                                        .testTag("lecture_play_pause_button")
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "Pause Lecture" else "Play Lecture",
                                        tint = Color(0xFF0F172A)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Slider(
                                    value = playbackProgress,
                                    onValueChange = { playbackProgress = it },
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                AssistChip(
                                    onClick = {
                                        playbackSpeed = when (playbackSpeed) {
                                            1.0f -> 1.25f
                                            1.25f -> 1.5f
                                            1.5f -> 2.0f
                                            else -> 1.0f
                                        }
                                    },
                                    label = {
                                        Text("${playbackSpeed}x", color = Color.White)
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Speed,
                                            contentDescription = "Playback Speed",
                                            tint = Color(0xFFFBBF24),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // Lecture Metadata & Action Buttons
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = language.pick(selectedLecture.titleEn, selectedLecture.titleHi),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "👩‍🏫 ${selectedLecture.instructor} • ⏱ ${selectedLecture.durationMinutes} mins",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // Chapter Timestamps Row
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(selectedLecture.chapters) { chap ->
                                AssistChip(
                                    onClick = {
                                        playbackProgress = chap.progressFraction
                                        isPlaying = true
                                    },
                                    label = {
                                        Text("${chap.timestampLabel} • ${language.pick(chap.titleEn, chap.titleHi)}")
                                    }
                                )
                            }
                        }

                        // Action Buttons Row: AI Summary + Draw Diagram + Offline Download
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onGenerateAiSummary(selectedLecture) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("generate_ai_summary_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = "AI Notes",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = language.pick("AI Summary & Notes", "AI सारांश और नोट्स"),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            FilledTonalButton(
                                onClick = { onOpenInDiagramStudio(selectedLecture) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("open_diagram_studio_button")
                            ) {
                                Icon(
                                    Icons.Default.Brush,
                                    contentDescription = "Draw Diagram",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = language.pick("Draw Diagram", "चित्र बनाएं"),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Offline Download Button
                        OutlinedButton(
                            onClick = {
                                if (isDownloaded) {
                                    onRemoveOfflineLecture(selectedLecture.id)
                                } else {
                                    onDownloadLecture(selectedLecture, currentAiSummary)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("offline_download_button")
                        ) {
                            Icon(
                                imageVector = if (isDownloaded) Icons.Default.CheckCircle else Icons.Default.CloudDownload,
                                contentDescription = "Offline Download",
                                tint = if (isDownloaded) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isDownloaded) {
                                    language.pick(
                                        "Saved in Offline Library (Tap to Remove)",
                                        "ऑफलाइन लाइब्रेरी में सेव है (हटाने के लिए टैप करें)"
                                    )
                                } else {
                                    language.pick(
                                        "Download Lecture, Notes & Diagram for Offline Mode (+25 XP)",
                                        "ऑफलाइन पढ़ाई के लिए लेक्चर और नोट्स डाउनलोड करें (+25 XP)"
                                    )
                                }
                            )
                        }

                        // AI Summary & Notes Display Box
                        when (lectureAiState) {
                            is AiTaskState.Loading -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                            RoundedCornerShape(16.dp)
                                        )
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 3.dp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = language.pick(
                                            "Gemini AI is generating bilingual revision notes, formulas & diagram drawing steps...",
                                            "Gemini AI आपके लिए लेक्चर का सारांश, सूत्र और चित्र बनाने के चरण तैयार कर रहा है..."
                                        ),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            is AiTaskState.Error -> {
                                Surface(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = lectureAiState.message,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                            is AiTaskState.Success -> {
                                val data = lectureAiState.data
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("ai_generated_summary_box")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "✨ " + language.pick(
                                                "AI Generated Smart Notes & Diagram Guide",
                                                "AI द्वारा तैयार स्मार्ट नोट्स और चित्र गाइड"
                                            ),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        data.summaryBullets.forEach { bullet ->
                                            Text("• $bullet", style = MaterialTheme.typography.bodyMedium)
                                        }
                                        if (data.keyFormulasOrFacts.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = language.pick("📌 Key Formulas / Facts:", "📌 मुख्य सूत्र और तथ्य:"),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            data.keyFormulasOrFacts.forEach { f ->
                                                Text("  ▸ $f", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                            }
                                        }
                                        if (data.diagramDrawingSteps.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = language.pick("📐 How to Draw the Diagram:", "📐 परीक्षा में चित्र कैसे बनाएं:"),
                                                style = MaterialTheme.typography.titleSmall
                                            )
                                            data.diagramDrawingSteps.forEachIndexed { idx, step ->
                                                Text("  ${idx + 1}. $step", style = MaterialTheme.typography.bodyMedium)
                                            }
                                        }
                                        Surface(
                                            color = MaterialTheme.colorScheme.secondaryContainer,
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(
                                                text = "💡 Exam Tip: ${data.examTip}",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            AiTaskState.Idle -> {
                                // Show Pre-Bundled Quick Revision Notes & Key Formulas
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "📘 " + language.pick(
                                                "Quick Lecture Notes & Key Formulas (Available Offline)",
                                                "त्वरित लेक्चर नोट्स और मुख्य सूत्र (ऑफलाइन उपलब्ध)"
                                            ),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        val notes = when (language) {
                                            AppLanguage.ENGLISH -> selectedLecture.defaultSummaryEn
                                            AppLanguage.HINDI -> selectedLecture.defaultSummaryHi
                                            AppLanguage.HINDI_ENGLISH -> selectedLecture.defaultSummaryEn.zip(
                                                selectedLecture.defaultSummaryHi
                                            ) { en, hi -> "$en\n   ($hi)" }
                                        }
                                        notes.forEach { n ->
                                            Text("• $n", style = MaterialTheme.typography.bodyMedium)
                                        }
                                        selectedLecture.keyFormulas.forEach { formula ->
                                            Surface(
                                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "⚡ $formula",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Section Header for Lecture Catalog
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isOfflineMode) {
                        language.pick(
                            "Downloaded Offline Lectures (${filteredLectures.size})",
                            "डाउनलोड किए गए ऑफलाइन लेक्चर (${filteredLectures.size})"
                        )
                    } else {
                        language.pick(
                            "Recorded Video Lectures (${filteredLectures.size})",
                            "रिकॉर्डेड वीडियो लेक्चर (${filteredLectures.size})"
                        )
                    },
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        if (filteredLectures.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Empty",
                            modifier = Modifier.size(42.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isOfflineMode) {
                                language.pick(
                                    "No offline lectures downloaded for this filter yet. Turn off Offline Mode or tap 'Download' on any lecture!",
                                    "इस फ़िल्टर के लिए अभी कोई ऑफलाइन लेक्चर डाउनलोड नहीं है। किसी भी लेक्चर को डाउनलोड करें!"
                                )
                            } else {
                                language.pick(
                                    "No lectures match this Class + Stream filter. Tap 'All Classes (1–12)' or 'All Streams' to explore all lessons!",
                                    "इस कक्षा और स्ट्रीम के लिए सभी लेक्चर देखने हेतु 'सभी कक्षाएं (1–12)' या 'सभी विषय' चुनें!"
                                )
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = {
                                onSelectClass(0)
                                onSelectStream(SubjectStream.ALL)
                                onToggleOfflineMode(false)
                            }
                        ) {
                            Text(language.pick("Show All Classes 1–12 Lectures", "सभी कक्षा 1–12 लेक्चर दिखाएं"))
                        }
                    }
                }
            }
        } else {
            items(filteredLectures, key = { it.id }) { lecture ->
                val isSelected = lecture.id == selectedLecture.id
                val isDownloaded = downloadedIds.contains(lecture.id)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { onSelectLecture(lecture) }
                        .testTag("lecture_card_${lecture.id}"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Class",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                                Text(
                                    text = "${lecture.classNumber}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = language.pick(lecture.subjectEn, lecture.subjectHi),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                if (isDownloaded) {
                                    Icon(
                                        imageVector = Icons.Default.CloudDone,
                                        contentDescription = "Available Offline",
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = language.pick(lecture.titleEn, lecture.titleHi),
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${lecture.instructor} • ${lecture.durationMinutes} min • 📐 ${
                                    language.pick(lecture.diagramType.titleEn, lecture.diagramType.titleHi)
                                }",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
