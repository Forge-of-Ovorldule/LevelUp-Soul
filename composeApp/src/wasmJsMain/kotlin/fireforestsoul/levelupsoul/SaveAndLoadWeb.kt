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
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.browser.localStorage
import kotlinx.datetime.LocalDate

actual fun closeSaveFiles() {}
actual fun openSaveFiles() {}

actual fun saveValue(value: Any, name: String) {
    val serialized = value.savedElementToString()
    localStorage.setItem(name, serialized)
}

actual fun <T> loadValue(value: T, name: String): T {
    val serialized = localStorage.getItem(name) ?: return value
    return serialized.loadedElementToVal(value)
}

actual fun old1001000000LoadAllValues() {
    val oldAppVersion = localStorage.getItem("app_version")?.toLong() ?: return
    if (oldAppVersion < 1001001000) {
        val habitsSize = localStorage.getItem("habits-size")?.toInt() ?: return
        habits = mutableListOf(Habit())
        for (x in 0 until habitsSize) {
            if (x > 0) habits.add(Habit())
            habits[x].nameOfHabit = localStorage.getItem("habits-$x-nameOfHabit").toString()
            habits[x].nameOfUnitsOfDimension = localStorage.getItem("habits-$x-nameOfUnitsOfDimension").toString()
            if (oldAppVersion >= 3000000)
                habits[x].typeOfGoalHabits =
                    enumValueOf<TypeOfGoalHabits>(localStorage.getItem("habits-$x-typeOfGoalHabits").toString())
            else habits[x].typeOfGoalHabits = toTypeOfGoalHabits(
                enumValueOf<Old3000000TypeOfGoalHabits>(
                    localStorage.getItem("habits-$x-typeOfGoalHabits").toString()
                )
            )
            habits[x].needGoal = (localStorage.getItem("habits-$x-needGoal")?.toBigDecimal() ?: return)
            habits[x].needDays = (localStorage.getItem("habits-$x-needDays")?.toInt() ?: return)

            if (oldAppVersion >= 2000000) {
                habits[x].typeOfColorHabits =
                    enumValueOf<TypeOfColorHabits>(localStorage.getItem("habits-$x-typeOfColorHabits").toString())
                habits[x].colorGood = Color(localStorage.getItem("habits-$x-colorGood").toString().toULong(16))

                if (oldAppVersion >= 1000000000) {
                    habits[x].changeLevel = localStorage.getItem("habits-$x-changeLevel").toBoolean()
                    habits[x].changeNeedGoalWithLevel =
                        localStorage.getItem("habits-$x-changeNeedGoalWithLevel").toBoolean()
                    habits[x].changeNeedDaysWithLevel =
                        localStorage.getItem("habits-$x-changeNeedDaysWithLevel").toBoolean()
                }
            }
            habits[x].startDate = (localStorage.getItem("habits-$x-startDate")?.let { LocalDate.parse(it) } ?: return)

            if (oldAppVersion >= 1000000000) {
                habits[x].lastLevelChangeDate =
                    (localStorage.getItem("habits-$x-lastLevelChangeDate")?.let { LocalDate.parse(it) } ?: return)
                habits[x].level = (localStorage.getItem("habits-$x-level")?.toInt() ?: return)

                if (oldAppVersion >= 1001000000) {
                    habits[x].iconChar = localStorage.getItem("habits-$x-iconChar").toString()
                }
            }

            val habitDaySize = localStorage.getItem("habits-$x-habitDay-size")?.toInt() ?: return
            habits[x].habitDay = mutableListOf(HabitDay())
            for (y in 0 until habitDaySize) {
                if (y > 0) habits[x].habitDay.add(HabitDay())
                habits[x].habitDay[y].today = (localStorage.getItem("habits-$x-habitDay-$y-today")?.toBigDecimal()
                    ?: return)
                habits[x].habitDay[y].totalOfAPeriod =
                    (localStorage.getItem("habits-$x-habitDay-$y-totalOfAPeriod")?.toBigDecimal() ?: return)
                habits[x].habitDay[y].correctly =
                    localStorage.getItem("habits-$x-habitDay-$y-correctly").toBoolean()
            }
        }

        if (oldAppVersion >= 4000000) {
            soul_color_type = enumValueOf<TypeOfColorHabits>(localStorage.getItem("soul_color_type").toString())
            soul_color = Color(localStorage.getItem("soul_color").toString().toULong(16))
            soul_name = localStorage.getItem("soul_name").toString()

            if (oldAppVersion >= 1000000000) {
                soul_level = (localStorage.getItem("soul_level")?.toInt() ?: return)
                soul_last_level_change_date =
                    (localStorage.getItem("soul_last_level_change_date")?.let { LocalDate.parse(it) } ?: return)

                if (oldAppVersion >= 1000001000) {
                    language = enumValueOf<Languages>(localStorage.getItem("language").toString())

                    if (oldAppVersion >= 1001000000) {
                        withExponent = localStorage.getItem("withExponent").toBoolean()
                    }
                }
            }
        }
    }
}

actual fun deleteValue(name: String) {
    localStorage.removeItem(name)
}