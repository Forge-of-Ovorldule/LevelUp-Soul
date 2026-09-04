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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.daysUntil
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

    var priority : Priority = Priority.NO_PRIORITY

    fun clearOfDefaults() {
        habitDay.entries.removeAll { it.value.today == BigDecimal.ZERO }
    }

    fun setDayValue(date: LocalDate, value: BigDecimal) {
        if (value == BigDecimal.ZERO) {
            habitDay.remove(date)
        } else {
            habitDay[date] = HabitDay(value)
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
    fun totalDays(): Int = startDate().daysUntil(dateNow()) + 1

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
        return progress.toFloat() / 20f
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

    var progressiveColorCache = color

    fun habitStreaks(): List<Int> {
        val list = mutableListOf(0)
        var add = 0
        for (day in startDate()..dateNow()) {
            if (correctly(day)) add++
            else {
                list.add(add)
                add = 0
            }
        }
        list.add(add)

        list.removeAll { it == 0 }
        list.sortDescending()
        return list
    }

    suspend fun calculateProgressiveColor(
        onColorUpdate: (Color) -> Unit,
    ) {
        if (typeOfColor == TypeOfColorHabit.SELECTED) {
            onColorUpdate(color)
            progressiveColorCache = color
            return
        }

        val kRed = progressiveColorCache.red
        var kProgress = kRed
        var kLevel = kRed
        var kNeedDays = kRed

        val kGreen = progressiveColorCache.green
        var kDays = kGreen
        var kNeedGoal = kGreen
        var kLevelChange = kGreen

        val kBlue = progressiveColorCache.blue
        var kStreak = kBlue
        var kTypeOfGoal = kBlue

        fun emitCurrentColor() {
            val red = ((kProgress + kLevel + kNeedDays) / 3 * 255).toInt().coerceIn(0, 255)
            val green = ((kDays + kNeedGoal + kLevelChange) / 3 * 255).toInt().coerceIn(0, 255)
            val blue = ((kStreak + kTypeOfGoal) / 2 * 255).toInt().coerceIn(0, 255)

            val curColor = Color(red, green, blue)
            onColorUpdate(curColor)
            progressiveColorCache = curColor
        }

        val addProcess = ts_Calculating_adaptive_color_habits

        withContext(Dispatchers.Default) {
            try {
                lock.withLock { listProgressedStatusBar.add(addProcess) }

                var maxProgress = Float.MIN_VALUE
                var minProgress = Float.MAX_VALUE
                for (habit in LocalSaveManager.data.habits) {
                    maxProgress = max(progress(habit), maxProgress)
                    minProgress = min(progress(habit), minProgress)
                }
                kProgress =
                    if (maxProgress == minProgress) 1f else (progress() - minProgress) / (if (maxProgress - minProgress == 0f) 1f else (maxProgress - minProgress))

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
                    else (totalDays() - minDays).toFloat() /
                            (if (maxDays - minDays == 0) 1f else (maxDays - minDays).toFloat())

                emitCurrentColor()
                yield()

                if (habitStreaks().isNotEmpty()) {
                    var maxStreak = Int.MIN_VALUE
                    val minStreak = 0
                    for (habit in LocalSaveManager.data.habits) {
                        val s = if (habitStreaks(habit).isNotEmpty()) habitStreaks(habit)[0] else 0
                        maxStreak = max(s, maxStreak)
                    }
                    kStreak =
                        if (maxStreak == minStreak) 1f else (habitStreaks()[0] - minStreak).toFloat() / (if (maxStreak - minStreak == 0) 1f else (maxStreak - minStreak).toFloat())
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
                    if (maxLevel == minLevel) 1f else (level - minLevel).toFloat() / (if (maxLevel - minLevel == 0) 1f else (maxLevel - minLevel).toFloat())

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
                    if (maxNeedGoal == minNeedGoal) 1f else (numericalGoal - minNeedGoal).floatValue(
                        false
                    ) / (if (diffGoal == BigDecimal.ZERO) 1f else diffGoal.floatValue(
                        false
                    ))

                emitCurrentColor()
                yield()

                kTypeOfGoal = when (typeOfGoal) {
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
                    if (maxNeedDays == minNeedDays) 1f else (periodForGoalCompletion - minNeedDays).toFloat() / (if (maxNeedDays - minNeedDays == 0) 1f else (maxNeedDays - minNeedDays).toFloat())

                emitCurrentColor()
                yield()

                kLevelChange = ((if (changeLevel) 1f else 0f)
                        + (if (changeNumericalGoalWithLevel) 1f else 0f)
                        + (if (changePeriodForGoalCompletionWithLevel) 1f else 0f)) / 3f

                emitCurrentColor()

            } finally {
                withContext(NonCancellable) {
                    lock.withLock {
                        listProgressedStatusBar.removeAll { it == addProcess }
                    }
                }
            }
        }
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