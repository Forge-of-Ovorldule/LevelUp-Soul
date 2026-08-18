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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.downTo
import kotlinx.datetime.minus
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

    var lastLevelChangeDate: LocalDate = dateNow()
    var level: Int = 0
    var habitDay: HashMap<LocalDate, HabitDay> = hashMapOf()

    @Serializable(with = BigDecimalAsStringSerializer::class)
    var phantomPeriodForGoalCompletionWithLevel: BigDecimal = periodForGoalCompletion.toBigDecimal()

    fun clearOfDefaults() {
        habitDay.entries.removeAll {
            it.value == BigDecimal.ZERO
        }
    }

    fun totalOfAPeriod(toDate: LocalDate): BigDecimal {
        var sum = BigDecimal.ZERO
        for (i in toDate.minus(periodForGoalCompletion - 1, DateTimeUnit.DAY)..toDate) {
            sum += habitDay[i]?.today ?: BigDecimal.ZERO
        }
        return sum
    }

    fun correctly(toDate: LocalDate): Boolean {
        return when (typeOfGoal) {
            TypeOfGoalHabit.AT_LEAST -> totalOfAPeriod(toDate) >= numericalGoal
            TypeOfGoalHabit.NO_MORE -> totalOfAPeriod(toDate) <= numericalGoal
        }
    }

    fun update(sortedHabits: MutableList<Int> = mutableListOf()) {
        if (sortedHabits.isNotEmpty()) {
            sortedHabits.sortSystem()
        }

        if (changeLevel) {
            changeLvl()
        }
    }

    private fun minDate(): LocalDate = habitDay.keys.minOrNull() ?: dateNow()
    fun startDate(): LocalDate = minDate()
    fun totalDays(): Int = dateNow().minus(startDate()).days + 1

    private fun changeLvl() {
        if (dateNow().toEpochDays() - lastLevelChangeDate.toEpochDays() >= 20
        ) {
            var goodProgress = 0
            if (progress(this) >= 0.8) {
                for (x in (dateNow().minus(19, DateTimeUnit.DAY))..dateNow()) {
                    if (progress(this, toDate = x) >= 0.8) {
                        goodProgress++
                    }
                }
                if (goodProgress == 20) {
                    lvlUp()
                }
            } else if (progress(this) <= 0.2) {
                for (day in dateNow().minusDays(19)..dateNow()) {
                    if (progress(this, toDate = day) <= 0.2) {
                        goodProgress++
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

    fun getToLevelUp(daysToCalculateAverage: Int = totalDays()): Float {
        val isProgressUp = if (progress(this, daysToCalculateAverage) <= 0.2f) false else (
                if (progress(this, daysToCalculateAverage) >= 0.8f) true else return 0f
                )

        var progress = 0

        for (day in dateNow() downTo dateNow().minusDays(19)) {
            if (isProgressUp) {
                if (progress(this, daysToCalculateAverage, day) >= 0.8f) progress++
                else return progress.toFloat() / 20f
            } else {
                if (progress(this, daysToCalculateAverage, day) <= 0.2f) progress--
                else return progress.toFloat() / 20f
            }
        }
        return 1f
    }

    fun getNeedGoalWhenNewLevel(
        daysToCalculateAverage: Int = totalDays(),
        isProgressUp: Boolean = if (progress(this, daysToCalculateAverage) <= 0.2f) false else true
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
        daysToCalculateAverage: Int = totalDays(),
        isProgressUp: Boolean = if (progress(this, daysToCalculateAverage) <= 0.2f) false else true
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
        daysToCalculateAverage: Int = totalDays(),
        isProgressUp: Boolean = if (progress(this, daysToCalculateAverage) <= 0.2f) false else true
    ): Int {
        if (changePeriodForGoalCompletionWithLevel) {
            return if (getPhantomNeedDaysWhenNewLevel(
                    daysToCalculateAverage,
                    isProgressUp
                ) - getPhantomNeedDaysWhenNewLevel(
                    daysToCalculateAverage,
                    isProgressUp
                ).intValue(false) != BigDecimal.ZERO
            )
                getPhantomNeedDaysWhenNewLevel(daysToCalculateAverage, isProgressUp).intValue(false) + 1
            else
                getPhantomNeedDaysWhenNewLevel(daysToCalculateAverage, isProgressUp).intValue(false)
        }
        return periodForGoalCompletion
    }

    fun progress(
        daysToCalculateAverage: Int = totalDays(),
        toDate: LocalDate = dateNow()
    ): Float {
        if (daysToCalculateAverage <= 0) {
            return 0f
        }

        var correctly = 0
        for (day in toDate.minus(daysToCalculateAverage - 1, DateTimeUnit.DAY)..toDate) {
            if (correctly(day)) correctly++
        }
        return correctly.toFloat() / daysToCalculateAverage
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