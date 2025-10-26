package fireforestsoul.levelupsoul

import androidx.compose.ui.graphics.Color
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.browser.localStorage
import kotlinx.datetime.LocalDate

actual fun saveValue() {
    localStorage.setItem("app_version", app_version.toString())
    localStorage.setItem("habits-size", habits.size.toString())
    for (x in 0 until habits.size) {
        localStorage.setItem("habits-$x-nameOfHabit", habits[x].nameOfHabit)
        localStorage.setItem("habits-$x-nameOfUnitsOfDimension", habits[x].nameOfUnitsOfDimension)
        localStorage.setItem("habits-$x-typeOfGoalHabits", habits[x].typeOfGoalHabits.toString())
        localStorage.setItem("habits-$x-needGoal", habits[x].needGoal.toString())
        localStorage.setItem("habits-$x-needDays", habits[x].needDays.toString())
        localStorage.setItem("habits-$x-typeOfColorHabits", habits[x].typeOfColorHabits.toString())
        localStorage.setItem("habits-$x-colorGood", habits[x].colorGood.value.toString(16))
        localStorage.setItem("habits-$x-changeLevel", habits[x].changeLevel.toString())
        localStorage.setItem("habits-$x-changeNeedGoalWithLevel", habits[x].changeNeedGoalWithLevel.toString())
        localStorage.setItem("habits-$x-changeNeedDaysWithLevel", habits[x].changeNeedDaysWithLevel.toString())
        localStorage.setItem("habits-$x-startDate", habits[x].startDate.toString())
        localStorage.setItem("habits-$x-lastLevelChangeDate", habits[x].lastLevelChangeDate.toString())
        localStorage.setItem("habits-$x-level", habits[x].level.toString())
        localStorage.setItem("habits-$x-iconChar", habits[x].iconChar)
        localStorage.setItem("habits-$x-habitDay-size", habits[x].habitDay.size.toString())
        for (y in 0 until habits[x].habitDay.size) {
            localStorage.setItem("habits-$x-habitDay-$y-today", habits[x].habitDay[y].today.toString())
            localStorage.setItem(
                "habits-$x-habitDay-$y-totalOfAPeriod",
                habits[x].habitDay[y].totalOfAPeriod.toString()
            )
            localStorage.setItem("habits-$x-habitDay-$y-correctly", habits[x].habitDay[y].correctly.toString())
        }
    }
    localStorage.setItem("soul_color_type", soul_color_type.toString())
    localStorage.setItem("soul_color", soul_color.value.toString(16))
    localStorage.setItem("soul_name", soul_name)
    localStorage.setItem("soul_level", soul_level.toString())
    localStorage.setItem("soul_last_level_change_date", soul_last_level_change_date.toString())
    localStorage.setItem("language", language.toString())
    localStorage.setItem("withExponent", withExponent.toString())
}

actual fun old1001000000LoadAllValues() {

    val oldAppVersion = localStorage.getItem("app_version")?.toLong()
    if (oldAppVersion != null) {
        if (oldAppVersion > 0) {

            val habitsSize = localStorage.getItem("habits-size")?.toInt()!!
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
                habits[x].needGoal = localStorage.getItem("habits-$x-needGoal")?.toBigDecimal()!!
                habits[x].needDays = localStorage.getItem("habits-$x-needDays")?.toInt()!!

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
                habits[x].startDate = localStorage.getItem("habits-$x-startDate")?.let { LocalDate.parse(it) }!!

                if (oldAppVersion >= 1000000000) {
                    habits[x].lastLevelChangeDate =
                        localStorage.getItem("habits-$x-lastLevelChangeDate")?.let { LocalDate.parse(it) }!!
                    habits[x].level = localStorage.getItem("habits-$x-level")?.toInt()!!

                    if (oldAppVersion >= 1001000000) {
                        habits[x].iconChar = localStorage.getItem("habits-$x-iconChar").toString()
                    }
                }

                val habitDaySize = localStorage.getItem("habits-$x-habitDay-size")?.toInt()!!
                habits[x].habitDay = mutableListOf(HabitDay())
                for (y in 0 until habitDaySize) {
                    if (y > 0) habits[x].habitDay.add(HabitDay())
                    habits[x].habitDay[y].today = localStorage.getItem("habits-$x-habitDay-$y-today")?.toBigDecimal()!!
                    habits[x].habitDay[y].totalOfAPeriod =
                        localStorage.getItem("habits-$x-habitDay-$y-totalOfAPeriod")?.toBigDecimal()!!
                    habits[x].habitDay[y].correctly =
                        localStorage.getItem("habits-$x-habitDay-$y-correctly").toBoolean()
                }
            }

            if (oldAppVersion >= 4000000) {
                soul_color_type = enumValueOf<TypeOfColorHabits>(localStorage.getItem("soul_color_type").toString())
                soul_color = Color(localStorage.getItem("soul_color").toString().toULong(16))
                soul_name = localStorage.getItem("soul_name").toString()

                if (oldAppVersion >= 1000000000) {
                    soul_level = localStorage.getItem("soul_level")?.toInt()!!
                    soul_last_level_change_date =
                        localStorage.getItem("soul_last_level_change_date")?.let { LocalDate.parse(it) }!!

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
}