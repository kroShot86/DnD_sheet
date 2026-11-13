import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.dnd_sheet.NeonBlue
import com.example.dnd_sheet.GoldGlow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path

@Composable
fun MagicPortalIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "magic_portal")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.size(28.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2
        val glowWidth = 2.dp.toPx()

        rotate(rotation) {
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        NeonBlue,
                        Color.Transparent,
                        GoldGlow,
                        Color.Transparent,
                        NeonBlue
                    )
                ),
                radius = radius - glowWidth,
                center = center,
                style = Stroke(width = glowWidth)
            )

            val runeCount = 8
            repeat(runeCount) { i ->
                val angle = (360f / runeCount) * i
                rotate(angle, pivot = center) {
                    drawLine(
                        color = NeonBlue.copy(alpha = pulse),
                        start = Offset(center.x, center.y - radius + 6.dp.toPx()),
                        end = Offset(center.x, center.y - radius + 2.dp.toPx()),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
        }

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White,
                    NeonBlue.copy(alpha = pulse),
                    GoldGlow.copy(alpha = 0.5f),
                    Color.Transparent
                ),
                center = center,
                radius = radius * 0.6f
            ),
            radius = radius * 0.6f,
            center = center
        )

        val sparkCount = 4
        repeat(sparkCount) { i ->
            val angle = (rotation + (i * 90f)) * (Math.PI / 180).toFloat()
            val distance = radius * (0.7f + 0.3f * pulse)
            val sparkX = center.x + kotlin.math.cos(angle) * distance
            val sparkY = center.y + kotlin.math.sin(angle) * distance

            drawCircle(
                color = GoldGlow.copy(alpha = 0.8f),
                radius = 1.5.dp.toPx(),
                center = Offset(sparkX, sparkY)
            )
        }
    }
}

@Composable
fun SkillsTargetIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "skills_target")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(modifier = modifier.size(32.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        drawCircle(
            color = GoldGlow.copy(alpha = 0.6f),
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        drawCircle(
            color = NeonBlue.copy(alpha = 0.8f),
            radius = radius * 0.6f * scale,
            center = center,
            style = Stroke(width = 1.5.dp.toPx())
        )

        drawCircle(
            color = GoldGlow,
            radius = 2.dp.toPx(),
            center = center
        )

        rotate(rotation) {
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color.Transparent,
                        NeonBlue.copy(alpha = 0.8f),
                        Color.Transparent
                    )
                ),
                startAngle = 0f,
                sweepAngle = 90f,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
            )
        }

        val crossSize = 6.dp.toPx()
        drawLine(color = GoldGlow.copy(alpha = 0.5f), start = Offset(center.x - crossSize, center.y), end = Offset(center.x + crossSize, center.y), strokeWidth = 1.dp.toPx())
        drawLine(color = GoldGlow.copy(alpha = 0.5f), start = Offset(center.x, center.y - crossSize), end = Offset(center.x, center.y + crossSize), strokeWidth = 1.dp.toPx())
    }
}

