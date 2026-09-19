package fireforestsoul.levelupsoul

import androidx.annotation.FloatRange
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Modifier.insideBorder(
    width: Dp,
    brush: Brush,
    shape: Shape,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1f,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode? = null,
    clipToInner: Boolean = false,
    pathEffect: PathEffect? = null,
): Modifier {
    require(width >= 0.dp) { "width must be >= 0.dp" }

    if (width == 0.dp) return this

    return drawWithCache {
        val borderWidth = width.toPx()
        val outline = shape.createOutline(size, layoutDirection, this)
        val contour = Path().apply { add(outline) }

        val innerPath = if (clipToInner) {
            Path().apply {
                add(outline.deflateBy(borderWidth))
            }
        } else {
            null
        }

        onDrawWithContent {
            if (size.width <= 0f || size.height <= 0f) {
                drawContent()
                return@onDrawWithContent
            }

            if (innerPath != null) {
                drawContext.canvas.save()
                drawContext.canvas.clipPath(innerPath, ClipOp.Intersect)
                drawContent()
                drawContext.canvas.restore()
            } else {
                drawContent()
            }

            clipPath(contour, ClipOp.Intersect) {
                drawPath(
                    path = contour,
                    brush = brush,
                    alpha = alpha,
                    colorFilter = colorFilter,
                    blendMode = blendMode ?: BlendMode.SrcOver,
                    style = Stroke(
                        width = borderWidth * 2f,
                        cap = StrokeCap.Butt,
                        join = StrokeJoin.Miter,
                        pathEffect = pathEffect,
                    ),
                )
            }
        }
    }
}

@Stable
fun Modifier.insideBorder(
    width: Dp,
    color: Color,
    shape: Shape,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1f,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode? = null,
    clipToInner: Boolean = false,
    pathEffect: PathEffect? = null,
): Modifier = insideBorder(
    width = width,
    brush = Brush.linearGradient(listOf(color, color)),
    shape = shape,
    alpha = alpha,
    colorFilter = colorFilter,
    blendMode = blendMode,
    clipToInner = clipToInner,
    pathEffect = pathEffect,
)

private fun Path.add(outline: Outline) {
    when (outline) {
        is Outline.Rectangle -> addRect(outline.rect)
        is Outline.Rounded -> addRoundRect(outline.roundRect)
        is Outline.Generic -> addPath(outline.path)
    }
}

private fun Outline.deflateBy(amount: Float): Outline =
    when (this) {
        is Outline.Rectangle -> Outline.Rectangle(rect.deflate(amount))

        is Outline.Rounded -> Outline.Rounded(
            RoundRect(
                left = roundRect.left + amount,
                top = roundRect.top + amount,
                right = roundRect.right - amount,
                bottom = roundRect.bottom - amount,
                topLeftCornerRadius = roundRect.topLeftCornerRadius.shrinkBy(amount),
                topRightCornerRadius = roundRect.topRightCornerRadius.shrinkBy(amount),
                bottomRightCornerRadius = roundRect.bottomRightCornerRadius.shrinkBy(amount),
                bottomLeftCornerRadius = roundRect.bottomLeftCornerRadius.shrinkBy(amount),
            )
        )

        is Outline.Generic -> this
    }

private fun CornerRadius.shrinkBy(amount: Float): CornerRadius =
    copy(x = (x - amount).coerceAtLeast(0f), y = (y - amount).coerceAtLeast(0f))

@Stable
fun Modifier.outsideBorder(
    width: Dp,
    brush: Brush,
    shape: Shape,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1f,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode? = null,
    pathEffect: PathEffect? = null,
): Modifier {
    require(width >= 0.dp) { "width must be >= 0.dp" }

    if (width == 0.dp) return this

    return drawWithCache {
        val borderWidth = width.toPx()

        val outline = shape.createOutline(size, layoutDirection, this)
        val contour = Path().apply { add(outline) }

        onDrawWithContent {
            drawContent()

            clipPath(contour, ClipOp.Difference) {
                drawPath(
                    path = contour,
                    brush = brush,
                    alpha = alpha,
                    colorFilter = colorFilter,
                    blendMode = blendMode ?: BlendMode.SrcOver,
                    style = Stroke(
                        width = borderWidth * 2f,
                        cap = StrokeCap.Butt,
                        join = StrokeJoin.Miter,
                        pathEffect = pathEffect,
                    ),
                )
            }
        }
    }
}

@Stable
fun Modifier.outsideBorder(
    width: Dp,
    color: Color,
    shape: Shape,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1f,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode? = null,
    pathEffect: PathEffect? = null,
): Modifier = outsideBorder(
    width = width,
    brush = Brush.linearGradient(listOf(color, color)),
    shape = shape,
    alpha = alpha,
    colorFilter = colorFilter,
    blendMode = blendMode,
    pathEffect = pathEffect,
)

fun Modifier.sideHills(
    color: Color,
    cutoutRadius: Dp,
): Modifier {
    return drawBehind {
        val radius = cutoutRadius
            .toPx()
            .coerceIn(0f, size.height)

        if (radius == 0f) {
            return@drawBehind
        }

        val kappa = 0.5522848f
        val bottomY = size.height
        val topY = bottomY - radius

        val leftHill = Path().apply {
            moveTo(0f, topY)
            lineTo(0f, bottomY)
            lineTo(-radius, bottomY)

            cubicTo(
                -radius + radius * kappa,
                bottomY,
                0f,
                topY + radius * kappa,
                0f,
                topY,
            )

            close()
        }

        val rightHill = Path().apply {
            moveTo(size.width, topY)
            lineTo(size.width, bottomY)
            lineTo(size.width + radius, bottomY)

            cubicTo(
                size.width + radius - radius * kappa,
                bottomY,
                size.width,
                topY + radius * kappa,
                size.width,
                topY,
            )

            close()
        }

        drawPath(
            path = leftHill,
            color = color,
        )

        drawPath(
            path = rightHill,
            color = color,
        )
    }
}