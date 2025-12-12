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
import java.io.File
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.serialization.json.*

private val json = Json { prettyPrint = true }
private val settingsFile = File("$save_file_name.json")

private var savedStrings: MutableMap<String, JsonElement> = readSettings().toMutableMap()

actual fun openSaveFiles() {
    savedStrings = readSettings().toMutableMap()
}

actual fun closeSaveFiles() {
    settingsFile.writeText(json.encodeToString(JsonObject(savedStrings)))
}

private fun readSettings(): MutableMap<String, JsonElement> {
    if (!settingsFile.exists()) return mutableMapOf()
    val text = settingsFile.readText()
    if (text.isBlank()) return mutableMapOf()
    return json.parseToJsonElement(text).jsonObject.toMutableMap()
}

actual fun saveValue(value: Any, name: String) {
    val element = JsonPrimitive(value.savedElementToString())
    savedStrings[name] = element
}

actual fun <T> loadValue(value: T, name: String): T {
    val settings = readSettings()
    val jsonElement = settings[name] ?: return value
    var element = jsonElement.jsonPrimitive.toString()
    element = element.substring(1, element.length - 1)

    return element.loadedElementToVal(value)
}

actual fun old1001000000LoadAllValues() {
    val file = File(old1001000000_save_file_name)
    if (file.exists()) {
        val input = file.readLines()

        val oldAppVersion = input.getOrNull(0)?.toLong()
        if (oldAppVersion != null) {
            if (oldAppVersion > 0) {
                var index = 1

                val habitsSize = input.getOrNull(index)?.toInt()!!
                index++
                habits = mutableListOf(Habit())
                for (x in 0 until habitsSize) {
                    if (x > 0) habits.add(Habit())
                    habits[x].nameOfHabit = input.getOrNull(index).toString()
                    index++
                    habits[x].nameOfUnitsOfDimension = input.getOrNull(index).toString()
                    index++
                    if (oldAppVersion > 2001000) {
                        habits[x].typeOfGoalHabits =
                            enumValueOf<TypeOfGoalHabits>(input.getOrNull(index).toString())
                        index++
                    } else {
                        habits[x].typeOfGoalHabits = toTypeOfGoalHabits(
                            enumValueOf<Old3000000TypeOfGoalHabits>(
                                input.getOrNull(index).toString()
                            )
                        )
                        index++
                    }
                    habits[x].needGoal = input.getOrNull(index)?.toBigDecimal()!!
                    index++
                    habits[x].needDays = input.getOrNull(index)?.toInt()!!
                    index++
                    if (oldAppVersion >= 2000000) {
                        habits[x].typeOfColorHabits =
                            enumValueOf<TypeOfColorHabits>(input.getOrNull(index).toString())
                        index++
                        habits[x].colorGood = Color(input.getOrNull(index).toString().toULong(16))
                        index++

                        if (oldAppVersion >= 1000000000) {
                            habits[x].changeLevel = input.getOrNull(index).toBoolean()
                            index++
                            habits[x].changeNeedGoalWithLevel = input.getOrNull(index).toBoolean()
                            index++
                            habits[x].changeNeedDaysWithLevel = input.getOrNull(index).toBoolean()
                            index++
                        }
                    }
                    habits[x].startDate =
                        input.getOrNull(index)?.let { LocalDate.parse(it) }!!
                    index++
                    if (oldAppVersion < 1000000000) {
                        /** lastDate */
                        index++
                    }

                    if (oldAppVersion >= 1000000000) {
                        habits[x].lastLevelChangeDate = input.getOrNull(index)?.let { LocalDate.parse(it) }!!
                        index++
                        habits[x].level = input.getOrNull(index)?.toInt()!!
                        index++

                        if (oldAppVersion >= 1001000000) {
                            habits[x].iconChar = input.getOrNull(index).toString()
                            index++
                        }

                    }

                    val habitDaySize = input.getOrNull(index)?.toInt()!!
                    index++
                    habits[x].habitDay = mutableListOf(HabitDay())
                    for (y in 0 until habitDaySize) {
                        if (y > 0) habits[x].habitDay.add(HabitDay())

                        habits[x].habitDay[y].today =
                            input.getOrNull(index)?.toBigDecimal()!!
                        index++
                        habits[x].habitDay[y].totalOfAPeriod =
                            input.getOrNull(index)?.toBigDecimal()!!
                        index++
                        habits[x].habitDay[y].correctly =
                            input.getOrNull(index).toBoolean()
                        index++
                    }
                }
                if (oldAppVersion > 3000000) {

                    soul_color_type = enumValueOf<TypeOfColorHabits>(input.getOrNull(index).toString())
                    index++
                    soul_color = Color(input.getOrNull(index).toString().toULong(16))
                    index++
                    soul_name = input.getOrNull(index).toString()
                    index++

                    if (oldAppVersion >= 1000000000) {
                        soul_level = input.getOrNull(index)?.toInt()!!
                        index++
                        soul_last_level_change_date = input.getOrNull(index)?.let { LocalDate.parse(it) }!!
                        index++

                        if (oldAppVersion >= 1000001000) {
                            language = enumValueOf<Languages>(input.getOrNull(index).toString())
                            index++

                            if (oldAppVersion >= 1001000000) {
                                withExponent = input.getOrNull(index).toBoolean()
                            }
                        }
                    }
                }
            }
        }
    }
}

actual fun deleteValue(name: String) {
    val settings = readSettings().toMutableMap()
    settings.remove(name)
    settingsFile.writeText(json.encodeToString(JsonObject(settings)))
}