@Composable
fun HistoryScrollIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "history_scroll")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Canvas(modifier = modifier.size(32.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        val scrollWidth = radius * 0.8f
        val scrollHeight = radius * 1.6f

        drawRoundRect(
            color = Color.Black.copy(alpha = 0.5f),
            topLeft = Offset(center.x - scrollWidth/2 + 2.dp.toPx(), center.y - scrollHeight/2 + 2.dp.toPx()),
            size = androidx.compose.ui.geometry.Size(scrollWidth, scrollHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        drawRoundRect(
            color = Color(0xFFF5E6D3).copy(alpha = 0.8f),
            topLeft = Offset(center.x - scrollWidth/2, center.y - scrollHeight/2),
            size = androidx.compose.ui.geometry.Size(scrollWidth, scrollHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        drawRoundRect(
            color = Color(0xFF4E342E),
            topLeft = Offset(center.x - scrollWidth/2 - 2.dp.toPx(), center.y - scrollHeight/2 - 2.dp.toPx()),
            size = androidx.compose.ui.geometry.Size(scrollWidth + 4.dp.toPx(), 4.dp.toPx()),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFF4E342E),
            topLeft = Offset(center.x - scrollWidth/2 - 2.dp.toPx(), center.y + scrollHeight/2 - 2.dp.toPx()),
            size = androidx.compose.ui.geometry.Size(scrollWidth + 4.dp.toPx(), 4.dp.toPx()),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )

        val runeColor = NeonBlue.copy(alpha = glowAlpha)
        val runeSize = 2.dp.toPx()

        drawLine(runeColor, Offset(center.x - scrollWidth/2 + 4.dp.toPx(), center.y - 8.dp.toPx()), Offset(center.x + scrollWidth/2 - 4.dp.toPx(), center.y - 8.dp.toPx()), strokeWidth = runeSize)
        drawLine(runeColor, Offset(center.x - scrollWidth/2 + 4.dp.toPx(), center.y - 2.dp.toPx()), Offset(center.x + scrollWidth/2 - 6.dp.toPx(), center.y - 2.dp.toPx()), strokeWidth = runeSize)
        drawLine(runeColor, Offset(center.x - scrollWidth/2 + 4.dp.toPx(), center.y + 4.dp.toPx()), Offset(center.x + scrollWidth/2 - 4.dp.toPx(), center.y + 4.dp.toPx()), strokeWidth = runeSize)
        drawLine(runeColor, Offset(center.x - scrollWidth/2 + 4.dp.toPx(), center.y + 10.dp.toPx()), Offset(center.x + scrollWidth/2 - 8.dp.toPx(), center.y + 10.dp.toPx()), strokeWidth = runeSize)
    }
}

@Composable
fun HistoryBookIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "history_book")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val pageFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pageFloat"
    )

    Canvas(modifier = modifier.size(32.dp)) {
        val center = Offset(size.width / 2, size.height / 2)

        val bookWidth = size.width * 0.65f
        val bookHeight = size.height * 0.75f
        val bookLeft = center.x - bookWidth / 2
        val bookTop = center.y - bookHeight / 2 + 2.dp.toPx()

        drawRoundRect(
            color = Color.Black.copy(alpha = 0.6f),
            topLeft = Offset(bookLeft + 2.dp.toPx(), bookTop + 2.dp.toPx()),
            size = Size(bookWidth, bookHeight),
            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
        )

        drawRoundRect(
            color = Color(0xFF3E1A0A),
            topLeft = Offset(bookLeft, bookTop),
            size = Size(bookWidth, bookHeight),
            cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
        )

        drawRect(
            color = GoldGlow.copy(alpha = 0.8f + glowAlpha * 0.2f),
            topLeft = Offset(bookLeft, bookTop),
            size = Size(bookWidth * 0.2f, bookHeight)
        )

        drawCircle(color = GoldGlow, radius = 1.dp.toPx(), center = Offset(bookLeft + bookWidth * 0.1f, bookTop + bookHeight * 0.2f))
        drawCircle(color = GoldGlow, radius = 1.dp.toPx(), center = Offset(bookLeft + bookWidth * 0.1f, bookTop + bookHeight * 0.8f))

        drawRoundRect(
            color = NeonBlue.copy(alpha = glowAlpha * 0.8f),
            topLeft = Offset(bookLeft + bookWidth * 0.2f, bookTop + 2.dp.toPx()),
            size = Size(bookWidth * 0.8f - 2.dp.toPx(), bookHeight - 4.dp.toPx()),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
        )

        val textStartX = bookLeft + bookWidth * 0.3f
        val textEndX = bookLeft + bookWidth * 0.85f
        val textYStart = bookTop + bookHeight * 0.3f
        val lineSpacing = bookHeight * 0.15f

        drawLine(GoldGlow.copy(alpha = 0.6f), Offset(textStartX, textYStart), Offset(textEndX, textYStart), strokeWidth = 1.dp.toPx())
        drawLine(GoldGlow.copy(alpha = 0.6f), Offset(textStartX, textYStart + lineSpacing), Offset(textEndX - 3.dp.toPx(), textYStart + lineSpacing), strokeWidth = 1.dp.toPx())
        drawLine(GoldGlow.copy(alpha = 0.6f), Offset(textStartX, textYStart + lineSpacing * 2), Offset(textEndX, textYStart + lineSpacing * 2), strokeWidth = 1.dp.toPx())

        if (pageFloat < 0.8f) {
            val floatY = bookTop - (pageFloat * 15.dp.toPx())
            val pageAlpha = 1f - (pageFloat / 0.8f)
            val pageWidth = bookWidth * 0.4f
            val pageHeight = bookHeight * 0.3f

            drawRoundRect(
                color = Color(0xFFF5E6D3).copy(alpha = pageAlpha),
                topLeft = Offset(center.x, floatY),
                size = Size(pageWidth, pageHeight),
                cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )
            drawLine(
                color = GoldGlow.copy(alpha = pageAlpha * 0.8f),
                start = Offset(center.x + 2.dp.toPx(), floatY + 2.dp.toPx()),
                end = Offset(center.x + pageWidth - 2.dp.toPx(), floatY + 2.dp.toPx()),
                strokeWidth = 0.5.dp.toPx()
            )
        }
    }
}

