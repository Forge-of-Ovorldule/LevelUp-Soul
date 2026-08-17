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
import kotlinx.datetime.LocalDate
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.math.max
import kotlin.math.min

@Serializable
class Habit(
    var nameOfHabit: String = ts_New_habit,
    var nameOfUnitsOfDimension: String = ts_km,
    var typeOfGoal: TypeOfGoalHabit = TypeOfGoalHabit.AT_LEAST,
    @Serializable(with = BigDecimalAsStringSerializer::class) var numericalGoal: BigDecimal = BigDecimal.ONE,
    var periodForGoalCompletion: Int = 1,
    var typeOfColor: TypeOfColorHabit = TypeOfColorHabit.SELECTED,
    @Contextual var color: Color = UICT_see,
    var changeLevel: Boolean = true,
    var changeNumericalGoalWithLevel: Boolean = false,
    var changePeriodForGoalCompletionWithLevel: Boolean = false,
    var icon: String = ""
) {

    var startDate: LocalDate = dateNow()
    var lastLevelChangeDate: LocalDate = startDate
    var level: Int = 0
    var habitDay: MutableList<HabitDay> = MutableList(1) { HabitDay(0.toBigDecimal()) }

    @Serializable(with = BigDecimalAsStringSerializer::class)
    var phantomPeriodForGoalCompletionWithLevel: BigDecimal = periodForGoalCompletion.toBigDecimal()

    fun updateDate() {
        val today = dateNow()
        val addDays: Long = (today.toEpochDays() - startDate.toEpochDays() - habitDay.size + 1)

        if (addDays > 0) {
            habitDay.addAll(List(addDays.toInt()) { HabitDay(0.toBigDecimal()) })
        }

        update()
    }

    fun totalOfAPeriod(toId: Int): BigDecimal {
        var sum = BigDecimal.ZERO
        for (i in max(0, toId - periodForGoalCompletion + 1)..toId) {
            sum += habitDay[i].today
        }
        return sum
    }

    private fun updateHabitDay() {
    }

    fun update(sortedHabits: MutableList<Int> = mutableListOf()) {
        updateHabitDay()

        if (sortedHabits.isNotEmpty()) {
            sortedHabits.sortSystem()
        }

        if (changeLevel) {
            changeLvl()
        }
    }

    private fun changeLvl() {
        if (dateNow().toEpochDays() - lastLevelChangeDate.toEpochDays() >= 20
        ) {
            var goodProgress = 0
            if (progress(this) >= 0.8) {
                for (x in (habitDay.size - 20) until habitDay.size) {
                    if (x >= 0) {
                        if (progress(this, startIndex = x) >= 0.8) {
                            goodProgress++
                        }
                    }
                }
                if (goodProgress == 20) {
                    lvlUp()
                }
            } else if (progress(this) <= 0.2) {
                for (x in (habitDay.size - 20) until habitDay.size) {
                    if (x >= 0) {
                        if (progress(this, startIndex = x) <= 0.2) {
                            goodProgress++
                        }
                    }
                }
                if (goodProgress == 20) {
                    lvlDown()
                }
            }
        }
    }

    fun lvlUp() {
        level++
        lastLevelChangeDate = dateNow()
        if (changePeriodForGoalCompletionWithLevel) {
            when (typeOfGoal) {
                TypeOfGoalHabit.AT_LEAST -> phantomPeriodForGoalCompletionWithLevel *= "0.8".toBigDecimal()
                TypeOfGoalHabit.NO_MORE -> phantomPeriodForGoalCompletionWithLevel /= "0.8".toBigDecimal()
            }
            periodForGoalCompletion =
                if (phantomPeriodForGoalCompletionWithLevel == phantomPeriodForGoalCompletionWithLevel.intValue(false)
                        .toBigDecimal()
                ) phantomPeriodForGoalCompletionWithLevel.intValue(false) else phantomPeriodForGoalCompletionWithLevel.intValue(
                    false
                ) + 1
        }
        if (changeNumericalGoalWithLevel) {
            when (typeOfGoal) {
                TypeOfGoalHabit.AT_LEAST -> numericalGoal /= "0.8".toBigDecimal()
                TypeOfGoalHabit.NO_MORE -> numericalGoal *= "0.8".toBigDecimal()
            }
        }
    }

    fun lvlDown() {
        level--
        lastLevelChangeDate = dateNow()
        if (changePeriodForGoalCompletionWithLevel) {
            when (typeOfGoal) {
                TypeOfGoalHabit.AT_LEAST -> phantomPeriodForGoalCompletionWithLevel /= 0.8
                TypeOfGoalHabit.NO_MORE -> phantomPeriodForGoalCompletionWithLevel *= "0.8".toBigDecimal()
            }
            periodForGoalCompletion =
                if (phantomPeriodForGoalCompletionWithLevel == phantomPeriodForGoalCompletionWithLevel.intValue(false)
                        .toBigDecimal()
                ) phantomPeriodForGoalCompletionWithLevel.intValue(false) else phantomPeriodForGoalCompletionWithLevel.intValue(
                    false
                ) + 1
        }
        if (changeNumericalGoalWithLevel) {
            when (typeOfGoal) {
                TypeOfGoalHabit.AT_LEAST -> numericalGoal *= "0.8".toBigDecimal()
                TypeOfGoalHabit.NO_MORE -> numericalGoal /= "0.8".toBigDecimal()
            }
        }
    }

    fun getToLevelUp(pps: Int = habitDay.size - 1): Float {
        var end = habitDay.size - 20
        end = if (end < 0) 0 else end
        end =
            (if (end < lastLevelChangeDate.toEpochDays() - startDate.toEpochDays()) lastLevelChangeDate.toEpochDays() - startDate.toEpochDays() else end).toInt()

        var progress = 0f
        val isProgressUp = if (progress(this, pps) <= 0.2f) false else true
        for (index in (habitDay.size - 1) downTo end) {
            if (isProgressUp) {
                if (progress(this, pps, index) >= 0.8f) progress++
                else break
            } else {
                if (progress(this, pps, index) <= 0.2f) progress--
                else break
            }
        }
        return progress / 20f
    }

    fun getNeedGoalWhenNewLevel(
        pps: Int = habitDay.size - 1,
        isProgressUp: Boolean = if (progress(this, pps) <= 0.2f) false else true
    ): BigDecimal {
        if (changeNumericalGoalWithLevel) {
            return if (isProgressUp) {
                when (typeOfGoal) {
                    TypeOfGoalHabit.AT_LEAST -> numericalGoal / "0.8".toBigDecimal()
                    TypeOfGoalHabit.NO_MORE -> numericalGoal * "0.8".toBigDecimal()
                }
            } else {
                when (typeOfGoal) {
                    TypeOfGoalHabit.AT_LEAST -> numericalGoal * "0.8".toBigDecimal()
                    TypeOfGoalHabit.NO_MORE -> numericalGoal / "0.8".toBigDecimal()
                }
            }
        }
        return numericalGoal
    }

    fun getPhantomNeedDaysWhenNewLevel(
        pps: Int = habitDay.size - 1,
        isProgressUp: Boolean = if (progress(this, pps) <= 0.2f) false else true
    ): BigDecimal {
        if (changePeriodForGoalCompletionWithLevel) {
            return if (isProgressUp) {
                when (typeOfGoal) {
                    TypeOfGoalHabit.AT_LEAST -> phantomPeriodForGoalCompletionWithLevel * "0.8".toBigDecimal()
                    TypeOfGoalHabit.NO_MORE -> phantomPeriodForGoalCompletionWithLevel / "0.8".toBigDecimal()
                }
            } else {
                when (typeOfGoal) {
                    TypeOfGoalHabit.AT_LEAST -> phantomPeriodForGoalCompletionWithLevel / 0.8
                    TypeOfGoalHabit.NO_MORE -> phantomPeriodForGoalCompletionWithLevel * "0.8".toBigDecimal()
                }
            }
        }
        return phantomPeriodForGoalCompletionWithLevel
    }

    fun getNeedDaysWhenNewLevel(
        pps: Int = habitDay.size - 1,
        isProgressUp: Boolean = if (progress(this, pps) <= 0.2f) false else true
    ): Int {
        if (changePeriodForGoalCompletionWithLevel) {
            return if (getPhantomNeedDaysWhenNewLevel(pps, isProgressUp) - getPhantomNeedDaysWhenNewLevel(
                    pps,
                    isProgressUp
                ).intValue(false) != BigDecimal.ZERO
            )
                getPhantomNeedDaysWhenNewLevel(pps, isProgressUp).intValue(false) + 1
            else
                getPhantomNeedDaysWhenNewLevel(pps, isProgressUp).intValue(false)
        }
        return periodForGoalCompletion
    }
}

fun MutableList<Int>.sortSystem() {
    this.sortByDescending { if (habitStreaks(it).isNotEmpty()) habitStreaks(it)[0] else 0 }
    if (LocalSaveManager.data.smartSort) {
        var maxLevel = Int.MIN_VALUE
        var minLevel = Int.MAX_VALUE
        for (habit in LocalSaveManager.data.habits) {
            maxLevel = max(habit.level, maxLevel)
            minLevel = min(habit.level, minLevel)
        }
        if (maxLevel != minLevel)
            this.sortByDescending {
                val kLevel =
                    (LocalSaveManager.data.habits[it].level - minLevel).toFloat() / (maxLevel - minLevel).toFloat()
                kLevel + progress(it)
            }
        else
            this.sortByDescending { progress(it) }
    } else {
        this.sortByDescending { LocalSaveManager.data.habits[it].level }
        this.sortByDescending { progress(it) }
    }
}