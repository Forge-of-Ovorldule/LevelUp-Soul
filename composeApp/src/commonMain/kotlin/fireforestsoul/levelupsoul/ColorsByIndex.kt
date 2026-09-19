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
import kotlin.math.max

suspend fun calculateProgressiveColor(
    index: Int,
    onColorUpdate: (Color) -> Unit
) {
    return LocalSaveManager.data.habits[index].calculateProgressiveColor(onColorUpdate)
}

fun getSeeSoulColor(): Color {
    var maxDays = 0
    for (habit in LocalSaveManager.data.habits) {
        maxDays = max(habit.totalDays(), maxDays)
    }
    return if (LocalSaveManager.data.soulColorType == TypeOfColorHabit.SELECTED) LocalSaveManager.data.soulColor
    else Color(
        (progressAll(maxDays) * 255.0).toInt(),
        255,
        255
    )
}

fun getNoSeeSoulColor(): Color {
    return Color(
        getSeeSoulColor().red * 0.5f,
        getSeeSoulColor().green * 0.5f,
        getSeeSoulColor().blue * 0.5f
    )
}

fun getSoulRealColor(): Color {
    return if (LocalSaveManager.data.soulColorType == TypeOfColorHabit.SELECTED) LocalSaveManager.data.soulColor
    else getSeeSoulColor()
}