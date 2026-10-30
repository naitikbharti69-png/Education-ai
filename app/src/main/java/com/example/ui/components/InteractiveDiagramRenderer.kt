package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import com.example.model.DiagramType
import com.example.model.DrawnStroke
import com.example.model.DrawnStrokePoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun InteractiveDiagramCanvas(
    diagramType: DiagramType,
    showBlueprint: Boolean,
    animationPhase: Float,
    strokes: List<DrawnStroke>,
    isDrawingEnabled: Boolean,
    activeColorHex: Long,
    activeStrokeWidth: Float,
    activeToolShape: String,
    onStrokeCompleted: (DrawnStroke) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPoints by remember { mutableStateOf<List<DrawnStrokePoint>>(emptyList()) }

    Box(
        modifier = modifier
            .background(Color(0xFF0F172A))
            .then(
                if (isDrawingEnabled) {
                    Modifier.pointerInput(activeColorHex, activeStrokeWidth, activeToolShape) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                if (size.width > 0 && size.height > 0) {
                                    currentPoints = listOf(
                                        DrawnStrokePoint(
                                            xRatio = (offset.x / size.width).coerceIn(0f, 1f),
                                            yRatio = (offset.y / size.height).coerceIn(0f, 1f)
                                        )
                                    )
                                }
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                if (size.width > 0 && size.height > 0) {
                                    val pt = DrawnStrokePoint(
                                        xRatio = (change.position.x / size.width).coerceIn(0f, 1f),
                                        yRatio = (change.position.y / size.height).coerceIn(0f, 1f)
                                    )
                                    currentPoints = currentPoints + pt
                                }
                            },
                            onDragEnd = {
                                if (currentPoints.size >= 2) {
                                    onStrokeCompleted(
                                        DrawnStroke(
                                            points = currentPoints,
                                            colorHex = activeColorHex,
                                            strokeWidth = activeStrokeWidth,
                                            shapeType = activeToolShape
                                        )
                                    )
                                }
                                currentPoints = emptyList()
                            },
                            onDragCancel = {
                                currentPoints = emptyList()
                            }
                        )
                    }
                } else {
                    Modifier
                }
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Subtle Academic Blueprint Grid
            val gridStep = min(w, h) / 10f
            val gridColor = Color(0xFF1E293B)
            var gx = 0f
            while (gx <= w) {
                drawLine(gridColor, Offset(gx, 0f), Offset(gx, h), strokeWidth = 1f)
                gx += gridStep
            }
            var gy = 0f
            while (gy <= h) {
                drawLine(gridColor, Offset(0f, gy), Offset(w, gy), strokeWidth = 1f)
                gy += gridStep
            }

            // 2. Concept Diagram Blueprint
            if (showBlueprint) {
                drawConceptBlueprint(diagramType, animationPhase)
            }

            // 3. Saved / Completed User Strokes
            strokes.forEach { stroke ->
                drawUserStroke(stroke, w, h)
            }

            // 4. Active In-Progress Stroke
            if (currentPoints.size >= 2) {
                drawUserStroke(
                    DrawnStroke(
                        points = currentPoints,
                        colorHex = activeColorHex,
                        strokeWidth = activeStrokeWidth,
                        shapeType = activeToolShape
                    ),
                    w,
                    h
                )
            }
        }
    }
}

