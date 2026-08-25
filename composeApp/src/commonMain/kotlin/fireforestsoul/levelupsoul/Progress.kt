/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.datetime.*

fun progress(
    index: Int,
    daysToCalculateAverage: Int = LocalSaveManager.data.habits[index].totalDays(),
    toDate: LocalDate = dateNow()
): Float {
    return LocalSaveManager.data.habits[index].progress(daysToCalculateAverage, toDate)
}

fun progress(
    habit: Habit,
    daysToCalculateAverage: Int = habit.totalDays(),
    toDate: LocalDate = dateNow()
): Float {
    return habit.progress(daysToCalculateAverage, toDate)
}

fun progressAll(
    maxDays: Int,
    daysToCalculateAverage: Int = maxDays,
    toDate: LocalDate = dateNow()
): Float {
    if (LocalSaveManager.data.habits.isEmpty() || daysToCalculateAverage <= 0) return 0f

    var correctly = 0f
    for (habit in LocalSaveManager.data.habits) {
        correctly += habit.progress(
            if (daysToCalculateAverage >= maxDays) habit.totalDays() else daysToCalculateAverage,
            toDate
        )
    }
    return correctly / LocalSaveManager.data.habits.size
}

fun plusProgress(
    index: Int,
    period: Int,
    daysToCalculateAverage: Int = LocalSaveManager.data.habits[index].totalDays(),
    toDate: LocalDate = dateNow()
): Float {
    if (index < 0 || index >= LocalSaveManager.data.habits.size || period <= 0 || daysToCalculateAverage <= 0) return 0f

    return progress(index, daysToCalculateAverage, toDate) -
            progress(
                index,
                daysToCalculateAverage,
                toDate.minusDays(period)
            )
}

fun plusProgressAll(
    maxDays: Int,
    period: Int,
    days: Int = maxDays,
    toDate: LocalDate = dateNow()
): Float {
    if (maxDays <= 0 || period <= 0 || days <= 0) return 0f

    return progressAll(maxDays, days, toDate) -
            progressAll(
                maxDays,
                days,
                toDate.minusDays(period)
            )
}

fun listProgress(
    habitIndex: Int,
    period: Int,
    step: Int,
    daysToCalculateAverage: Int = LocalSaveManager.data.habits[habitIndex].totalDays()
): List<Float> {
    if (period <= 0 || step <= 0 || daysToCalculateAverage <= 0) return emptyList()

    val list = mutableListOf<Float>()

    var sum = 0f
    var n = 0
    var i = 0
    for (day in dateNow() downTo dateNow().minusDays(period - 1)) {
        sum += progress(habitIndex, daysToCalculateAverage, day)
        n++
        i++
        if (i % step == 0 || day == dateNow().minusDays(period - 1)) {
            list.add(sum / n)
            sum = 0f
            n = 0
        }
    }

    list.reverse()
    return list
}

fun listProgressAll(
    maxDays: Int,
    period: Int,
    step: Int,
    days: Int = maxDays
): List<Float> {
    if (period <= 0 || step <= 0 || days <= 0 || maxDays <= 0) return emptyList()

    var curDay = dateNow().minusDays(period - 1)
    val list = mutableListOf(progressAll(maxDays, days, curDay))
    curDay = curDay.plusDays(step)
    while (curDay < dateNow()) {
        list.add(progressAll(maxDays, days, curDay))
        curDay = curDay.plusDays(step)
    }
    return list
}

fun listToday(
    habitIndex: Int,
    step: Int,
    startDate: LocalDate = LocalSaveManager.data.habits[habitIndex].startDate()
): List<BigDecimal> {
    if (habitIndex < 0 || habitIndex >= LocalSaveManager.data.habits.size || step <= 0) {
        return emptyList()
    }

    val list = mutableListOf<BigDecimal>()

    var sum = BigDecimal.ZERO
    val habit = LocalSaveManager.data.habits[habitIndex]

    for ((i, day) in (dateNow() downTo startDate).withIndex()) {
        sum += habit.habitDay[day]?.today ?: BigDecimal.ZERO
        if (i % step == 0 || day == startDate) {
            list.add(sum)
            sum = BigDecimal.ZERO
        }
    }

    list.reverse()
    return list
}

fun listTodayAll(
    maxDays: Int,
    step: Int
): List<BigDecimal> {
    if (maxDays <= 0 || step <= 0) return emptyList()

    val list = mutableListOf<BigDecimal>()
    var sum = BigDecimal.ZERO

    val habits = LocalSaveManager.data.habits
    val startDate = dateNow().minusDays(maxDays - 1)

    for ((i, day) in (startDate..dateNow()).withIndex()) {
        habits.forEach { habit ->
            sum += if (habit.correctly(day)) BigDecimal.ONE else BigDecimal.ZERO
        }

        if ((i + 1) % step == 0 || day == dateNow()) {
            list.add(sum)
            sum = BigDecimal.ZERO
        }
    }

    return list
}

