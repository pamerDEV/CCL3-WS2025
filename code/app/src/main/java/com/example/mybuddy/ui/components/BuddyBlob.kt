package com.example.mybuddy.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.mybuddy.ui.theme.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BuddyBlob(
    mood: BlobMood,
    colorTheme: List<Color> = BlobThemes.purple,
    modifier: Modifier = Modifier
) {
    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "blobAnimations")

    // JELLY Wobble state
    var isWobbling by remember { mutableStateOf(false) }

    val wobbleProgress = remember { Animatable(0f) }

    // Trigger wobble animation when tapped
    LaunchedEffect(isWobbling) {
        if (isWobbling) {
            wobbleProgress.snapTo(0f)
            wobbleProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = LinearEasing
                )
            )
            isWobbling = false
        }
    }

    // Wobble values - damped sine wave
    val wobbleAmount = if (isWobbling || wobbleProgress.value > 0f && wobbleProgress.value < 1f) {
        val t = wobbleProgress.value
        val damping = (1f - t)  // Fades out
        val frequency = 3f      // Number of oscillations
        sin(t * frequency * 2 * PI.toFloat()) * damping * 0.08f
    } else 0f

    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 0.985f,
        targetValue = 1.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    val sparklePulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sparkle"
    )

    // === BOUNCE ANIMATION mit SQUISH ===
    val bouncePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bouncePhase"
    )

    // Bounce nur bei EXCITED
    val bounceY: Float
    val squishX: Float
    val squishY: Float

    val jumpHeight = 84f      // how high he jumps (in dp)
    val squishStrength = 0.12f // Wie stark er squished (0.10 = 10%, 0.20 = 20%)

    if (mood == BlobMood.EXCITED) {
        // Bounce Kurve
        val bounceT = if (bouncePhase < 0.5f) {
            val t = bouncePhase * 2f
            t * (2f - t)
        } else {
            val t = (bouncePhase - 0.5f) * 2f
            (1f - t) * (1f + t)
        }

        bounceY = -bounceT * jumpHeight  // <-- Höhe hier

        // Squish beim Aufprall
        val distanceFromGround = if (bouncePhase < 0.2f) {
            bouncePhase / 0.2f
        } else if (bouncePhase > 0.8f) {
            (1f - bouncePhase) / 0.2f
        } else {
            1f
        }

        val squishAmount = (1f - distanceFromGround) * squishStrength  // <-- Stärke hier

        squishX = 1f + squishAmount           // Breiter
        squishY = 1f - squishAmount * 0.8f    // Flacher

    } else {
        bounceY = 0f
        squishX = 1f
        squishY = 1f
    }

    // === WAVE EFFECT (subtle idle animation) ===
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    // TEAR ANIMATION
    val tearProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "tearProgress"
    )

    val showSparkles = mood == BlobMood.HAPPY || mood == BlobMood.EXCITED
    val showAlert = mood == BlobMood.WORRIED
    val showEyebrows = mood == BlobMood.SAD || mood == BlobMood.WORRIED

    val bodyColorLight = colorTheme[0]
    val bodyColorMid = colorTheme[1]
    val bodyColorMidDark = colorTheme[2]
    val bodyColorDark = colorTheme[3]

    Box(
        modifier = modifier
            .size(280.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        isWobbling = true
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Base breathing + wobble deformation
                    scaleX = breatheScale * squishX * (1f + wobbleAmount)
                    scaleY = breatheScale * squishY * (1f - wobbleAmount * 0.5f)
                    translationY = bounceY
                }
        ){
            val centerX = size.width / 2
            val centerY = size.height / 2

            // Blob dimensions
            val blobWidth = size.width * 0.85f
            val blobHeight = size.height * 0.70f
            val blobCenterY = centerY + 18.dp.toPx()

            // WAVE DEFORMATION für den Path
            // Diese Werte werden auf die Kontrollpunkte angewendet
            fun waveOffset(baseX: Float, baseY: Float, phase: Float = 0f): Offset {
                val waveStrength = 3.dp.toPx()  // Subtle wave
                val xWave = sin(wavePhase + phase) * waveStrength * 0.5f
                val yWave = cos(wavePhase + phase * 0.7f) * waveStrength * 0.3f
                return Offset(baseX + xWave, baseY + yWave)
            }

            //GROUND SHADOW
            // Shadow gets wider when squished
            val shadowScale = if (mood == BlobMood.EXCITED) squishX else 1f
            drawOval(
                color = Color.Black.copy(alpha = 0.12f),
                topLeft = Offset(
                    centerX - blobWidth * 0.38f * shadowScale,
                    blobCenterY + blobHeight * 0.44f),
                size = Size(blobWidth * 0.76f * shadowScale, 14.dp.toPx())
            )

            // Kontakt shadow
            drawOval(
                color = Color.Black.copy(alpha = 0.18f),
                topLeft = Offset(
                    centerX - blobWidth * 0.22f * shadowScale,
                    blobCenterY + blobHeight * 0.40f
                ),
                size = Size(blobWidth * 0.44f * shadowScale, 6.dp.toPx())
            )

            // MAIN BLOB BODY - AUS FIGMA SVG
            val blobPath = Path().apply {
                // Scale factors - SVG war 1100 x 1001
                val scaleX = blobWidth / 1100f
                val scaleY = blobHeight / 1001f
                val offsetX = centerX - blobWidth / 2
                val offsetY = blobCenterY - blobHeight / 2

                fun px(x: Float) = x * scaleX + offsetX
                fun py(y: Float) = y * scaleY + offsetY

                moveTo(px(628.796f), py(0.937805f))
                cubicTo(px(617.073f), py(-0.207568f), px(594.559f), py(0.187561f), px(586f), py(0f))
                cubicTo(px(577.523f), py(1.14537f), px(575.115f), py(0.281494f), px(559.5f), py(5.72687f))
                cubicTo(px(543.885f), py(11.1722f), px(540.823f), py(14.3722f), px(521f), py(32.7269f))
                cubicTo(px(501.177f), py(51.0815f), px(464.462f), py(95.8f), px(445f), py(115.5f))
                cubicTo(px(445f), py(115.5f), px(393.538f), py(141.325f), px(371.5f), py(151f))
                cubicTo(px(349.462f), py(160.675f), px(286.991f), py(183.382f), px(263.614f), py(193.727f))
                cubicTo(px(245.651f), py(204.452f), px(228.614f), py(213.927f), px(210.614f), py(227.727f))
                cubicTo(px(198.014f), py(240.727f), px(117.47f), py(332.266f), px(103.62f), py(349.391f))
                cubicTo(px(88.4696f), py(382.066f), px(44.5367f), py(454.927f), px(28.6139f), py(511.727f))
                cubicTo(px(12.6911f), py(568.527f), px(5.2227f), py(658.903f), px(0f), py(699.5f))
                cubicTo(px(1.52279f), py(723.1f), px(8.57727f), py(771.797f), px(11f), py(786f))
                cubicTo(px(13.4f), py(794.4f), px(16.4139f), py(809.527f), px(26.6139f), py(827.727f))
                cubicTo(px(36.8139f), py(845.927f), px(47.4139f), py(867.727f), px(66.6139f), py(887.727f))
                cubicTo(px(85.8139f), py(907.727f), px(105.014f), py(916.327f), px(122.614f), py(927.727f))
                cubicTo(px(144.32f), py(936.102f), px(197.743f), py(961.404f), px(231.143f), py(969.604f))
                cubicTo(px(264.543f), py(977.804f), px(271.093f), py(980.006f), px(321.614f), py(985.727f))
                cubicTo(px(372.135f), py(991.448f), px(425.626f), py(995.497f), px(483.75f), py(998.208f))
                cubicTo(px(541.875f), py(1000.92f), px(548.462f), py(1001.78f), px(612.235f), py(999.282f))
                cubicTo(px(676.008f), py(996.785f), px(767.447f), py(993.056f), px(815.5f), py(989f))
                cubicTo(px(843.477f), py(981.755f), px(918.302f), py(958.355f), px(942.5f), py(949.5f))
                cubicTo(px(966.698f), py(940.645f), px(984.191f), py(928.081f), px(994.614f), py(922.727f))
                cubicTo(px(1006.04f), py(914.327f), px(1015.01f), py(910.727f), px(1030.61f), py(892.727f))
                cubicTo(px(1046.21f), py(874.727f), px(1061.51f), py(851.532f), px(1072.61f), py(832.727f))
                cubicTo(px(1083.72f), py(813.922f), px(1090.49f), py(772.061f), px(1094.96f), py(756.895f))
                cubicTo(px(1095.39f), py(734.601f), px(1102.37f), py(685.557f), px(1097.12f), py(645.425f))
                cubicTo(px(1091.87f), py(605.292f), px(1081.42f), py(552.385f), px(1072.5f), py(514f))
                cubicTo(px(1063.58f), py(475.615f), px(1061.68f), py(476.355f), px(1052.5f), py(453.5f))
                cubicTo(px(1043.32f), py(430.645f), px(1007.79f), py(368.882f), px(996.614f), py(347.727f))
                cubicTo(px(970.214f), py(321.127f), px(904.014f), py(242.927f), px(864.614f), py(214.727f))
                cubicTo(px(825.214f), py(186.527f), px(796.614f), py(171.727f), px(760.614f), py(155.727f))
                cubicTo(px(724.614f), py(139.727f), px(698.814f), py(149.872f), px(684.614f), py(134.727f))
                cubicTo(px(670.414f), py(119.581f), px(689.014f), py(94.8f), px(689.614f), py(80f))
                cubicTo(px(690.214f), py(65.2f), px(690.414f), py(70.1815f), px(687.614f), py(60.7269f))
                cubicTo(px(684.814f), py(51.2722f), px(680.752f), py(41.4394f), px(675.614f), py(32.7269f))
                cubicTo(px(670.477f), py(24.0144f), px(668.127f), py(22.5644f), px(661.927f), py(17.1644f))
                cubicTo(px(655.727f), py(11.7644f), px(651.24f), py(8.97218f), px(644.614f), py(5.72687f))
                cubicTo(px(637.988f), py(2.48156f), px(640.518f), py(2.08318f), px(628.796f), py(0.937805f))
                close()
            }

            // Light comes from upper left
            val lightDirection = Offset(
                x = -blobWidth * 0.35f,
                y = -blobHeight * 0.45f
            )

            // Main gradient
            val bodyGradient = Brush.verticalGradient(
                colors = listOf(
                    bodyColorLight,
                    bodyColorMid,
                    bodyColorMidDark,
                    bodyColorDark
                ),
                startY = blobCenterY - blobHeight * 0.5f,
                endY = blobCenterY + blobHeight * 0.5f
            )

            drawPath(path = blobPath, brush = bodyGradient)

            //inner shadow
            drawPath(
                path = blobPath,
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.28f)
                    ),
                    center = Offset(
                        centerX + blobWidth * 0.18f,
                        blobCenterY + blobHeight * 0.30f
                    ),
                    radius = blobWidth * 0.9f
                ),
                blendMode = BlendMode.Multiply
            )

            //rim light
            drawPath(
                path = blobPath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.38f),
                        Color.Transparent
                    ),
                    start = Offset(
                        centerX + lightDirection.x,
                        blobCenterY + lightDirection.y
                    ),
                    end = Offset(centerX, blobCenterY)
                ),
                blendMode = BlendMode.Screen
            )

            //SUBSURFACE SCATTERING
            drawPath(
                path = blobPath,
                brush = Brush.radialGradient(
                    colors = listOf(
                        bodyColorLight.copy(alpha = 0.45f),
                        Color.Transparent
                    ),
                    center = Offset(
                        centerX - blobWidth * 0.40f,
                        blobCenterY
                    ),
                    radius = blobWidth * 0.45f
                ),
                blendMode = BlendMode.Screen
            )

            // === SECONDARY HIGHLIGHT (smaller dot below main) ===
            drawOval(
                color = Color.White.copy(alpha = 0.5f),
                topLeft = Offset(centerX - blobWidth * 0.32f, blobCenterY + blobHeight * 0.02f),
                size = Size(14.dp.toPx(), 10.dp.toPx())
            )

            // === EYES ===
            val eyeY = blobCenterY - blobHeight * 0.01f
            val eyeSpacing = blobWidth * 0.19f
            val eyeRadius = 20.dp.toPx()

            val eyeOffsetY = when (mood) {
                BlobMood.SLEEPY -> 6.dp.toPx()
                BlobMood.SAD -> 2.dp.toPx()
                else -> 0f
            }

            if (mood == BlobMood.SLEEPY) {
                // Sleepy closed eyes - curved lines
                val sleepyWidth = 16.dp.toPx()

                // Left eye
                val leftEyePath = Path().apply {
                    moveTo(centerX - eyeSpacing - sleepyWidth, eyeY + eyeOffsetY)
                    quadraticTo(
                        centerX - eyeSpacing, eyeY + eyeOffsetY + 10.dp.toPx(),
                        centerX - eyeSpacing + sleepyWidth, eyeY + eyeOffsetY
                    )
                }
                drawPath(leftEyePath, EyeColor, style = Stroke(3.5.dp.toPx(), cap = StrokeCap.Round))

                // Right eye
                val rightEyePath = Path().apply {
                    moveTo(centerX + eyeSpacing - sleepyWidth, eyeY + eyeOffsetY)
                    quadraticTo(
                        centerX + eyeSpacing, eyeY + eyeOffsetY + 10.dp.toPx(),
                        centerX + eyeSpacing + sleepyWidth, eyeY + eyeOffsetY
                    )
                }
                drawPath(rightEyePath, EyeColor, style = Stroke(3.5.dp.toPx(), cap = StrokeCap.Round))

            } else {
                // Normal glossy eyes
                listOf(-eyeSpacing, eyeSpacing).forEach { offset ->
                    val ex = centerX + offset
                    val ey = eyeY + eyeOffsetY

                    // === EYE SOCKET SHADOW (3D Tiefe) ===
                    drawCircle(
                        Color.Black.copy(alpha = 0.14f),
                        eyeRadius * 1.08f,
                        Offset(ex + 2.dp.toPx(), ey + 2.dp.toPx())
                    )

                    // White base
                    drawCircle(Color.White, eyeRadius, Offset(ex, ey))

                    // Dark pupil with gradient
                    val pupilRadius = eyeRadius * 0.82f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF3D2066),
                                Color(0xFF2A1B3D),
                                EyeColor
                            ),
                            center = Offset(ex + 2.dp.toPx(), ey - 2.dp.toPx()),
                            radius = pupilRadius
                        ),
                        radius = pupilRadius,
                        center = Offset(ex, ey)
                    )

                    // Main highlight
                    drawCircle(
                        Color.White,
                        5.5.dp.toPx(),
                        Offset(ex + 5.dp.toPx(), ey - 5.dp.toPx())
                    )

                    // Secondary highlight
                    drawCircle(
                        Color.White.copy(alpha = 0.6f),
                        2.5.dp.toPx(),
                        Offset(ex - 3.dp.toPx(), ey + 4.dp.toPx())
                    )
                }
                // === ANIMATED TEARS (nur bei SAD) ===
                if (mood == BlobMood.SAD) {
                    val tearColor = Color(0xFF7DD3FC)

                    // Animation Phasen:
                    // 0.0 - 0.4: Wasser sammelt sich (pooling)
                    // 0.4 - 0.5: Tropfen formt sich unten am Auge
                    // 0.5 - 1.0: Träne fällt runter, Pool leert sich

                    listOf(
                        -eyeSpacing to tearProgress,
                        eyeSpacing to (tearProgress + 0.3f) % 1f
                    ).forEach { (offset, progress) ->
                        val ex = centerX + offset
                        val ey = eyeY + eyeOffsetY

                        // === POOL LEVEL basierend auf Animation ===
                        val poolFill = when {
                            progress < 0.4f -> progress / 0.4f           // 0→1: füllt sich
                            progress < 0.5f -> 1f                             // voll
                            else -> 1f - ((progress - 0.5f) / 0.5f) * 0.7f   // leert sich teilweise
                        }

                        // Pool nur zeichnen wenn gefüllt
                        if (poolFill > 0.05f) {
                            val poolPath = Path().apply {
                                val poolDepth = 0.95f
                                val poolTop = 0.75f - (poolFill * 0.25f)  // Je voller, desto höher
                                val waterLevel = 0.55f - (poolFill * 0.25f)

                                val startX = ex - eyeRadius * 0.85f * poolFill.coerceIn(0.3f, 1f)
                                val endX = ex + eyeRadius * 0.85f * poolFill.coerceIn(0.3f, 1f)
                                val bottomY = ey + eyeRadius * poolDepth

                                moveTo(startX, ey + eyeRadius * poolTop)

                                // Äußerer Bogen
                                quadraticTo(
                                    ex, bottomY,
                                    endX, ey + eyeRadius * poolTop
                                )

                                // Innerer Bogen - Wasseroberfläche
                                quadraticTo(
                                    ex + eyeRadius * 0.2f, ey + eyeRadius * (waterLevel + 0.08f),
                                    ex, ey + eyeRadius * waterLevel
                                )
                                quadraticTo(
                                    ex - eyeRadius * 0.2f, ey + eyeRadius * (waterLevel + 0.08f),
                                    startX, ey + eyeRadius * poolTop
                                )

                                close()
                            }

                            drawPath(poolPath, tearColor.copy(alpha = 0.55f * poolFill))
                        }

                        // === FALLING TEARDROP ===
                        if (progress > 0.4f) {
                            val dropProgress = ((progress - 0.4f) / 0.6f).coerceIn(0f, 1f)

                            // Tropfen Position
                            val dropStartY = ey + eyeRadius * 0.95f
                            val dropEndY = ey + eyeRadius * 3.5f  // Wie weit die Träne fällt
                            val dropY = dropStartY + (dropEndY - dropStartY) * dropProgress

                            // Tropfen wird kleiner und schneller während er fällt
                            val dropScale = 1f - (dropProgress * 0.3f)
                            val dropAlpha = 1f - (dropProgress * 0.8f)  // Fade out am Ende

                            if (dropAlpha > 0.1f) {
                                val dropWidth = 7.dp.toPx() * dropScale
                                val dropHeight = 10.dp.toPx() * dropScale

                                // Klassische Tränenform
                                val tearDropPath = Path().apply {
                                    moveTo(ex, dropY - dropHeight)  // Spitze oben

                                    // Rechte Seite
                                    cubicTo(
                                        ex + dropWidth * 0.3f, dropY - dropHeight * 0.5f,
                                        ex + dropWidth, dropY + dropHeight * 0.2f,
                                        ex, dropY + dropHeight  // Unten Mitte
                                    )

                                    // Linke Seite
                                    cubicTo(
                                        ex - dropWidth, dropY + dropHeight * 0.2f,
                                        ex - dropWidth * 0.3f, dropY - dropHeight * 0.5f,
                                        ex, dropY - dropHeight  // Zurück zur Spitze
                                    )

                                    close()
                                }

                                drawPath(tearDropPath, tearColor.copy(alpha = dropAlpha * 0.8f))

                                // Highlight auf dem Tropfen
                                drawCircle(
                                    Color.White.copy(alpha = dropAlpha * 0.7f),
                                    2.5.dp.toPx() * dropScale,
                                    Offset(ex - 1.5.dp.toPx(), dropY - dropHeight * 0.3f)
                                )
                            }
                        }
                    }
                }
            }

            // === EYEBROWS ===
            if (showEyebrows) {
                val browY = eyeY - eyeRadius - 10.dp.toPx()

                if (mood == BlobMood.SAD) {
                    // Sad - sanfte gebogene Augenbrauen
                    val leftBrowPath = Path().apply {
                        moveTo(centerX - eyeSpacing - 12.dp.toPx(), browY)
                        quadraticTo(
                            centerX - eyeSpacing, browY - 6.dp.toPx(),  // Nach oben gebogen
                            centerX - eyeSpacing + 12.dp.toPx(), browY
                        )
                    }
                    drawPath(leftBrowPath, EyeColor, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))

                    val rightBrowPath = Path().apply {
                        moveTo(centerX + eyeSpacing - 12.dp.toPx(), browY)
                        quadraticTo(
                            centerX + eyeSpacing, browY - 6.dp.toPx(),
                            centerX + eyeSpacing + 12.dp.toPx(), browY
                        )
                    }
                    drawPath(rightBrowPath, EyeColor, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
                } else {
                    // Worried - angled up toward center
                    drawLine(EyeColor,
                        Offset(centerX - eyeSpacing - 12.dp.toPx(), browY + 4.dp.toPx()),
                        Offset(centerX - eyeSpacing + 8.dp.toPx(), browY - 5.dp.toPx()),
                        strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(EyeColor,
                        Offset(centerX + eyeSpacing - 8.dp.toPx(), browY - 5.dp.toPx()),
                        Offset(centerX + eyeSpacing + 12.dp.toPx(), browY + 4.dp.toPx()),
                        strokeWidth = 3.dp.toPx(), cap = StrokeCap.Round)
                }
            }

            // === CHEEKS ===
            val cheekY = blobCenterY + blobHeight * 0.16f
            val cheekSpacing = blobWidth * 0.32f
            val cheekW = 22.dp.toPx()
            val cheekH = 18.dp.toPx()
            val cheekAlpha = when (mood) {
                BlobMood.HAPPY, BlobMood.EXCITED -> 0.7f
                else -> 0.55f
            }

            drawOval(CheekPink.copy(alpha = cheekAlpha),
                Offset(centerX - cheekSpacing - cheekW/2, cheekY - cheekH/2),
                Size(cheekW, cheekH))
            drawOval(CheekPink.copy(alpha = cheekAlpha),
                Offset(centerX + cheekSpacing - cheekW/2, cheekY - cheekH/2),
                Size(cheekW, cheekH))

            // === MOUTH ===
            val mouthY = blobCenterY + blobHeight * 0.20f

            when (mood) {
                BlobMood.HAPPY -> {
                    val path = Path().apply {
                        moveTo(centerX - 14.dp.toPx(), mouthY)
                        quadraticTo(centerX, mouthY + 12.dp.toPx(), centerX + 14.dp.toPx(), mouthY)
                    }
                    drawPath(path, EyeColor, style = Stroke(3.5.dp.toPx(), cap = StrokeCap.Round))
                }
                BlobMood.EXCITED -> {
                    val mouthWidth = 38.dp.toPx()
                    val mouthHeight = 44.dp.toPx()

                    // Mund Form: Oben flach (Smile-Kurve), unten rund
                    val mouthPath = Path().apply {
                        // Start left
                        moveTo(centerX - mouthWidth / 2, mouthY)

                        // Obere Kante - light Smile-curf
                        quadraticTo(
                            centerX, mouthY - 2.dp.toPx(),  // Leicht nach oben = Lächeln
                            centerX + mouthWidth / 2, mouthY
                        )

                        // Untere Kante - rund nach unten
                        quadraticTo(
                            centerX, mouthY + mouthHeight,
                            centerX - mouthWidth / 2, mouthY
                        )

                        close()
                    }

                    // black mouth
                    drawPath(mouthPath, EyeColor)

                    // Zunge unten
                    drawOval(
                        color = CheekPink,
                        topLeft = Offset(
                            centerX - 7.dp.toPx(),
                            mouthY + 10.dp.toPx()),
                        size = Size(14.dp.toPx(), 10.dp.toPx())
                    )
                }
                BlobMood.SAD -> {
                    val path = Path().apply {
                        moveTo(centerX - 12.dp.toPx(), mouthY + 5.dp.toPx())
                        quadraticTo(centerX, mouthY - 7.dp.toPx(), centerX + 12.dp.toPx(), mouthY + 5.dp.toPx())
                    }
                    drawPath(path, EyeColor, style = Stroke(3.5.dp.toPx(), cap = StrokeCap.Round))
                }
                BlobMood.WORRIED -> {
                    val path = Path().apply {
                        moveTo(centerX - 9.dp.toPx(), mouthY)
                        quadraticTo(centerX - 3.dp.toPx(), mouthY - 4.dp.toPx(), centerX, mouthY)
                        quadraticTo(centerX + 3.dp.toPx(), mouthY + 4.dp.toPx(), centerX + 9.dp.toPx(), mouthY)
                    }
                    drawPath(path, EyeColor, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
                }
                BlobMood.SLEEPY -> {
                    val path = Path().apply {
                        moveTo(centerX - 8.dp.toPx(), mouthY)
                        quadraticTo(centerX, mouthY + 5.dp.toPx(), centerX + 8.dp.toPx(), mouthY)
                    }
                    drawPath(path, EyeColor, style = Stroke(3.dp.toPx(), cap = StrokeCap.Round))
                }
            }

            //micro noice
            repeat(120) {
                val x = centerX + (Math.random().toFloat() - 0.5f) * blobWidth
                val y = blobCenterY + (Math.random().toFloat() - 0.5f) * blobHeight

                drawCircle(
                    Color.White.copy(alpha = 0.025f),
                    radius = 1.2.dp.toPx(),
                    center = Offset(x, y),
                    blendMode = BlendMode.Overlay
                )
            }

            // === SPARKLES ===
            if (showSparkles) {
                val sparkles = listOf(
                    Triple(centerX - blobWidth * 0.50f, blobCenterY - blobHeight * 0.60f, 9.dp.toPx() to SparkleYellow),
                    Triple(centerX + blobWidth * 0.45f, blobCenterY - blobHeight * 0.52f, 7.dp.toPx() to SparkleCyan),
                    Triple(centerX + blobWidth * 0.28f, blobCenterY - blobHeight * 0.72f, 6.dp.toPx() to SparkleCyan)
                )

                sparkles.forEach { (x, y, data) ->
                    val starSize = data.first
                    val color = data.second

                    val star = Path().apply {
                        moveTo(x, y - starSize)
                        lineTo(x + starSize * 0.22f, y - starSize * 0.22f)
                        lineTo(x + starSize, y)
                        lineTo(x + starSize * 0.22f, y + starSize * 0.22f)
                        lineTo(x, y + starSize)
                        lineTo(x - starSize * 0.22f, y + starSize * 0.22f)
                        lineTo(x - starSize, y)
                        lineTo(x - starSize * 0.22f, y - starSize * 0.22f)
                        close()
                    }
                    drawPath(star, color.copy(alpha = sparklePulse))
                }

                drawCircle(SparkleYellow.copy(alpha = sparklePulse * 0.7f), 2.5.dp.toPx(),
                    Offset(centerX - blobWidth * 0.36f, blobCenterY - blobHeight * 0.68f))
            }

            // === ALERT (Worried) ===
            if (showAlert) {
                val ax = centerX + blobWidth * 0.42f
                val ay = blobCenterY - blobHeight * 0.50f

                drawLine(SparkleYellow, Offset(ax, ay - 9.dp.toPx()), Offset(ax, ay + 3.dp.toPx()),
                    strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
                drawCircle(SparkleYellow, 2.5.dp.toPx(), Offset(ax, ay + 9.dp.toPx()))
            }
        }

        // === ZZZ (Sleepy) ===
        if (mood == BlobMood.SLEEPY) {
            Text(
                text = "zᶻZ",
                color = SparkleCyan,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 28.dp, top = 60.dp)
            )
        }
    }
}