private fun DrawScope.drawUserStroke(stroke: DrawnStroke, w: Float, h: Float) {
    if (stroke.points.isEmpty()) return
    val color = Color(stroke.colorHex.toULong())
    val first = Offset(stroke.points.first().xRatio * w, stroke.points.first().yRatio * h)
    val last = Offset(stroke.points.last().xRatio * w, stroke.points.last().yRatio * h)

    when (stroke.shapeType) {
        "CIRCLE" -> {
            val radius = (last - first).getDistance().coerceAtLeast(8f)
            drawCircle(
                color = color,
                radius = radius,
                center = first,
                style = Stroke(width = stroke.strokeWidth)
            )
        }
        "RECT" -> {
            val left = min(first.x, last.x)
            val top = min(first.y, last.y)
            val rw = kotlin.math.abs(last.x - first.x).coerceAtLeast(8f)
            val rh = kotlin.math.abs(last.y - first.y).coerceAtLeast(8f)
            drawRoundRect(
                color = color,
                topLeft = Offset(left, top),
                size = Size(rw, rh),
                cornerRadius = CornerRadius(8f, 8f),
                style = Stroke(width = stroke.strokeWidth)
            )
        }
        "ARROW" -> {
            drawArrowLine(first, last, color, stroke.strokeWidth)
        }
        else -> {
            val path = Path()
            path.moveTo(first.x, first.y)
            for (i in 1 until stroke.points.size) {
                val pt = stroke.points[i]
                path.lineTo(pt.xRatio * w, pt.yRatio * h)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = stroke.strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

private fun DrawScope.drawArrowLine(start: Offset, end: Offset, color: Color, strokeWidth: Float) {
    drawLine(color = color, start = start, end = end, strokeWidth = strokeWidth, cap = StrokeCap.Round)
    val angle = atan2((end.y - start.y).toDouble(), (end.x - start.x).toDouble())
    val headLen = 22f
    val angle1 = angle - Math.PI / 6
    val angle2 = angle + Math.PI / 6
    val p1 = Offset(
        (end.x - headLen * cos(angle1)).toFloat(),
        (end.y - headLen * sin(angle1)).toFloat()
    )
    val p2 = Offset(
        (end.x - headLen * cos(angle2)).toFloat(),
        (end.y - headLen * sin(angle2)).toFloat()
    )
    drawLine(color = color, start = end, end = p1, strokeWidth = strokeWidth, cap = StrokeCap.Round)
    drawLine(color = color, start = end, end = p2, strokeWidth = strokeWidth, cap = StrokeCap.Round)
}

private fun DrawScope.drawConceptBlueprint(type: DiagramType, phase: Float) {
    val w = size.width
    val h = size.height
    val cx = w / 2f
    val cy = h / 2f
    val cyan = Color(0xFF38BDF8)
    val gold = Color(0xFFFBBF24)
    val emerald = Color(0xFF34D399)
    val coral = Color(0xFFF87171)
    val violet = Color(0xFFA78BFA)

    when (type) {
        DiagramType.TRIGONOMETRY_CIRCLE -> {
            val r = min(w, h) * 0.34f
            // Axes
            drawLine(Color(0xFF64748B), Offset(cx - r * 1.3f, cy), Offset(cx + r * 1.3f, cy), 3f)
            drawLine(Color(0xFF64748B), Offset(cx, cy - r * 1.3f), Offset(cx, cy + r * 1.3f), 3f)
            // Unit Circle
            drawCircle(cyan, radius = r, center = Offset(cx, cy), style = Stroke(width = 4f))
            // Animated Angle Theta
            val theta = (Math.PI / 4.0) + sin(phase * Math.PI * 2) * (Math.PI / 10.0)
            val px = cx + (r * cos(theta)).toFloat()
            val py = cy - (r * sin(theta)).toFloat()
            // Base, Perpendicular, Hypotenuse
            drawLine(emerald, Offset(cx, cy), Offset(px, cy), strokeWidth = 6f)
            drawLine(coral, Offset(px, cy), Offset(px, py), strokeWidth = 6f)
            drawLine(gold, Offset(cx, cy), Offset(px, py), strokeWidth = 6f)
            drawCircle(Color.White, radius = 9f, center = Offset(px, py))
        }

        DiagramType.RAY_OPTICS -> {
            val r = min(w, h) * 0.35f
            // Principal Axis
            drawLine(Color(0xFF94A3B8), Offset(w * 0.08f, cy), Offset(w * 0.92f, cy), 3f)
            // Convex Lens at center
            drawOval(
                color = cyan.copy(alpha = 0.25f),
                topLeft = Offset(cx - 18f, cy - r),
                size = Size(36f, r * 2f)
            )
            drawOval(
                color = cyan,
                topLeft = Offset(cx - 18f, cy - r),
                size = Size(36f, r * 2f),
                style = Stroke(width = 4f)
            )
            // Object arrow on left
            val objX = w * 0.22f
            val objTopY = cy - r * 0.65f
            drawArrowLine(Offset(objX, cy), Offset(objX, objTopY), emerald, 6f)
            // Image arrow on right (inverted)
            val imgX = w * 0.78f
            val imgBottomY = cy + r * 0.65f
            drawArrowLine(Offset(imgX, cy), Offset(imgX, imgBottomY), coral, 6f)
            // Ray 1: Parallel then through Focus F2
            drawLine(gold, Offset(objX, objTopY), Offset(cx, objTopY), 4f)
            drawLine(gold, Offset(cx, objTopY), Offset(imgX, imgBottomY), 4f)
            // Ray 2: Through Optical Center O
            drawLine(violet, Offset(objX, objTopY), Offset(imgX, imgBottomY), 4f)
            // Focal points F1 & F2
            drawCircle(Color.White, 7f, Offset(cx - (cx - objX) * 0.5f, cy))
            drawCircle(Color.White, 7f, Offset(cx + (imgX - cx) * 0.5f, cy))
        }

        DiagramType.HUMAN_HEART -> {
            val r = min(w, h) * 0.32f
            // Four-chamber stylized heart schematic
            val path = Path().apply {
                moveTo(cx, cy + r)
                cubicTo(cx - r * 1.2f, cy + r * 0.2f, cx - r * 1.1f, cy - r * 0.8f, cx, cy - r * 0.35f)
                cubicTo(cx + r * 1.1f, cy - r * 0.8f, cx + r * 1.2f, cy + r * 0.2f, cx, cy + r)
                close()
            }
            drawPath(path, color = coral.copy(alpha = 0.2f))
            drawPath(path, color = coral, style = Stroke(width = 5f))
            // Septum dividing Left (O2 - Red) and Right (CO2 - Blue)
            drawLine(Color.White, Offset(cx, cy - r * 0.35f), Offset(cx, cy + r * 0.85f), 4f)
            drawLine(Color.White.copy(alpha = 0.7f), Offset(cx - r * 0.7f, cy + r * 0.1f), Offset(cx + r * 0.7f, cy + r * 0.1f), 3f)
            // Animated circulation arrows
            val pulse = 8f + sin(phase * Math.PI * 2).toFloat() * 4f
            drawCircle(cyan, radius = pulse, center = Offset(cx - r * 0.38f, cy - r * 0.1f))
            drawCircle(cyan, radius = pulse, center = Offset(cx - r * 0.35f, cy + r * 0.38f))
            drawCircle(coral, radius = pulse, center = Offset(cx + r * 0.38f, cy - r * 0.1f))
            drawCircle(coral, radius = pulse, center = Offset(cx + r * 0.35f, cy + r * 0.38f))
            // Aorta arch at top
            drawArc(
                color = gold,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(cx - r * 0.3f, cy - r * 0.9f),
                size = Size(r * 0.6f, r * 0.6f),
                style = Stroke(width = 6f)
            )
        }

        DiagramType.SOLAR_SYSTEM -> {
            val maxR = min(w, h) * 0.42f
            // Sun at center
            drawCircle(gold.copy(alpha = 0.3f), radius = maxR * 0.22f, center = Offset(cx, cy))
            drawCircle(gold, radius = maxR * 0.15f, center = Offset(cx, cy))
            val orbitRadii = listOf(0.32f, 0.50f, 0.70f, 0.90f)
            val planetColors = listOf(Color(0xFFCBD5E1), cyan, coral, gold)
            orbitRadii.forEachIndexed { idx, ratio ->
                val or = maxR * ratio
                drawCircle(
                    color = Color.White.copy(alpha = 0.22f),
                    radius = or,
                    center = Offset(cx, cy),
                    style = Stroke(width = 2f)
                )
                val angle = (phase * 2 * Math.PI * (1.2 - idx * 0.2)) + idx * 1.3
                val px = cx + (or * cos(angle)).toFloat()
                val py = cy + (or * sin(angle)).toFloat()
                drawCircle(planetColors[idx], radius = 10f + idx * 2f, center = Offset(px, py))
            }
        }

        DiagramType.PHOTOSYNTHESIS -> {
            val r = min(w, h) * 0.32f
            // Sun in top-left
            val sunCenter = Offset(w * 0.18f, h * 0.22f)
            drawCircle(gold, radius = r * 0.28f, center = sunCenter)
            // Green Leaf in center
            val leafPath = Path().apply {
                moveTo(cx - r, cy + r * 0.2f)
                quadraticTo(cx, cy - r * 0.9f, cx + r, cy - r * 0.1f)
                quadraticTo(cx, cy + r * 0.9f, cx - r, cy + r * 0.2f)
                close()
            }
            drawPath(leafPath, emerald.copy(alpha = 0.3f))
            drawPath(leafPath, emerald, style = Stroke(width = 5f))
            drawLine(emerald, Offset(cx - r, cy + r * 0.2f), Offset(cx + r, cy - r * 0.1f), 4f)
            // Sunlight ray arrow
            drawArrowLine(sunCenter, Offset(cx - r * 0.2f, cy - r * 0.2f), gold, 4f)
            // Water from roots arrow (bottom)
            drawArrowLine(Offset(cx, h * 0.88f), Offset(cx, cy + r * 0.35f), cyan, 5f)
            // Oxygen output arrow (right)
            drawArrowLine(Offset(cx + r * 0.4f, cy - r * 0.2f), Offset(w * 0.86f, h * 0.25f), coral, 5f)
        }

        DiagramType.WATER_CYCLE -> {
            // Water body at bottom
            drawRoundRect(
                color = cyan.copy(alpha = 0.35f),
                topLeft = Offset(w * 0.08f, h * 0.72f),
                size = Size(w * 0.84f, h * 0.18f),
                cornerRadius = CornerRadius(16f, 16f)
            )
            // Sun top-left
            drawCircle(gold, radius = min(w, h) * 0.1f, center = Offset(w * 0.2f, h * 0.2f))
            // Evaporation upward arrows (left)
            drawArrowLine(Offset(w * 0.3f, h * 0.68f), Offset(w * 0.3f, h * 0.34f), gold, 5f)
            drawArrowLine(Offset(w * 0.4f, h * 0.68f), Offset(w * 0.4f, h * 0.34f), gold, 5f)
            // Cloud top-right
            drawRoundRect(
                color = Color.White.copy(alpha = 0.85f),
                topLeft = Offset(w * 0.55f, h * 0.16f),
                size = Size(w * 0.32f, h * 0.16f),
                cornerRadius = CornerRadius(30f, 30f)
            )
            // Rain drops downward arrows (right)
            drawArrowLine(Offset(w * 0.65f, h * 0.36f), Offset(w * 0.65f, h * 0.68f), cyan, 5f)
            drawArrowLine(Offset(w * 0.76f, h * 0.36f), Offset(w * 0.76f, h * 0.68f), cyan, 5f)
        }

        DiagramType.NUMBER_LINE -> {
            val lineY = cy + 20f
            val startX = w * 0.1f
            val endX = w * 0.9f
            drawLine(Color.White, Offset(startX, lineY), Offset(endX, lineY), 5f)
            val steps = 10
            val stepW = (endX - startX) / steps
            for (i in 0..steps) {
                val tx = startX + i * stepW
                drawLine(cyan, Offset(tx, lineY - 14f), Offset(tx, lineY + 14f), 4f)
            }
            // Jump arcs from 3 to 5 (+2)
            for (j in 3 until 5) {
                val x1 = startX + j * stepW
                drawArc(
                    color = gold,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(x1, lineY - stepW * 0.9f),
                    size = Size(stepW, stepW * 1.4f),
                    style = Stroke(width = 5f)
                )
            }
        }

        DiagramType.FRACTION_PIE -> {
            val r = min(w, h) * 0.33f
            // Full circle divided into 4 quarters, 1 shaded gold, 2 shaded emerald
            drawArc(
                color = gold.copy(alpha = 0.75f),
                startAngle = -90f,
                sweepAngle = 90f,
                useCenter = true,
                topLeft = Offset(cx - r, cy - r),
                size = Size(r * 2, r * 2)
            )
            drawArc(
                color = cyan.copy(alpha = 0.45f),
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(cx - r, cy - r),
                size = Size(r * 2, r * 2)
            )
            drawCircle(Color.White, radius = r, center = Offset(cx, cy), style = Stroke(width = 4f))
            drawLine(Color.White, Offset(cx - r, cy), Offset(cx + r, cy), 3f)
            drawLine(Color.White, Offset(cx, cy - r), Offset(cx, cy + r), 3f)
        }

        DiagramType.ELECTRIC_CIRCUIT -> {
            val left = w * 0.18f
            val right = w * 0.82f
            val top = h * 0.22f
            val bottom = h * 0.78f
            // Circuit loop
            drawRoundRect(
                color = cyan,
                topLeft = Offset(left, top),
                size = Size(right - left, bottom - top),
                cornerRadius = CornerRadius(16f, 16f),
                style = Stroke(width = 5f)
            )
            // Glowing Bulb at top center
            drawCircle(gold.copy(alpha = 0.35f), radius = 36f, center = Offset(cx, top))
            drawCircle(gold, radius = 20f, center = Offset(cx, top))
            // Battery at bottom center
            drawLine(coral, Offset(cx - 12f, bottom - 22f), Offset(cx - 12f, bottom + 22f), 6f)
            drawLine(emerald, Offset(cx + 12f, bottom - 12f), Offset(cx + 12f, bottom + 12f), 6f)
        }

        DiagramType.CELL_STRUCTURE -> {
            val r = min(w, h) * 0.35f
            // Outer Cell Wall / Membrane
            drawRoundRect(
                color = emerald.copy(alpha = 0.22f),
                topLeft = Offset(cx - r * 1.2f, cy - r * 0.9f),
                size = Size(r * 2.4f, r * 1.8f),
                cornerRadius = CornerRadius(42f, 42f)
            )
            drawRoundRect(
                color = emerald,
                topLeft = Offset(cx - r * 1.2f, cy - r * 0.9f),
                size = Size(r * 2.4f, r * 1.8f),
                cornerRadius = CornerRadius(42f, 42f),
                style = Stroke(width = 5f)
            )
            // Nucleus
            drawCircle(violet.copy(alpha = 0.5f), radius = r * 0.35f, center = Offset(cx - r * 0.3f, cy))
            drawCircle(violet, radius = r * 0.35f, center = Offset(cx - r * 0.3f, cy), style = Stroke(width = 4f))
            // Mitochondria
            drawOval(coral, topLeft = Offset(cx + r * 0.25f, cy - r * 0.45f), size = Size(r * 0.5f, r * 0.28f))
            // Vacuole
            drawOval(cyan.copy(alpha = 0.5f), topLeft = Offset(cx + r * 0.15f, cy + r * 0.05f), size = Size(r * 0.65f, r * 0.45f))
        }

        DiagramType.PARABOLA_GRAPH -> {
            val pad = min(w, h) * 0.16f
            // Coordinate Axes
            drawArrowLine(Offset(pad, h - pad), Offset(pad, pad), Color.White, 4f)
            drawArrowLine(Offset(pad, h - pad), Offset(w - pad, h - pad), Color.White, 4f)
            // Velocity-Time / Curve line
            drawLine(emerald, Offset(pad, h - pad - 30f), Offset(w - pad - 20f, pad + 30f), 6f)
            // Dashed projection lines
            val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
            drawLine(
                gold,
                Offset(w - pad - 20f, pad + 30f),
                Offset(w - pad - 20f, h - pad),
                strokeWidth = 3f,
                pathEffect = dash
            )
        }

        DiagramType.DEMAND_SUPPLY -> {
            val pad = min(w, h) * 0.16f
            // Price (Y) and Quantity (X) Axes
            drawArrowLine(Offset(pad, h - pad), Offset(pad, pad), Color.White, 4f)
            drawArrowLine(Offset(pad, h - pad), Offset(w - pad, h - pad), Color.White, 4f)
            // Downward Demand Curve (DD)
            drawLine(coral, Offset(pad + 30f, pad + 30f), Offset(w - pad - 30f, h - pad - 30f), 6f)
            // Upward Supply Curve (SS)
            drawLine(emerald, Offset(pad + 30f, h - pad - 30f), Offset(w - pad - 30f, pad + 30f), 6f)
            // Equilibrium Point E at center
            drawCircle(gold, radius = 12f, center = Offset(cx, cy))
        }
    }
}
