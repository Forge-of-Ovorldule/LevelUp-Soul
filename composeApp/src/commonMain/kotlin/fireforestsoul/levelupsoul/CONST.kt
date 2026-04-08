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
import kotlinx.datetime.Clock
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

const val app_version: Long = 1001006000 //version(001).001.005.000

const val old1001000000_save_file_name = "LevelUp-Soul.FireForestSouls-saving"
const val save_file_name = "levelup-soul-saving-by-forge-of-ovorldule"

var habits: MutableList<Habit> = mutableListOf(
    Habit()
)

var soul_color_type = TypeOfColorHabits.ADAPTIVE
var soul_color = Color(200, 200, 200)
var soul_name = ts_Mr_Soul_Forest
var soul_level = 0
var soul_last_level_change_date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

var language: Languages = Languages.EN
var sort_habit_statistics_sections_by_frequency_of_use = false

var backAppStatus = AppStatus.TABLE