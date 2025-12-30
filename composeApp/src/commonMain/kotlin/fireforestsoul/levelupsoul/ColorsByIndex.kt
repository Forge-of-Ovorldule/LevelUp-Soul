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
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

suspend fun calculateProgressiveColor(
    index: Int,
    onColorUpdate: (Color) -> Unit
) {
    if (habits[index].typeOfColorHabits == TypeOfColorHabits.SELECTED) {
        onColorUpdate(habits[index].colorGood)
        return
    }

    var kProgress = 0f
    var kLevel = 0f
    var kNeedDays = 0f

    var kDays = 0f
    var kNeedGoal = 0f
    var kLevelChange = 0f

    var kStreak = 0f
    var kTypeOfGoal = 0f

    fun emitCurrentColor() {
        val red = ((kProgress + kLevel + kNeedDays) / 3 * 255).toInt().coerceIn(0, 255)
        val green = ((kDays + kNeedGoal + kLevelChange) / 3 * 255).toInt().coerceIn(0, 255)
        val blue = ((kStreak + kTypeOfGoal) / 2 * 255).toInt().coerceIn(0, 255)

        onColorUpdate(Color(red, green, blue))
    }

    val addProcess = ts_Calculating_adaptive_color_habits
    listProgressedStatusBar.add(addProcess)

    withContext(Dispatchers.Default) {
        try {
            var maxProgress = Float.MIN_VALUE
            var minProgress = Float.MAX_VALUE
            for (habit in habits) {
                maxProgress = max(progress(habit), maxProgress)
                minProgress = min(progress(habit), minProgress)
            }
            kProgress =
                if (maxProgress == minProgress) 1f else (progress(index) - minProgress) / (if (maxProgress - minProgress == 0f) 1f else (maxProgress - minProgress))

            emitCurrentColor()
            yield()

            var maxDays = Int.MIN_VALUE
            var minDays = Int.MAX_VALUE
            for (habit in habits) {
                maxDays = max(habit.habitDay.size, maxDays)
                minDays = min(habit.habitDay.size, minDays)
            }
            kDays =
                if (maxDays == minDays) 1f else (habits[index].habitDay.size - minDays).toFloat() / (if (maxDays - minDays == 0) 1f else (maxDays - minDays).toFloat())

            emitCurrentColor()
            yield()

            if (habitStreaks(index).isNotEmpty()) {
                var maxStreak = Int.MIN_VALUE
                val minStreak = 0
                for (habit in habits) {
                    val s = if (habitStreaks(habit).isNotEmpty()) habitStreaks(habit)[0] else 0
                    maxStreak = max(s, maxStreak)
                }
                kStreak =
                    if (maxStreak == minStreak) 1f else (habitStreaks(index)[0] - minStreak).toFloat() / (if (maxStreak - minStreak == 0) 1f else (maxStreak - minStreak).toFloat())
            } else {
                kStreak = 0f
            }

            emitCurrentColor()
            yield()

            var maxLevel = Int.MIN_VALUE
            var minLevel = Int.MAX_VALUE
            for (habit in habits) {
                maxLevel = max(habit.level, maxLevel)
                minLevel = min(habit.level, minLevel)
            }
            kLevel =
                if (maxLevel == minLevel) 1f else (habits[index].level - minLevel).toFloat() / (if (maxLevel - minLevel == 0) 1f else (maxLevel - minLevel).toFloat())

            emitCurrentColor()
            yield()

            var maxNeedGoal = Double.MIN_VALUE.toBigDecimal()
            var minNeedGoal = Double.MAX_VALUE.toBigDecimal()
            for (habit in habits) {
                maxNeedGoal = maxOf(habit.needGoal, maxNeedGoal)
                minNeedGoal = minOf(habit.needGoal, minNeedGoal)
            }
            val diffGoal = maxNeedGoal - minNeedGoal
            kNeedGoal =
                if (maxNeedGoal == minNeedGoal) 1f else (habits[index].needGoal - minNeedGoal).floatValue(false) / (if (diffGoal == BigDecimal.ZERO) 1f else diffGoal.floatValue(
                    false
                ))

            emitCurrentColor()
            yield()

            kTypeOfGoal = when (habits[index].typeOfGoalHabits) {
                TypeOfGoalHabits.NO_MORE -> 0f
                TypeOfGoalHabits.AT_LEAST -> 1f
            }

            emitCurrentColor()
            yield()

            var maxNeedDays = Int.MIN_VALUE
            var minNeedDays = Int.MAX_VALUE
            for (habit in habits) {
                maxNeedDays = maxOf(habit.needDays, maxNeedDays)
                minNeedDays = minOf(habit.needDays, minNeedDays)
            }
            kNeedDays =
                if (maxNeedDays == minNeedDays) 1f else (habits[index].needDays - minNeedDays).toFloat() / (if (maxNeedDays - minNeedDays == 0) 1f else (maxNeedDays - minNeedDays).toFloat())

            emitCurrentColor()
            yield()

            kLevelChange = ((if (habits[index].changeLevel) 1f else 0f)
                    + (if (habits[index].changeNeedGoalWithLevel) 1f else 0f)
                    + (if (habits[index].changeNeedDaysWithLevel) 1f else 0f)) / 3f

            emitCurrentColor()

        } finally {
            listProgressedStatusBar.remove(addProcess)
        }
    }
}

fun getSeeSoulColor(): Color {
    var maxDays = 0
    for (habit in habits) {
        maxDays = max(habit.habitDay.size, maxDays)
    }
    return if (soul_color_type == TypeOfColorHabits.SELECTED) soul_color
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
    return if (soul_color_type == TypeOfColorHabits.SELECTED) soul_color
    else getSeeSoulColor()
}