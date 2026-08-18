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
import fireforestsoul.levelupsoul.HelpOldSaveSystem.loadValue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

expect object HelpOldSaveSystem {
    fun <T> loadValue(value: T, name: String): T
}

object OldSaveSystem {
    @Serializable
    class OldHabit(
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
        var habitDay: MutableList<OldHabitDay> = MutableList(1) { OldHabitDay(0.toBigDecimal()) }

        @Serializable(with = BigDecimalAsStringSerializer::class)
        var phantomPeriodForGoalCompletionWithLevel: BigDecimal = periodForGoalCompletion.toBigDecimal()

        fun toHabit(): Habit {
            val habit = Habit()
            habit.nameOfHabit = nameOfHabit
            habit.nameOfUnitsOfDimension = nameOfUnitsOfDimension
            habit.numericalGoal = numericalGoal
            habit.typeOfGoal = typeOfGoal
            habit.periodForGoalCompletion = periodForGoalCompletion
            habit.changeLevel = changeLevel
            habit.typeOfColor = typeOfColor
            habit.color = color
            habit.changeLevel = changeLevel
            habit.changeNumericalGoalWithLevel = changeNumericalGoalWithLevel
            habit.changePeriodForGoalCompletionWithLevel = changePeriodForGoalCompletionWithLevel
            habit.icon = icon
            habit.lastLevelChangeDate = lastLevelChangeDate
            habit.level = level
            habit.habitDay =
                habitDay.mapIndexed { index, day -> startDate.plusDays(index) to day.toHabitDay() }.toMap(hashMapOf())
            habit.phantomPeriodForGoalCompletionWithLevel = phantomPeriodForGoalCompletionWithLevel
            return habit
        }

        fun loadNeedDays(value: Int) {
            periodForGoalCompletion = value
            phantomPeriodForGoalCompletionWithLevel = value.toBigDecimal()
        }
    }

    @Serializable
    class OldHabitDay(@Serializable(with = BigDecimalAsStringSerializer::class) var today: BigDecimal = 0.0.toBigDecimal()) {
        @Serializable(with = BigDecimalAsStringSerializer::class)
        var totalOfAPeriod: BigDecimal = 0.toBigDecimal()
        var correctly: Boolean = false

        fun toHabitDay(): HabitDay {
            val habitDay = HabitDay()
            habitDay.today = today
            return habitDay
        }
    }

    fun <T> String.loadedElementToVal(value: T): T {
        val element = this
        return when (value) {
            is Long -> element.toLongOrNull() ?: value
            is Int -> element.toIntOrNull() ?: value
            is String -> element
            is TypeOfGoalHabit -> enumValueOf<TypeOfGoalHabit>(element)
            is BigDecimal -> element.toBigDecimal()
            is TypeOfColorHabit -> enumValueOf<TypeOfColorHabit>(element)
            is Color -> Color(element.toULongOrNull(16) ?: "ffffffff00000000".toULong(16))
            is Boolean -> element.toBoolean()
            is LocalDate -> element.let { LocalDate.parse(it) }
            is Languages -> enumValueOf<Languages>(element)
            is AppStatus -> enumValueOf<AppStatus>(element)
            is Float -> element.toFloatOrNull() ?: value
            else -> value
        } as T
    }

    private var habits_: MutableList<OldHabit> = mutableListOf(
        OldHabit()
    )
    private var soul_color_type_: TypeOfColorHabit = TypeOfColorHabit.ADAPTIVE
    private var soul_color_: Color = Color(200, 200, 200)
    private var soul_name_: String = ts_Mr_Soul_Forest
    private var soul_level_: Int = 0
    private var soul_last_level_change_date_: LocalDate =
        kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private var language_: Languages = Languages.EN
    private var withExponent_: Boolean = false
    private var backAppStatus_: AppStatus = AppStatus.TABLE
    private var listPointsOfHabitStatistic_: MutableMap<HabitStatisticsStatus, Float> = mutableMapOf(
        HabitStatisticsStatus.GOAL to 0f,
        HabitStatisticsStatus.PROGRESS to 0f,
        HabitStatisticsStatus.LEVEL to 0f,
        HabitStatisticsStatus.PROGRESS_GRAPH to 0f,
        HabitStatisticsStatus.BAR_CHART to 0f,
        HabitStatisticsStatus.CALENDAR to 0f,
        HabitStatisticsStatus.DISTRIBUTION_BY_DAY_OF_THE_WEEK to 0f,
        HabitStatisticsStatus.STREAKS to 0f
    )
    private var sort_habit_statistics_sections_by_frequency_of_use_: Boolean = false
    private var smart_sort_: Boolean = false
    private var oldAppVersion = -1

