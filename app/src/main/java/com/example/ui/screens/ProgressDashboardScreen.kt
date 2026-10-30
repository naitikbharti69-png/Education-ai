package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.DownloadedLessonEntity
import com.example.data.local.SavedDiagramNoteEntity
import com.example.data.local.StudyTaskEntity
import com.example.data.local.TestAttemptEntity
import com.example.model.AppLanguage
import com.example.model.GamifiedBadge
import com.example.model.pick

@Composable
fun ProgressDashboardScreen(
    language: AppLanguage,
    totalXp: Int,
    watchedCount: Int,
    downloadedLessons: List<DownloadedLessonEntity>,
    savedNotes: List<SavedDiagramNoteEntity>,
    testAttempts: List<TestAttemptEntity>,
    studyTasks: List<StudyTaskEntity>,
    badges: List<GamifiedBadge>,
    onRemoveOfflineLesson: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val level = (totalXp / 100) + 1
    val xpInCurrentLevel = totalXp % 100
    val rankTitle = when {
        level >= 4 -> language.pick("Aryabhata Gold Scholar", "आर्यभट्ट स्वर्ण विद्वान")
        level == 3 -> language.pick("Vigyan Ratna Master", "विज्ञान रत्न मास्टर")
        level == 2 -> language.pick("Vidya Setu Rising Star", "विद्या सेतु उभरता सितारा")
        else -> language.pick("Shiksha Explorer", "शिक्षा अन्वेषक")
    }

    val completedTasks = studyTasks.count { it.isCompleted }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("progress_dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Gamified Scholar Hero Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF1E3A8A),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Surface(
                                    color = Color(0xFFFBBF24),
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = "LEVEL $level • $rankTitle",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color(0xFF0F172A),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "$totalXp XP",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = Color.White
                                )
                            }

                            // Daily Streak Pill
                            Surface(
                                color = Color(0xFFEA580C),
                                shape = RoundedCornerShape(18.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.LocalFireDepartment,
                                        contentDescription = "Streak",
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "7 Days",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White
                                        )
                                        Text(
                                            text = language.pick("Study Streak", "लगातार पढ़ाई"),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                    }
                                }
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = language.pick(
                                        "Next Level Progress ($xpInCurrentLevel / 100 XP)",
                                        "अगले स्तर की प्रगति ($xpInCurrentLevel / 100 XP)"
                                    ),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFFCBD5E1)
                                )
                                Text(
                                    text = "${100 - xpInCurrentLevel} XP to Level ${level + 1}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFFFBBF24)
                                )
                            }
                            LinearProgressIndicator(
                                progress = { xpInCurrentLevel / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp),
                                color = Color(0xFFFBBF24),
                                trackColor = Color(0xFF334155)
                            )
                        }
                    }
                }
            }
        }

        // 2. Four Key Metric Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatMiniCard(
                    title = language.pick("Lectures", "लेक्चर"),
                    value = "$watchedCount",
                    subtitle = language.pick("Watched", "देखे गए"),
                    modifier = Modifier.weight(1f)
                )
                StatMiniCard(
                    title = language.pick("Mock Tests", "मॉक टेस्ट"),
                    value = "${testAttempts.size}",
                    subtitle = language.pick("Attempted", "पूरे किए"),
                    modifier = Modifier.weight(1f)
                )
                StatMiniCard(
                    title = language.pick("Diagrams", "चित्र नोट्स"),
                    value = "${savedNotes.size}",
                    subtitle = language.pick("Saved", "सेव किए"),
                    modifier = Modifier.weight(1f)
                )
                StatMiniCard(
                    title = language.pick("Offline", "ऑफलाइन"),
                    value = "${downloadedLessons.size}",
                    subtitle = language.pick("Downloads", "डाउनलोड"),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. Subject Mastery Bar Chart (Canvas)
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
                        text = "📊 " + language.pick(
                            "Subject Mastery & Learning Analytics (Class 1–12)",
                            "विषयवार प्रगति और लर्निंग एनालिटिक्स"
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )

                    val masteryItems = listOf(
                        Triple(language.pick("Mathematics", "गणित"), 0.86f, Color(0xFF2563EB)),
                        Triple(language.pick("Science (Phy/Chem/Bio)", "विज्ञान"), 0.82f, Color(0xFF059669)),
                        Triple(language.pick("Commerce & Economics", "वाणिज्य/अर्थशास्त्र"), 0.74f, Color(0xFF7C3AED)),
                        Triple(language.pick("Humanities & SST", "सामाजिक विज्ञान"), 0.78f, Color(0xFFD97706)),
                        Triple(language.pick("Hindi & English", "हिंदी व अंग्रेजी"), 0.90f, Color(0xFFE11D48))
                    )

                    masteryItems.forEach { (subj, ratio, barColor) ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(subj, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "${(ratio * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                            ) {
                                drawRoundRect(
                                    color = barColor.copy(alpha = 0.2f),
                                    size = Size(size.width, size.height),
                                    cornerRadius = CornerRadius(12f, 12f)
                                )
                                drawRoundRect(
                                    color = barColor,
                                    size = Size(size.width * ratio, size.height),
                                    cornerRadius = CornerRadius(12f, 12f)
                                )
                            }
                        }
                    }

                    Text(
                        text = language.pick(
                            "Completed $completedTasks/${studyTasks.size} Personalized Study Plan tasks this week.",
                            "इस सप्ताह $completedTasks/${studyTasks.size} अध्ययन कार्य पूरे किए गए।"
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 4. Gamified Badges Showcase
        item {
            Text(
                text = "🏆 " + language.pick(
                    "Gamified Scholar Badges & Rewards",
                    "गेमिफाइड स्कॉलर बैज और पुरस्कार"
                ),
                style = MaterialTheme.typography.titleMedium
            )
        }

        items(badges, key = { it.id }) { badge ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (badge.unlocked) {
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (badge.unlocked) Color(0xFFFBBF24) else Color.Gray.copy(alpha = 0.4f),
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (badge.unlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF0F172A)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = language.pick(badge.titleEn, badge.titleHi),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = language.pick(badge.descEn, badge.descHi),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        color = if (badge.unlocked) {
                            MaterialTheme.colorScheme.tertiaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = if (badge.unlocked) {
                                language.pick("Unlocked ✓", "अनलॉक ✓")
                            } else {
                                "${badge.requiredXp} XP"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // 5. Offline Downloaded Lessons Vault
        item {
            Text(
                text = "📥 " + language.pick(
                    "Offline Downloaded Lessons (${downloadedLessons.size})",
                    "ऑफलाइन डाउनलोड किए गए लेक्चर (${downloadedLessons.size})"
                ),
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (downloadedLessons.isEmpty()) {
            item {
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = language.pick(
                            "No lessons downloaded yet. Tap 'Download for Offline Mode' on any lecture in the Lectures tab to access notes & formulas anytime without internet!",
                            "अभी कोई लेक्चर डाउनलोड नहीं है। बिना इंटरनेट पढ़ाई के लिए लेक्चर टैब से कोई भी पाठ डाउनलोड करें!"
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(downloadedLessons, key = { it.lectureId }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Class ${item.classNumber}: ${language.pick(item.titleEn, item.titleHi)}",
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                            IconButton(onClick = { onRemoveOfflineLesson(item.lectureId) }) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Offline Lesson")
                            }
                        }
                        Text(
                            text = language.pick(item.cachedSummaryEn, item.cachedSummaryHi),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMiniCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