@Composable
fun InventoryPotionIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "inventory_potion")

    val bubbleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bubbleProgress"
    )

    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    Canvas(modifier = modifier.size(32.dp)) {
        val center = Offset(size.width / 2, size.height / 2)

        val flaskWidth = size.width * 0.55f
        val flaskHeight = size.height * 0.65f
        val flaskLeft = center.x - flaskWidth / 2
        val flaskTop = center.y - flaskHeight / 2 + 3.dp.toPx()

        val neckWidth = flaskWidth * 0.4f
        val neckHeight = flaskHeight * 0.25f
        drawRect(
            color = Color(0xFF88B0C0).copy(alpha = 0.6f),
            topLeft = Offset(center.x - neckWidth / 2, flaskTop - neckHeight),
            size = Size(neckWidth, neckHeight)
        )

        drawRoundRect(
            color = GoldGlow,
            topLeft = Offset(center.x - neckWidth / 2 - 1.dp.toPx(), flaskTop - neckHeight - 2.dp.toPx()),
            size = Size(neckWidth + 2.dp.toPx(), 3.dp.toPx()),
            cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
        )

        drawCircle(
            color = Color(0xFF88B0C0).copy(alpha = 0.5f),
            radius = flaskWidth / 2,
            center = Offset(center.x, center.y + flaskHeight * 0.2f)
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.6f),
            radius = flaskWidth / 2,
            center = Offset(center.x, center.y + flaskHeight * 0.2f),
            style = Stroke(width = 1.dp.toPx())
        )

        val liquidRadius = (flaskWidth / 2) * 0.85f
        val liquidCenter = Offset(center.x, center.y + flaskHeight * 0.25f)

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = glowPulse * 0.8f),
                    NeonBlue.copy(alpha = glowPulse),
                    Color(0xFF006080).copy(alpha = 0.9f)
                ),
                center = liquidCenter,
                radius = liquidRadius
            ),
            radius = liquidRadius,
            center = liquidCenter
        )

        val bubbleCount = 3
        repeat(bubbleCount) { i ->
            val offsetProgress = (bubbleProgress + i / bubbleCount.toFloat()) % 1f
            val bubbleX = liquidCenter.x + (kotlin.math.sin(offsetProgress * 3f) * liquidRadius * 0.5f)
            val bubbleY = liquidCenter.y + liquidRadius - (offsetProgress * liquidRadius * 1.8f)
            val bubbleSize = (1.5f - offsetProgress * 0.8f).dp.toPx()
            val bubbleAlpha = (1f - offsetProgress * 0.7f)

            if (bubbleAlpha > 0) {
                drawCircle(
                    color = Color.White.copy(alpha = bubbleAlpha),
                    radius = bubbleSize.coerceAtLeast(0.5.dp.toPx()),
                    center = Offset(bubbleX, bubbleY)
                )
            }
        }

        drawLine(
            color = Color.White.copy(alpha = 0.8f),
            start = Offset(center.x - flaskWidth * 0.25f, center.y + flaskHeight * 0.1f),
            end = Offset(center.x - flaskWidth * 0.15f, center.y + flaskHeight * 0.2f),
            strokeWidth = 1.5.dp.toPx()
        )
    }
}