    private fun loadAllValues() {
        oldAppVersion = loadValue(-1, "app_version")
        if (oldAppVersion > 1001000000) {
            oldAppVersion = loadValue(-1, "app_version")
            val habitsSize = loadValue(habits_.size, "habits-size")
            habits_ = mutableListOf(OldHabit())
            for (x in 0 until habitsSize) {
                habits_[x].nameOfHabit = loadValue(habits_[x].nameOfHabit, "habits-$x-nameOfHabit")
                habits_[x].nameOfUnitsOfDimension =
                    loadValue(habits_[x].nameOfUnitsOfDimension, "habits-$x-nameOfUnitsOfDimension")
                habits_[x].typeOfGoal = loadValue(habits_[x].typeOfGoal, "habits-$x-typeOfGoalHabits")
                habits_[x].numericalGoal = loadValue(habits_[x].numericalGoal, "habits-$x-needGoal")
                habits_[x].loadNeedDays(loadValue(habits_[x].periodForGoalCompletion, "habits-$x-needDays"))
                habits_[x].typeOfColor = loadValue(habits_[x].typeOfColor, "habits-$x-typeOfColorHabits")
                habits_[x].color = loadValue(habits_[x].color, "habits-$x-colorGood")
                habits_[x].changeLevel = loadValue(habits_[x].changeLevel, "habits-$x-changeLevel")
                habits_[x].changeNumericalGoalWithLevel =
                    loadValue(habits_[x].changeNumericalGoalWithLevel, "habits-$x-changeNeedGoalWithLevel")
                habits_[x].changePeriodForGoalCompletionWithLevel =
                    loadValue(habits_[x].changePeriodForGoalCompletionWithLevel, "habits-$x-changeNeedDaysWithLevel")
                habits_[x].startDate = loadValue(habits_[x].startDate, "habits-$x-startDate")
                habits_[x].lastLevelChangeDate =
                    loadValue(habits_[x].lastLevelChangeDate, "habits-$x-lastLevelChangeDate")
                habits_[x].level = loadValue(habits_[x].level, "habits-$x-level")
                habits_[x].icon = loadValue(habits_[x].icon, "habits-$x-iconChar")
                if (oldAppVersion > 1001001000) {
                    habits_[x].phantomPeriodForGoalCompletionWithLevel =
                        loadValue(habits_[x].phantomPeriodForGoalCompletionWithLevel, "habits-$x-phantomNeedDays")
                }
                val habitDaySize = loadValue(habits_[x].habitDay.size, "habits-$x-habitDay-size")
                habits_[x].habitDay = mutableListOf(OldHabitDay())
                for (y in 0 until habitDaySize) {
                    habits_[x].habitDay[y].today =
                        loadValue(habits_[x].habitDay[y].today, "habits-$x-habitDay-$y-today")
                    habits_[x].habitDay[y].totalOfAPeriod =
                        loadValue(habits_[x].habitDay[y].totalOfAPeriod, "habits-$x-habitDay-$y-totalOfAPeriod")
                    habits_[x].habitDay[y].correctly =
                        loadValue(habits_[x].habitDay[y].correctly, "habits-$x-habitDay-$y-correctly")
                    if (y != habitDaySize - 1) habits_[x].habitDay.add(OldHabitDay())
                }
                if (x != habitsSize - 1) habits_.add(OldHabit())
            }
            soul_color_type_ = loadValue(soul_color_type_, "soul_color_type")
            soul_color_ = loadValue(soul_color_, "soul_color")
            soul_name_ = loadValue(soul_name_, "soul_name")
            soul_level_ = loadValue(soul_level_, "soul_level")
            soul_last_level_change_date_ = loadValue(soul_last_level_change_date_, "soul_last_level_change_date")
            language_ = loadValue(language_, "language")
            withExponent_ = loadValue(withExponent_, "withExponent")
            backAppStatus_ = loadValue(backAppStatus_, "backAppStatus")
            if (oldAppVersion >= 1001005000) {
                HabitStatisticsStatus.entries.forEach { status ->
                    listPointsOfHabitStatistic_[status] = loadValue(
                        listPointsOfHabitStatistic_[status] ?: 0f,
                        "listPointsOfHabitStatistic-HabitStatisticsStatus-${status.name}"
                    )
                }
                sort_habit_statistics_sections_by_frequency_of_use_ = loadValue(
                    sort_habit_statistics_sections_by_frequency_of_use_,
                    "sort_habit_statistics_sections_by_frequency_of_use"
                )

                if (oldAppVersion >= 1002000000) {
                    smart_sort_ = loadValue(smart_sort_, "smart_sort")
                }
            }
        }
    }

    fun loadToLocalData(): LocalData {
        loadAllValues()
        val data = LocalData()
        if (oldAppVersion >= 1001000000) {
            data.habits = habits_.map {
                val tmp = it.toHabit()
                tmp.clearOfDefaults()
                tmp
            }.toMutableList()
            data.soulColorType = soul_color_type_
            data.soulColor = soul_color_
            data.soulName = soul_name_
            data.soulLevel = soul_level_
            data.soulLastLevelChangeDate = soul_last_level_change_date_
            data.language = language_
            data.withExponent = withExponent_
            data.backAppStatus = backAppStatus_
            data.listPointsOfHabitStatistic = listPointsOfHabitStatistic_
            data.sortHabitStatisticsSectionsByFrequencyOfUse =
                sort_habit_statistics_sections_by_frequency_of_use_
            data.smartSort = smart_sort_
        }
        return data
    }
}