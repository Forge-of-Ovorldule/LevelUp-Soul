/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.compositeOver
import kotlin.math.pow

fun averageColor(colors: List<Color>): Color {
    if (colors.isEmpty()) return Color.Black

    val size = colors.size
    var sumRed = 0.0f
    var sumGreen = 0.0f
    var sumBlue = 0.0f
    var sumAlpha = 0.0f
    for (c in colors) {
        sumRed += c.red
        sumGreen += c.green
        sumBlue += c.blue
        sumAlpha += c.alpha
    }

    return Color(
        red = sumRed / size,
        green = sumGreen / size,
        blue = sumBlue / size,
        alpha = sumAlpha / size
    )
}

fun reversColor(color: Color): Color {
    return Color(1f - color.red, 1f - color.green, 1f - color.blue, color.alpha)
}

fun reversNoBiggerColor(color: Color): Color {
    val maxColor = maxOf(color.red, color.green, color.blue)
    return Color(maxColor - color.red, maxColor - color.green, maxColor - color.blue)
}

fun checkBackgroundBright(background: Color, ifDark: Color, ifLight: Color = reversColor(ifDark)): Color {
    return if ((background.red + background.green + background.blue) * 255 < 382.5) ifDark else ifLight
}

fun Color.multiply(
    r: Float = 1f,
    g: Float = 1f,
    b: Float = 1f,
    a: Float = 1f
): Color {
    return Color(
        if (this.red * r > 1f) 1f else this.red * r,
        if (this.green * g > 1f) 1f else this.green * g,
        if (this.blue * b > 1f) 1f else this.blue * b,
        if (this.alpha * a > 1f) 1f else this.alpha * a
    )
}

fun visibilityOn(
    background: Color,
    objectColor: Color,
): Float {
    val bg = background.convert(ColorSpaces.Srgb)
    val fg = objectColor.convert(ColorSpaces.Srgb)

    require(bg.alpha >= 0.999f) {
        "background должен быть непрозрачным"
    }

    val visibleObjectColor = fg.compositeOver(bg)

    val backgroundLuminance = bg.relativeLuminance()
    val objectLuminance = visibleObjectColor.relativeLuminance()

    val contrastRatio = (
            maxOf(backgroundLuminance, objectLuminance) + 0.05f
            ) / (
            minOf(backgroundLuminance, objectLuminance) + 0.05f
            )

    return ((contrastRatio - 1f) / 20f)
        .coerceIn(0f, 1f)
}

fun bestVisibleColor(
    background: Color,
    variants: Iterable<Color>,
): Color {
    return variants.maxByOrNull {
        visibilityOn(
            background = background,
            objectColor = it,
        )
    } ?: error("variants не должен быть пустым")
}

fun closestColorWithVisibility(
    background: Color,
    objectColor: Color,
    minimumVisibility: Float,
    maximumVisibility: Float? = null,
): Color {
    val maxVisibility = maximumVisibility ?: 1f

    require(minimumVisibility in 0f..1f) {
        "minimumVisibility должен быть в диапазоне от 0 до 1"
    }

    require(maxVisibility in 0f..1f) {
        "maximumVisibility должен быть в диапазоне от 0 до 1"
    }

    require(minimumVisibility <= maxVisibility) {
        "minimumVisibility не может быть больше maximumVisibility"
    }

    val backgroundSrgb = background.convert(ColorSpaces.Srgb)
    val source = objectColor.convert(ColorSpaces.Srgb)

    require(backgroundSrgb.alpha >= 0.999f) {
        "background должен быть непрозрачным"
    }

    val currentVisibility = visibilityOn(
        background = backgroundSrgb,
        objectColor = source,
    )

    if (currentVisibility in minimumVisibility..maxVisibility) {
        return source
    }

    val backgroundLuminance = backgroundSrgb.relativeLuminance()

    val minContrast = 1f + minimumVisibility * 20f
    val maxContrast = 1f + maxVisibility * 20f

    val lowerMinLuminance = (
            (backgroundLuminance + 0.05f) / maxContrast - 0.05f
            ).coerceIn(0f, backgroundLuminance)

    val lowerMaxLuminance = (
            (backgroundLuminance + 0.05f) / minContrast - 0.05f
            ).coerceIn(0f, backgroundLuminance)

    val upperMinLuminance = (
            minContrast * (backgroundLuminance + 0.05f) - 0.05f
            ).coerceIn(backgroundLuminance, 1f)

    val upperMaxLuminance = (
            maxContrast * (backgroundLuminance + 0.05f) - 0.05f
            ).coerceIn(backgroundLuminance, 1f)

    val ranges = mutableListOf<ClosedFloatingPointRange<Float>>()

    if (lowerMinLuminance <= lowerMaxLuminance) {
        ranges += lowerMinLuminance..lowerMaxLuminance
    }

    if (upperMinLuminance <= upperMaxLuminance) {
        ranges += upperMinLuminance..upperMaxLuminance
    }

    return ranges
        .mapNotNull { range ->
            closestColorInLuminanceRange(
                background = backgroundSrgb,
                source = source,
                luminanceRange = range,
            )
        }
        .minByOrNull { candidate ->
            rgbDistanceSquared(
                first = source,
                second = candidate,
            )
        }
        ?: error(
            "Невозможно достичь visibility в диапазоне " +
                    "$minimumVisibility..$maxVisibility " +
                    "изменением только RGB",
        )
}

