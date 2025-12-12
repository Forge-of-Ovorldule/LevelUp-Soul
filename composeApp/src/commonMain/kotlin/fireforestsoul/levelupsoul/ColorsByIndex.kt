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

fun seeColorByIndex(index: Int): Color {
    if (habits[index].typeOfColorHabits == TypeOfColorHabits.SELECTED)
        return habits[index].colorGood

    fun getProgressK(): Float {
        var maxProgress = Float.MIN_VALUE
        var minProgress = Float.MAX_VALUE
        for (habit in habits) {
            maxProgress = max(progress(habit), maxProgress)
            minProgress = min(progress(habit), maxProgress)
        }
        return if (maxProgress == minProgress) 1f else (progress(index) - minProgress) / (if (maxProgress - minProgress == 0f) 1f else (maxProgress - minProgress))
    }

    fun getDaysK(): Float {
        var maxDays = Int.MIN_VALUE
        var minDays = Int.MAX_VALUE
        for (habit in habits) {
            maxDays = max(habit.habitDay.size, maxDays)
            minDays = min(habit.habitDay.size, maxDays)
        }
        return if (maxDays == minDays) 1f else (habits[index].habitDay.size - minDays).toFloat() / (if (maxDays - minDays == 0) 1f else (maxDays - minDays).toFloat())
    }

    fun getStreakK(): Float {
        if (habitStreaks(index).isNotEmpty()) {
            var maxStreak = Int.MIN_VALUE
            val minStreak = 0
            for (habit in habits) {
                maxStreak = max(if (habitStreaks(habit).isNotEmpty()) habitStreaks(habit)[0] else 0, maxStreak)
            }
            return if (maxStreak == minStreak) 1f else (habitStreaks(index)[0] - minStreak).toFloat() / (if (maxStreak - minStreak == 0) 1f else (maxStreak - minStreak).toFloat())
        }
        return 0f
    }

    fun getLevelK(): Float {
        var maxLevel = Int.MIN_VALUE
        var minLevel = Int.MAX_VALUE
        for (habit in habits) {
            maxLevel = max(habit.level, maxLevel)
            minLevel = min(habit.level, maxLevel)
        }
        return if (maxLevel == minLevel) 1f else (habits[index].level - minLevel).toFloat() / (if (maxLevel - minLevel == 0) 1f else (maxLevel - minLevel).toFloat())
    }

    fun getNeedGoalK(): Float {
        var maxNeedGoal = Double.MIN_VALUE.toBigDecimal()
        var minNeedGoal = Double.MAX_VALUE.toBigDecimal()
        for (habit in habits) {
            maxNeedGoal = maxOf(habit.needGoal, maxNeedGoal)
            minNeedGoal = minOf(habit.needGoal, maxNeedGoal)
        }
        return if (maxNeedGoal == minNeedGoal) 1f else (habits[index].needGoal - minNeedGoal).floatValue(false) / (if (maxNeedGoal - minNeedGoal == BigDecimal.ZERO) 1f else (maxNeedGoal - minNeedGoal).floatValue(
            false
        ))
    }

    fun getTypeOfGoalK(): Float {
        return when (habits[index].typeOfGoalHabits) {
            TypeOfGoalHabits.NO_MORE -> 0f
            TypeOfGoalHabits.AT_LEAST -> 1f
        }
    }

    fun getNeedDaysK(): Float {
        var maxNeedDays = Int.MIN_VALUE
        var minNeedDays = Int.MAX_VALUE
        for (habit in habits) {
            maxNeedDays = maxOf(habit.needDays, maxNeedDays)
            minNeedDays = minOf(habit.needDays, maxNeedDays)
        }
        return if (maxNeedDays == minNeedDays) 1f else (habits[index].needDays - minNeedDays).toFloat() / (if (maxNeedDays - minNeedDays == 0) 1f else (maxNeedDays - minNeedDays).toFloat())
    }

    fun getLevelChangeK(): Float {
        return ((if (habits[index].changeLevel) 1f else 0f)
                + (if (habits[index].changeNeedGoalWithLevel) 1f else 0f)
                + (if (habits[index].changeNeedDaysWithLevel) 1f else 0f)) / 3f
    }

    return Color(
        ((getProgressK() + getLevelK() + getNeedDaysK()) / 3 * 255).toInt(),
        ((getDaysK() + getNeedGoalK() + getLevelChangeK()) / 3 * 255).toInt(),
        ((getStreakK() + getTypeOfGoalK()) / 2 * 255).toInt(),
    )
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