@Composable
fun FeaturesCrystalIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "features_crystal")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "crystal_rotation"
    )

    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crystal_glow"
    )

    Canvas(modifier = modifier.size(32.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF9D00FF).copy(alpha = glowPulse * 0.5f),
                    Color.Transparent
                ),
                center = center,
                radius = radius
            ),
            radius = radius,
            center = center
        )

        rotate(rotation) {
            val crystalSize = radius * 0.7f

            val crystalPath = Path().apply {
                moveTo(center.x, center.y - crystalSize)
                lineTo(center.x + crystalSize * 0.6f, center.y)
                lineTo(center.x, center.y + crystalSize)
                lineTo(center.x - crystalSize * 0.6f, center.y)
                close()
            }

            drawPath(
                path = crystalPath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonBlue.copy(alpha = glowPulse),
                        Color(0xFF9D00FF).copy(alpha = glowPulse),
                        NeonBlue.copy(alpha = glowPulse * 0.5f)
                    ),
                    start = Offset(center.x - crystalSize, center.y - crystalSize),
                    end = Offset(center.x + crystalSize, center.y + crystalSize)
                )
            )

            drawPath(
                path = crystalPath,
                color = GoldGlow.copy(alpha = 0.8f),
                style = Stroke(width = 1.dp.toPx())
            )

            drawLine(
                color = Color.White.copy(alpha = 0.4f),
                start = Offset(center.x, center.y - crystalSize),
                end = Offset(center.x, center.y + crystalSize),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(center.x - crystalSize * 0.6f, center.y),
                end = Offset(center.x + crystalSize * 0.6f, center.y),
                strokeWidth = 1.dp.toPx()
            )
        }

        val sparkCount = 3
        repeat(sparkCount) { i ->
            val angle = (rotation * 1.5f + (i * 120f)) * (Math.PI / 180).toFloat()
            val distance = radius * 0.9f
            val sparkX = center.x + kotlin.math.cos(angle) * distance
            val sparkY = center.y + kotlin.math.sin(angle) * distance

            drawCircle(
                color = GoldGlow.copy(alpha = glowPulse),
                radius = 1.2.dp.toPx(),
                center = Offset(sparkX, sparkY)
            )
        }
    }
}

@Composable
fun CharactersListIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "characters_list")

    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Canvas(modifier = modifier.size(32.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val width = size.width * 0.7f
        val height = size.height * 0.8f

        drawRoundRect(
            color = Color(0xFF2A1B1B).copy(alpha = 0.8f),
            topLeft = Offset(center.x - width/2, center.y - height/2),
            size = androidx.compose.ui.geometry.Size(width, height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        drawRoundRect(
            color = NeonBlue.copy(alpha = 0.6f),
            topLeft = Offset(center.x - width/2, center.y - height/2),
            size = androidx.compose.ui.geometry.Size(width, height),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx()),
            style = Stroke(width = 1.dp.toPx())
        )

        val lineStartX = center.x - width/2 + 4.dp.toPx()
        val lineEndX = center.x + width/2 - 4.dp.toPx()
        val startY = center.y - height/2 + 8.dp.toPx()
        val spacing = height * 0.25f

        repeat(3) { i ->
            drawLine(
                color = GoldGlow.copy(alpha = 0.7f),
                start = Offset(lineStartX, startY + (i * spacing)),
                end = Offset(lineEndX, startY + (i * spacing)),
                strokeWidth = 1.5.dp.toPx()
            )
        }

        val stampCenter = Offset(center.x + width/2 - 2.dp.toPx(), center.y + height/2 - 2.dp.toPx())
        drawCircle(
            color = Color(0xFF9D00FF).copy(alpha = glowPulse),
            radius = 5.dp.toPx(),
            center = stampCenter
        )
        drawLine(
            color = Color.White,
            start = Offset(stampCenter.x - 2.dp.toPx(), stampCenter.y),
            end = Offset(stampCenter.x + 2.dp.toPx(), stampCenter.y),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = Color.White,
            start = Offset(stampCenter.x, stampCenter.y - 2.dp.toPx()),
            end = Offset(stampCenter.x, stampCenter.y + 2.dp.toPx()),
            strokeWidth = 1.dp.toPx()
        )
    }
}