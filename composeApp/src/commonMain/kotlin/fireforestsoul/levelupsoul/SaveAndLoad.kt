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
import kotlinx.datetime.LocalDate

expect fun saveValue(value: Any, name: String)
fun Any.savedElementToString(): String {
    return when (val value = this) {
        is Color -> value.value.toString(16)
        else -> value.toString()
    }
}

fun saveAllValues() {
    saveValue(app_version, "app_version")
    saveValue(habits.size, "habits-size")
    for (x in 0 until habits.size) {
        saveValue(habits[x].nameOfHabit, "habits-$x-nameOfHabit")
        saveValue(habits[x].nameOfUnitsOfDimension, "habits-$x-nameOfUnitsOfDimension")
        saveValue(habits[x].typeOfGoalHabits, "habits-$x-typeOfGoalHabits")
        saveValue(habits[x].needGoal, "habits-$x-needGoal")
        saveValue(habits[x].needDays, "habits-$x-needDays")
        saveValue(habits[x].typeOfColorHabits, "habits-$x-typeOfColorHabits")
        saveValue(habits[x].colorGood, "habits-$x-colorGood")
        saveValue(habits[x].changeLevel, "habits-$x-changeLevel")
        saveValue(habits[x].changeNeedGoalWithLevel, "habits-$x-changeNeedGoalWithLevel")
        saveValue(habits[x].changeNeedDaysWithLevel, "habits-$x-changeNeedDaysWithLevel")
        saveValue(habits[x].startDate, "habits-$x-startDate")
        saveValue(habits[x].lastLevelChangeDate, "habits-$x-lastLevelChangeDate")
        saveValue(habits[x].level, "habits-$x-level")
        saveValue(habits[x].iconChar, "habits-$x-iconChar")
        saveValue(habits[x].habitDay.size, "habits-$x-habitDay-size")
        for (y in 0 until habits[x].habitDay.size) {
            saveValue(habits[x].habitDay[y].today, "habits-$x-habitDay-$y-today")
            saveValue(habits[x].habitDay[y].totalOfAPeriod, "habits-$x-habitDay-$y-totalOfAPeriod")
            saveValue(habits[x].habitDay[y].correctly, "habits-$x-habitDay-$y-correctly")
        }
    }
    saveValue(soul_color_type, "soul_color_type")
    saveValue(soul_color, "soul_color")
    saveValue(soul_name, "soul_name")
    saveValue(soul_level, "soul_level")
    saveValue(soul_last_level_change_date, "soul_last_level_change_date")
    saveValue(language, "language")
    saveValue(withExponent, "withExponent")
}

expect fun <T> loadValue(value: T, name: String): T
expect fun old1001000000LoadAllValues()

fun <T> String.loadedElementToVal(value: T): T {
    val element = this
    return when (value) {
        is Long -> element.toLongOrNull() ?: value
        is Int -> element.toIntOrNull() ?: value
        is String -> element
        is TypeOfGoalHabits -> enumValueOf<TypeOfGoalHabits>(element)
        is BigDecimal -> element.toBigDecimal()
        is TypeOfColorHabits -> enumValueOf<TypeOfColorHabits>(element)
        is Color -> Color(element.toULongOrNull(16) ?: "ffffffff00000000".toULong(16))
        is Boolean -> element.toBoolean()
        is LocalDate -> element.let { LocalDate.parse(it) }
        is Languages -> enumValueOf<Languages>(element)
        else -> value
    } as T
}

fun loadAllValues() {
    old1001000000LoadAllValues()
    val oldAppVersion = loadValue(app_version, "app_version")
    val habitsSize = loadValue(habits.size, "habits-size")
    habits = mutableListOf(Habit())
    for (x in 0 until habitsSize) {
        habits[x].nameOfHabit = loadValue(habits[x].nameOfHabit, "habits-$x-nameOfHabit")
        habits[x].nameOfUnitsOfDimension =
            loadValue(habits[x].nameOfUnitsOfDimension, "habits[x]s-$x-nameOfUnitsOfDimension")
        habits[x].typeOfGoalHabits = loadValue(habits[x].typeOfGoalHabits, "habits-$x-typeOfGoalHabits")
        habits[x].needGoal = loadValue(habits[x].needGoal, "habits-$x-needGoal")
        habits[x].needDays = loadValue(habits[x].needDays, "habits-$x-needDays")
        habits[x].typeOfColorHabits = loadValue(habits[x].typeOfColorHabits, "habits-$x-typeOfColorHabits")
        habits[x].colorGood = loadValue(habits[x].colorGood, "habits-$x-colorGood")
        habits[x].changeLevel = loadValue(habits[x].changeLevel, "habits-$x-changeLevel")
        habits[x].changeNeedGoalWithLevel =
            loadValue(habits[x].changeNeedGoalWithLevel, "habits-$x-changeNeedGoalWithLevel")
        habits[x].changeNeedDaysWithLevel =
            loadValue(habits[x].changeNeedDaysWithLevel, "habits-$x-changeNeedDaysWithLevel")
        habits[x].startDate = loadValue(habits[x].startDate, "habits-$x-startDate")
        habits[x].lastLevelChangeDate = loadValue(habits[x].lastLevelChangeDate, "habits-$x-lastLevelChangeDate")
        habits[x].level = loadValue(habits[x].level, "habits-$x-level")
        habits[x].iconChar = loadValue(habits[x].iconChar, "habits-$x-iconChar")
        val habitDaySize = loadValue(habits[x].habitDay.size, "habits-$x-habitDay-size")
        habits[x].habitDay = mutableListOf(HabitDay())
        for (y in 0 until habitDaySize) {
            habits[x].habitDay[y].today = loadValue(habits[x].habitDay[y].today, "habits-$x-habitDay-$y-today")
            habits[x].habitDay[y].totalOfAPeriod =
                loadValue(habits[x].habitDay[y].totalOfAPeriod, "habits-$x-habitDay-$y-totalOfAPeriod")
            habits[x].habitDay[y].correctly =
                loadValue(habits[x].habitDay[y].correctly, "habits-$x-habitDay-$y-correctly")
            if (y != habitDaySize - 1) habits[x].habitDay.add(HabitDay())
        }
        if (x != habitsSize - 1) habits.add(Habit())
    }
    soul_color_type = loadValue(soul_color_type, "soul_color_type")
    soul_color = loadValue(soul_color, "soul_color")
    soul_name = loadValue(soul_name, "soul_name")
    soul_level = loadValue(soul_level, "soul_level")
    soul_last_level_change_date = loadValue(soul_last_level_change_date, "soul_last_level_change_date")
    language = loadValue(language, "language")
    withExponent = loadValue(withExponent, "withExponent")
}