package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.local.SavedDiagramNoteEntity
import com.example.model.AppLanguage
import com.example.model.DiagramType
import com.example.model.DrawnStroke
import com.example.model.pick
import com.example.ui.components.InteractiveDiagramCanvas
import com.example.viewmodel.AiTaskState

@Composable
fun DiagramStudioScreen(
    language: AppLanguage,
    activeDiagramType: DiagramType,
    canvasStrokes: List<DrawnStroke>,
    savedNotes: List<SavedDiagramNoteEntity>,
    aiDiagramStepsState: AiTaskState<List<String>>,
    onSelectDiagramType: (DiagramType) -> Unit,
    onAddStroke: (DrawnStroke) -> Unit,
    onUndoStroke: () -> Unit,
    onClearCanvas: () -> Unit,
    onSaveDiagramNote: (String, String) -> Unit,
    onLoadSavedNote: (SavedDiagramNoteEntity) -> Unit,
    onDeleteSavedNote: (Int) -> Unit,
    onAskAiDiagramCoach: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBlueprint by remember { mutableStateOf(true) }
    var activeColorHex by remember { mutableLongStateOf(0xFFFBBF24L) } // Saffron Gold
    var activeStrokeWidth by remember { mutableFloatStateOf(7f) }
    var activeToolShape by remember { mutableStateOf("FREEHAND") }

    var customNoteTitle by remember(activeDiagramType) {
        mutableStateOf(activeDiagramType.titleEn)
    }
    var customNoteBody by remember { mutableStateOf("") }
    var aiDiagramPrompt by remember(activeDiagramType) {
        mutableStateOf(activeDiagramType.titleEn)
    }

    val palette = listOf(
        0xFFFBBF24L to "Gold",
        0xFF38BDF8L to "Cyan",
        0xFF34D399L to "Emerald",
        0xFFF87171L to "Coral",
        0xFFA78BFAL to "Violet",
        0xFFFFFFFFL to "White"
    )

    val toolShapes = listOf(
        "FREEHAND" to language.pick("Pen ✏️", "पेन ✏️"),
        "ARROW" to language.pick("Arrow ↗", "तीर ↗"),
        "CIRCLE" to language.pick("Circle ◯", "वृत्त ◯"),
        "RECT" to language.pick("Box ▭", "बॉक्स ▭")
    )

    val infiniteTransition = rememberInfiniteTransition(label = "studioAnim")
    val animPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "studioPhase"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("diagram_studio_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header & Blueprint Template Selector
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = language.pick(
                        "Interactive Diagram Studio & Visual Notes",
                        "इंटरैक्टिव चित्र (Diagram) स्टूडियो और स्मार्ट नोट्स"
                    ),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = language.pick(
                        "Select any Class 1–12 Science, Math, or Economics diagram blueprint, practice drawing/labeling directly on the canvas, and get AI drawing steps!",
                        "कक्षा 1–12 के किसी भी विज्ञान, गणित या अर्थशास्त्र के आरेख को चुनें, कैनवास पर स्वयं चित्र बनाएं और AI से चित्र बनाने के चरण सीखें!"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.testTag("diagram_type_selector")
                ) {
                    items(DiagramType.entries) { dType ->
                        FilterChip(
                            selected = activeDiagramType == dType,
                            onClick = { onSelectDiagramType(dType) },
                            label = {
                                Text(language.pick(dType.titleEn, dType.titleHi))
                            }
                        )
                    }
                }
            }
        }

        // 2. Interactive Drawing Canvas Card
        item {
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Top Canvas Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E293B))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📐 " + language.pick(activeDiagramType.titleEn, activeDiagramType.titleHi),
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        AssistChip(
                            onClick = { showBlueprint = !showBlueprint },
                            label = {
                                Text(
                                    text = if (showBlueprint) {
                                        language.pick("Hide Guide", "गाइड छुपाएं")
                                    } else {
                                        language.pick("Show Guide", "गाइड दिखाएं")
                                    },
                                    color = Color.White
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (showBlueprint) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Blueprint",
                                    tint = Color(0xFFFBBF24),
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            modifier = Modifier.testTag("toggle_blueprint_button")
                        )
                    }

                    // Interactive Drawing Surface
                    InteractiveDiagramCanvas(
                        diagramType = activeDiagramType,
                        showBlueprint = showBlueprint,
                        animationPhase = animPhase,
                        strokes = canvasStrokes,
                        isDrawingEnabled = true,
                        activeColorHex = activeColorHex,
                        activeStrokeWidth = activeStrokeWidth,
                        activeToolShape = activeToolShape,
                        onStrokeCompleted = onAddStroke,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(290.dp)
                            .testTag("interactive_drawing_canvas")
                    )

                    // Drawing Tools & Color Palette Footer
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Shape Mode Chips + Undo / Clear
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(toolShapes) { (shapeKey, label) ->
                                    FilterChip(
                                        selected = activeToolShape == shapeKey,
                                        onClick = { activeToolShape = shapeKey },
                                        label = { Text(label) }
                                    )
                                }
                            }

                            Row {
                                IconButton(
                                    onClick = onUndoStroke,
                                    modifier = Modifier.testTag("canvas_undo_button")
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Undo Stroke")
                                }
                                IconButton(
                                    onClick = onClearCanvas,
                                    modifier = Modifier.testTag("canvas_clear_button")
                                ) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Clear Canvas")
                                }
                            }
                        }

                        // Color Swatches & Brush Thickness
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            palette.forEach { (hex, name) ->
                                val isSelected = activeColorHex == hex
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(hex.toULong()))
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                            shape = CircleShape
                                        )
                                        .clickable { activeColorHex = hex }
                                        .testTag("color_swatch_$name")
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Slider(
                                value = activeStrokeWidth,
                                onValueChange = { activeStrokeWidth = it },
                                valueRange = 3f..18f,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // 3. AI Diagram Drawing Coach Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "🤖 " + language.pick(
                            "AI Diagram Drawing & Labeling Coach",
                            "AI चित्र निर्माण और नामांकन (Labeling) कोच"
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                    OutlinedTextField(
                        value = aiDiagramPrompt,
                        onValueChange = { aiDiagramPrompt = it },
                        label = {
                            Text(
                                language.pick(
                                    "Enter Diagram Topic (e.g. Human Heart, Ray Optics, Nephron)",
                                    "चित्र का विषय लिखें (जैसे: मानव हृदय, उत्तल लेंस, कोशिका)"
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_diagram_topic_input"),
                        singleLine = true
                    )
                    Button(
                        onClick = { onAskAiDiagramCoach(aiDiagramPrompt) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ask_ai_diagram_coach_button")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            language.pick(
                                "Generate Step-by-Step Drawing Instructions (+15 XP)",
                                "चित्र बनाने के चरण (Steps) जनरेट करें (+15 XP)"
                            )
                        )
                    }

                    when (aiDiagramStepsState) {
                        is AiTaskState.Loading -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(language.pick("Generating drawing steps...", "चित्र बनाने के चरण तैयार हो रहे हैं..."))
                            }
                        }
                        is AiTaskState.Error -> {
                            Text(
                                text = aiDiagramStepsState.message,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        is AiTaskState.Success -> {
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    aiDiagramStepsState.data.forEach { step ->
                                        Text(text = step, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                        AiTaskState.Idle -> {}
                    }
                }
            }
        }

        // 4. Save Diagram + Revision Note to Offline Room Database
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "💾 " + language.pick(
                            "Save Drawn Diagram & Revision Note (Offline Storage)",
                            "बनाया गया चित्र और नोट्स सेव करें (ऑफलाइन स्टोरेज)"
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                    OutlinedTextField(
                        value = customNoteTitle,
                        onValueChange = { customNoteTitle = it },
                        label = { Text(language.pick("Diagram / Note Title", "शीर्षक (Title)")) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = customNoteBody,
                        onValueChange = { customNoteBody = it },
                        label = {
                            Text(
                                language.pick(
                                    "Add key points, labels, or formulas for this diagram...",
                                    "इस चित्र के मुख्य बिंदु या सूत्र यहाँ लिखें..."
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("diagram_note_input"),
                        minLines = 2
                    )
                    Button(
                        onClick = {
                            onSaveDiagramNote(
                                customNoteTitle,
                                customNoteBody.ifBlank {
                                    "Saved ${activeDiagramType.titleEn} (${activeDiagramType.titleHi}) with ${canvasStrokes.size} custom strokes."
                                }
                            )
                            customNoteBody = ""
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_diagram_note_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            language.pick(
                                "Save Diagram & Note (+30 XP)",
                                "चित्र और नोट सेव करें (+30 XP)"
                            )
                        )
                    }
                }
            }
        }

        // 5. Saved Diagrams & Notes List
        if (savedNotes.isNotEmpty()) {
            item {
                Text(
                    text = language.pick(
                        "Saved Diagrams & Notes (${savedNotes.size})",
                        "सेव किए गए चित्र और नोट्स (${savedNotes.size})"
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(savedNotes, key = { it.id }) { saved ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLoadSavedNote(saved) },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${saved.title} (Class ${saved.classNumber})",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = saved.noteContent,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        OutlinedButton(onClick = { onLoadSavedNote(saved) }) {
                            Text(language.pick("Load", "खोलें"))
                        }
                        IconButton(onClick = { onDeleteSavedNote(saved.id) }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Saved Note")
                        }
                    }
                }
            }
        }
    }
}