private fun closestColorInLuminanceRange(
    background: Color,
    source: Color,
    luminanceRange: ClosedFloatingPointRange<Float>,
): Color? {
    val currentLuminance = displayedLuminance(
        background = background,
        objectColor = source,
    )

    if (currentLuminance in luminanceRange) {
        return source
    }

    val darkest = source.scaledRgb(0f)
    val brightest = source.scaledRgb(
        source.maximumBrighteningFactor(),
    )

    val minimumPossibleLuminance = displayedLuminance(
        background = background,
        objectColor = darkest,
    )

    val maximumPossibleLuminance = displayedLuminance(
        background = background,
        objectColor = brightest,
    )

    val targetLuminance = when {
        currentLuminance < luminanceRange.start &&
                maximumPossibleLuminance >= luminanceRange.start -> {
            luminanceRange.start
        }

        luminanceRange.endInclusive in minimumPossibleLuminance..<currentLuminance -> {
            luminanceRange.endInclusive
        }

        else -> return null
    }

    val coefficient = findRgbCoefficientForLuminance(
        background = background,
        source = source,
        targetLuminance = targetLuminance,
    )

    return source.scaledRgb(coefficient)
}

private fun findRgbCoefficientForLuminance(
    background: Color,
    source: Color,
    targetLuminance: Float,
): Float {
    var low = 0f
    var high = source.maximumBrighteningFactor()

    repeat(48) {
        val middle = (low + high) / 2f

        val luminance = displayedLuminance(
            background = background,
            objectColor = source.scaledRgb(middle),
        )

        if (luminance < targetLuminance) {
            low = middle
        } else {
            high = middle
        }
    }

    return (low + high) / 2f
}

private fun displayedLuminance(
    background: Color,
    objectColor: Color,
): Float {
    return objectColor
        .compositeOver(background)
        .relativeLuminance()
}

private fun Color.scaledRgb(
    factor: Float,
): Color {
    return Color(
        red = (red * factor).coerceIn(0f, 1f),
        green = (green * factor).coerceIn(0f, 1f),
        blue = (blue * factor).coerceIn(0f, 1f),
        alpha = alpha,
        colorSpace = ColorSpaces.Srgb,
    )
}

private fun Color.maximumBrighteningFactor(): Float {
    var minPositiveChannel = Float.POSITIVE_INFINITY

    if (red > 0f) minPositiveChannel = minOf(minPositiveChannel, red)
    if (green > 0f) minPositiveChannel = minOf(minPositiveChannel, green)
    if (blue > 0f) minPositiveChannel = minOf(minPositiveChannel, blue)

    return if (minPositiveChannel.isFinite()) {
        1f / minPositiveChannel
    } else {
        1f
    }
}

private fun rgbDistanceSquared(
    first: Color,
    second: Color,
): Float {
    val redDifference = first.red - second.red
    val greenDifference = first.green - second.green
    val blueDifference = first.blue - second.blue

    return redDifference * redDifference +
            greenDifference * greenDifference +
            blueDifference * blueDifference
}

private fun Color.relativeLuminance(): Float {
    val r = red.toLinearSrgb()
    val g = green.toLinearSrgb()
    val b = blue.toLinearSrgb()

    return 0.2126f * r +
            0.7152f * g +
            0.0722f * b
}

private fun Float.toLinearSrgb(): Float {
    return if (this <= 0.04045f) {
        this / 12.92f
    } else {
        (
                ((this + 0.055f) / 1.055f)
                    .toDouble()
                    .pow(2.4)
                ).toFloat()
    }
}