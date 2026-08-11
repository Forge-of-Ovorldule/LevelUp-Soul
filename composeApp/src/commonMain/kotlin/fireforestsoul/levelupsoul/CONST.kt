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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

enum class AppStatus {
    LOADING,
    TABLE,
    CREATE_HABIT,
    HABIT_STATISTICS,
    EDIT_HABIT,
    TABLE_UPDATER,
    SOUL_STATISTICS,
    HABITS_LIST,
    HABITS_LIST_UPDATER
}

const val app_version: Long = 1002000000 //version(001).002.000.000

const val old1001000000_save_file_name: String = "LevelUp-Soul.FireForestSouls-saving"
const val save_file_name: String = "levelup-soul-saving-by-forge-of-ovorldule"

var habits: MutableList<Habit> = mutableListOf(
    Habit()
)

var soul_color_type: TypeOfColorHabits = TypeOfColorHabits.ADAPTIVE
var soul_color: Color = Color(200, 200, 200)
var soul_name: String = ts_Mr_Soul_Forest
var soul_level: Int = 0
var soul_last_level_change_date: LocalDate =
    kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

var language: Languages = Languages.EN
var sort_habit_statistics_sections_by_frequency_of_use: Boolean = false

var backAppStatus: AppStatus = AppStatus.TABLE

var smart_sort: Boolean = false