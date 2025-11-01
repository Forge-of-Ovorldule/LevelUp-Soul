/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.datetime.LocalDate
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

@SuppressLint("StaticFieldLeak")
var context: Context? = null

fun initStorage(appContext: Context) {
    context = appContext
}

private val prefs: SharedPreferences
    get() = context!!.getSharedPreferences(save_file_name, Context.MODE_PRIVATE)

actual fun saveValue(value: Any, name: String) {
    val serialized = value.savedElementToString()
    prefs.edit { putString(name, serialized) }
}

actual fun <T> loadValue(value: T, name: String): T {
    val serialized =
        context!!.getSharedPreferences(save_file_name, Context.MODE_PRIVATE).getString(name, null)
            ?: return value

    return serialized.loadedElementToVal(value)
}

//old saving system

private val old1001000000_prefs: SharedPreferences
    get() = context!!.getSharedPreferences(old1001000000_save_file_name, Context.MODE_PRIVATE)

actual fun old1001000000LoadAllValues() {
    val oldAppVersion = old1001000000_prefs.getString("app_version", null)?.toLongOrNull() ?: return

    if (oldAppVersion > 0) {
        val habitsSize = old1001000000_prefs.getString("habits-size", null)?.toIntOrNull() ?: 0
        habits = MutableList(habitsSize) { Habit() }
        for (x in 0 until habitsSize) {
            habits[x].nameOfHabit =
                old1001000000_prefs.getString("habits-$x-nameOfHabit", "New habit") ?: "New habit"
            habits[x].nameOfUnitsOfDimension =
                old1001000000_prefs.getString("habits-$x-nameOfUnitsOfDimension", "km") ?: "km"
            if (oldAppVersion >= 3000000) {
                habits[x].typeOfGoalHabits = enumValueOf<TypeOfGoalHabits>(
                    old1001000000_prefs.getString(
                        "habits-$x-typeOfGoalHabits",
                        "AT_LEAST"
                    )!!
                )
            } else {
                habits[x].typeOfGoalHabits = toTypeOfGoalHabits(
                    enumValueOf<Old3000000TypeOfGoalHabits>(
                        old1001000000_prefs.getString(
                            "habits-$x-typeOfGoalHabits",
                            "NOT_LITTLE"
                        )!!
                    )
                )
            }
            habits[x].needGoal =
                old1001000000_prefs.getString("habits-$x-needGoal", "1")?.toBigDecimal()
                    ?: 1.toBigDecimal()
            habits[x].needDays =
                old1001000000_prefs.getString("habits-$x-needDays", "1")?.toIntOrNull() ?: 1

            if (oldAppVersion >= 2000000) {
                habits[x].typeOfColorHabits =
                    enumValueOf<TypeOfColorHabits>(
                        old1001000000_prefs.getString(
                            "habits-$x-typeOfColorHabits",
                            "ADAPTIVE"
                        )!!
                    )
                habits[x].colorGood = Color(
                    old1001000000_prefs.getString("habits-$x-colorGood", "ff000000")
                        ?.toULongOrNull(16)
                        ?: 0xFF000000u
                )

                if (oldAppVersion >= 1000000000) {
                    habits[x].changeLevel =
                        old1001000000_prefs.getString("habits-$x-changeLevel", "true").toBoolean()
                    habits[x].changeNeedGoalWithLevel =
                        old1001000000_prefs.getString("habits-$x-changeNeedGoalWithLevel", "false")
                            .toBoolean()
                    habits[x].changeNeedDaysWithLevel =
                        old1001000000_prefs.getString("habits-$x-changeNeedDaysWithLevel", "false")
                            .toBoolean()
                }
            }

            habits[x].startDate =
                old1001000000_prefs.getString("habits-$x-startDate", "2025-01-01")
                    ?.let { LocalDate.parse(it) }
                    ?: LocalDate(
                        2025,
                        1,
                        1
                    )

            if (oldAppVersion >= 1000000000) {
                habits[x].lastLevelChangeDate =
                    old1001000000_prefs.getString("habits-$x-lastLevelChangeDate", "2025-01-01")
                        ?.let { LocalDate.parse(it) }!!
                habits[x].level = old1001000000_prefs.getString("habits-$x-level", "0")?.toInt()!!

                if (oldAppVersion >= 1001000000) {
                    habits[x].iconChar =
                        old1001000000_prefs.getString("habits-$x-iconChar", " ").toString()
                }
            }

            val habitDaySize =
                old1001000000_prefs.getString("habits-$x-habitDay-size", null)?.toIntOrNull() ?: 0
            habits[x].habitDay = MutableList(habitDaySize) { HabitDay() }
            for (y in 0 until habitDaySize) {
                habits[x].habitDay[y].today =
                    old1001000000_prefs.getString("habits-$x-habitDay-$y-today", "0")
                        ?.toBigDecimal()
                        ?: 0.toBigDecimal()
                habits[x].habitDay[y].totalOfAPeriod =
                    old1001000000_prefs.getString("habits-$x-habitDay-$y-totalOfAPeriod", "0")
                        ?.toBigDecimal()
                        ?: 0.toBigDecimal()
                habits[x].habitDay[y].correctly =
                    old1001000000_prefs.getString(
                        "habits-$x-habitDay-$y-correctly",
                        "false"
                    ) == "true"
            }
        }

        if (oldAppVersion >= 4000000) {
            soul_color_type = enumValueOf<TypeOfColorHabits>(
                old1001000000_prefs.getString("soul_color_type", "ADAPTIVE").toString()
            )
            soul_color = Color(
                old1001000000_prefs.getString("soul_color", "ff000000").toString().toULong(16)
            )
            soul_name = old1001000000_prefs.getString("soul_name", "Mr. Soul Forest").toString()

            if (oldAppVersion >= 1000000000) {
                soul_level = old1001000000_prefs.getString("soul_level", "0")?.toInt()!!
                soul_last_level_change_date =
                    old1001000000_prefs.getString("soul_last_level_change_date", "2025-01-01")
                        ?.let { LocalDate.parse(it) }!!

                if (oldAppVersion >= 1000001000) {
                    language =
                        enumValueOf<Languages>(
                            old1001000000_prefs.getString("language", "EN").toString()
                        )

                    if (oldAppVersion >= 1001000000) {
                        withExponent =
                            old1001000000_prefs.getString("withExponent", "false") == "true"
                    }
                }
            }
        }
    }
}

actual fun deleteValue(name: String) {
    prefs.edit { remove(name) }
}