fun listTodayDates(
    habitIndex: Int,
    step: Int
): List<String> {
    fun LocalDate.formatter(): String {
        return if (step < 7) this.dayOfWeek.toString().take(3)
        else if (step < 30) day.toString()
        else if (step < 365) this.month.toString().take(3)
        else this.year.toString()
    }

    val list = mutableListOf<String>()

    val habit = LocalSaveManager.data.habits[habitIndex]

    for ((i, day) in (dateNow() downTo habit.startDate()).withIndex()) {
        if (i % step == 0 || day == habit.startDate()) {
            list.add(day.formatter())
        }
    }

    list.reverse()
    return list
}

fun listTodayDatesAll(
    maxDays: Int,
    step: Int
): List<String> {
    fun formatter(localDate: LocalDate): String {
        return if (step < 7) localDate.dayOfWeek.toString().take(3)
        else if (step < 30) localDate.day.toString()
        else if (step < 365) localDate.month.toString().take(3)
        else localDate.year.toString()
    }

    var oldestStartDate = LocalDate(9999, 1, 1)
    for (habit in LocalSaveManager.data.habits) {
        oldestStartDate = if (oldestStartDate < habit.startDate()) oldestStartDate else habit.startDate()
    }

    val list = mutableListOf(formatter(oldestStartDate))
    var y = step
    while (y < maxDays) {
        list.add(formatter(oldestStartDate.plus(y, DateTimeUnit.DAY)))
        y += step
    }
    return list
}

fun listDaysNumbers(
    index: Int
): List<Int> {
    val list = mutableListOf<Int>()
    for (day in LocalSaveManager.data.habits[index].startDate()..dateNow()) {
        list.add(day.day)
    }
    return list
}

fun listDaysNumbers(
    habit: Habit
): List<Int> {
    val list = mutableListOf<Int>()
    for (day in habit.startDate()..dateNow()) {
        list.add(day.day)
    }
    return list
}

fun listDaysBoolean(
    index: Int
): List<Boolean> {
    val list = mutableListOf<Boolean>()
    for (day in LocalSaveManager.data.habits[index].startDate()..dateNow()) {
        list.add(LocalSaveManager.data.habits[index].correctly(day))
    }
    return list
}

fun habitStreaks(
    index: Int
): List<Int> {
    val list = mutableListOf(0)
    var add = 0
    for (day in LocalSaveManager.data.habits[index].startDate()..dateNow()) {
        if (LocalSaveManager.data.habits[index].correctly(day)) add++
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

fun habitStreaks(
    habit: Habit
): List<Int> {
    val list = mutableListOf(0)
    var add = 0
    for (day in habit.startDate()..dateNow()) {
        if (habit.correctly(day)) add++
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

data class DistributionByDayOfTheWeekContent(var dayOfWeek: String, var value: BigDecimal)

fun habitDistributionByDayOfTheWeekContent(
    habitIndex: Int,
    sorted: Boolean = false
): List<DistributionByDayOfTheWeekContent> {
    var monday = BigDecimal.ZERO
    var tuesday = BigDecimal.ZERO
    var wednesday = BigDecimal.ZERO
    var thursday = BigDecimal.ZERO
    var friday = BigDecimal.ZERO
    var saturday = BigDecimal.ZERO
    var sunday = BigDecimal.ZERO

    for (day in LocalSaveManager.data.habits[habitIndex].startDate()..dateNow()) {
        val decimal = LocalSaveManager.data.habits[habitIndex].habitDay[day]?.today ?: BigDecimal.ZERO

        when (day.dayOfWeek) {
            DayOfWeek.MONDAY -> monday += decimal
            DayOfWeek.TUESDAY -> tuesday += decimal
            DayOfWeek.WEDNESDAY -> wednesday += decimal
            DayOfWeek.THURSDAY -> thursday += decimal
            DayOfWeek.FRIDAY -> friday += decimal
            DayOfWeek.SATURDAY -> saturday += decimal
            DayOfWeek.SUNDAY -> sunday += decimal
        }
    }

    val list = mutableListOf(
        DistributionByDayOfTheWeekContent(ts_Monday, monday),
        DistributionByDayOfTheWeekContent(ts_Tuesday, tuesday),
        DistributionByDayOfTheWeekContent(ts_Wednesday, wednesday),
        DistributionByDayOfTheWeekContent(ts_Thursday, thursday),
        DistributionByDayOfTheWeekContent(ts_Friday, friday),
        DistributionByDayOfTheWeekContent(ts_Saturday, saturday),
        DistributionByDayOfTheWeekContent(ts_Sunday, sunday)
    )
    if (sorted)
        list.sortByDescending { it.value }
    return list
}
