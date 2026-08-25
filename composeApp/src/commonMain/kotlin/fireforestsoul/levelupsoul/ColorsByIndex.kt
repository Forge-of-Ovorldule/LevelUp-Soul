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
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

val listMutex: Mutex = Mutex()

suspend fun calculateProgressiveColor(
    index: Int,
    onColorUpdate: (Color) -> Unit,
    oldColor: Color = LocalSaveManager.data.habits[index].color
) {
    if (LocalSaveManager.data.habits[index].typeOfColor == TypeOfColorHabit.SELECTED) {
        onColorUpdate(LocalSaveManager.data.habits[index].color)
        return
    }

    val kRed = oldColor.red
    var kProgress = kRed
    var kLevel = kRed
    var kNeedDays = kRed

    val kGreen = oldColor.green
    var kDays = kGreen
    var kNeedGoal = kGreen
    var kLevelChange = kGreen

    val kBlue = oldColor.blue
    var kStreak = kBlue
    var kTypeOfGoal = kBlue

    fun emitCurrentColor() {
        val red = ((kProgress + kLevel + kNeedDays) / 3 * 255).toInt().coerceIn(0, 255)
        val green = ((kDays + kNeedGoal + kLevelChange) / 3 * 255).toInt().coerceIn(0, 255)
        val blue = ((kStreak + kTypeOfGoal) / 2 * 255).toInt().coerceIn(0, 255)

        onColorUpdate(Color(red, green, blue))
    }

    val addProcess = "$ts_Calculating_adaptive_color_habits ($ts_Habit $index)"

    withContext(Dispatchers.Default) {
        try {
            listMutex.withLock { listProgressedStatusBar.add(addProcess) }

            var maxProgress = Float.MIN_VALUE
            var minProgress = Float.MAX_VALUE
            for (habit in LocalSaveManager.data.habits) {
                maxProgress = max(progress(habit), maxProgress)
                minProgress = min(progress(habit), minProgress)
            }
            kProgress =
                if (maxProgress == minProgress) 1f else (progress(index) - minProgress) / (if (maxProgress - minProgress == 0f) 1f else (maxProgress - minProgress))

            emitCurrentColor()
            yield()

            var maxDays = Int.MIN_VALUE
            var minDays = Int.MAX_VALUE
            for (habit in LocalSaveManager.data.habits) {
                maxDays = max(habit.totalDays(), maxDays)
                minDays = min(habit.totalDays(), minDays)
            }
            kDays =
                if (maxDays == minDays) 1f
                else (LocalSaveManager.data.habits[index].totalDays() - minDays).toFloat() /
                        (if (maxDays - minDays == 0) 1f else (maxDays - minDays).toFloat())

            emitCurrentColor()
            yield()

            if (habitStreaks(index).isNotEmpty()) {
                var maxStreak = Int.MIN_VALUE
                val minStreak = 0
                for (habit in LocalSaveManager.data.habits) {
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
            for (habit in LocalSaveManager.data.habits) {
                maxLevel = max(habit.level, maxLevel)
                minLevel = min(habit.level, minLevel)
            }
            kLevel =
                if (maxLevel == minLevel) 1f else (LocalSaveManager.data.habits[index].level - minLevel).toFloat() / (if (maxLevel - minLevel == 0) 1f else (maxLevel - minLevel).toFloat())

            emitCurrentColor()
            yield()

            var maxNeedGoal = Double.MIN_VALUE.toBigDecimal()
            var minNeedGoal = Double.MAX_VALUE.toBigDecimal()
            for (habit in LocalSaveManager.data.habits) {
                maxNeedGoal = maxOf(habit.numericalGoal, maxNeedGoal)
                minNeedGoal = minOf(habit.numericalGoal, minNeedGoal)
            }
            val diffGoal = maxNeedGoal - minNeedGoal
            kNeedGoal =
                if (maxNeedGoal == minNeedGoal) 1f else (LocalSaveManager.data.habits[index].numericalGoal - minNeedGoal).floatValue(
                    false
                ) / (if (diffGoal == BigDecimal.ZERO) 1f else diffGoal.floatValue(
                    false
                ))

            emitCurrentColor()
            yield()

            kTypeOfGoal = when (LocalSaveManager.data.habits[index].typeOfGoal) {
                TypeOfGoalHabit.NO_MORE -> 0f
                TypeOfGoalHabit.AT_LEAST -> 1f
            }

            emitCurrentColor()
            yield()

            var maxNeedDays = Int.MIN_VALUE
            var minNeedDays = Int.MAX_VALUE
            for (habit in LocalSaveManager.data.habits) {
                maxNeedDays = maxOf(habit.periodForGoalCompletion, maxNeedDays)
                minNeedDays = minOf(habit.periodForGoalCompletion, minNeedDays)
            }
            kNeedDays =
                if (maxNeedDays == minNeedDays) 1f else (LocalSaveManager.data.habits[index].periodForGoalCompletion - minNeedDays).toFloat() / (if (maxNeedDays - minNeedDays == 0) 1f else (maxNeedDays - minNeedDays).toFloat())

            emitCurrentColor()
            yield()

            kLevelChange = ((if (LocalSaveManager.data.habits[index].changeLevel) 1f else 0f)
                    + (if (LocalSaveManager.data.habits[index].changeNumericalGoalWithLevel) 1f else 0f)
                    + (if (LocalSaveManager.data.habits[index].changePeriodForGoalCompletionWithLevel) 1f else 0f)) / 3f

            emitCurrentColor()

        } finally {
            withContext(NonCancellable) {
                listMutex.withLock {
                    listProgressedStatusBar.removeAll { it == addProcess }
                }
            }
        }
    }
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