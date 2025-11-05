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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

class Habit(
    var nameOfHabit: String = ts_New_habit,
    var nameOfUnitsOfDimension: String = ts_km,
    var typeOfGoalHabits: TypeOfGoalHabits = TypeOfGoalHabits.AT_LEAST,
    var needGoal: BigDecimal = BigDecimal.ONE,
    var needDays: Int = 1,
    var typeOfColorHabits: TypeOfColorHabits = TypeOfColorHabits.SELECTED,
    var colorGood: Color = UICT_see,
    var changeLevel: Boolean = true,
    var changeNeedGoalWithLevel: Boolean = false,
    var changeNeedDaysWithLevel: Boolean = false,
    var iconChar: String = ""
) {

    var startDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var lastLevelChangeDate: LocalDate = startDate
    var level: Int = 0
    var habitDay: MutableList<HabitDay> = MutableList(1) { HabitDay(0.toBigDecimal()) }
    var phantomNeedDays = needDays.toBigDecimal()

    fun updateDate() {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val addDays: Int = (today.toEpochDays() - startDate.toEpochDays() - habitDay.size + 1)

        if (addDays > 0) {
            habitDay.addAll(List(addDays) { HabitDay(0.toBigDecimal()) })
        }

        update()
    }

    fun updateHabitDay(index: Int) {
        habitDay[index].totalOfAPeriod = 0.toBigDecimal()
        for (i in (index - needDays + 1)..index) {
            if (i >= 0)
                habitDay[index].totalOfAPeriod += habitDay[i].today
        }
        when (typeOfGoalHabits) {
            TypeOfGoalHabits.NO_MORE -> habitDay[index].correctly = (habitDay[index].totalOfAPeriod <= needGoal)
            TypeOfGoalHabits.AT_LEAST -> habitDay[index].correctly = (habitDay[index].totalOfAPeriod >= needGoal)
        }
    }

    fun update(sortedHabits: MutableList<Int> = mutableListOf()) {
        for (i in 0..<habitDay.size) {
            updateHabitDay(i)
        }

        if (sortedHabits.isNotEmpty()) {
            sortedHabits.sortSystem()
        }

        if (changeLevel) {
            if (Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays() - lastLevelChangeDate.toEpochDays() >= 20
            ) {
                var goodProgress = 0
                if (progress(this) >= 0.8) {
                    for (x in (habitDay.size - 20) until habitDay.size) {
                        if (x >= 0) {
                            if (progress(this, startIndex = x) >= 0.8) {
                                goodProgress++
                            }
                        }
                    }
                    if (goodProgress == 20) {
                        level++
                        lastLevelChangeDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                        if (changeNeedDaysWithLevel) {
                            when (typeOfGoalHabits) {
                                TypeOfGoalHabits.AT_LEAST -> phantomNeedDays *= "0.8".toBigDecimal()
                                TypeOfGoalHabits.NO_MORE -> phantomNeedDays /= "0.8".toBigDecimal()
                            }
                            needDays =
                                if (phantomNeedDays % 1 != BigDecimal.ZERO) phantomNeedDays.intValue(false) + 1 else phantomNeedDays.intValue(
                                    false
                                )
                        }
                        if (changeNeedGoalWithLevel) {
                            when (typeOfGoalHabits) {
                                TypeOfGoalHabits.AT_LEAST -> needGoal /= "0.8".toBigDecimal()
                                TypeOfGoalHabits.NO_MORE -> needGoal *= "0.8".toBigDecimal()
                            }
                        }
                    }
                } else if (progress(this) <= 0.2) {
                    for (x in (habitDay.size - 20) until habitDay.size) {
                        if (x >= 0) {
                            if (progress(this, startIndex = x) <= 0.2) {
                                goodProgress++
                            }
                        }
                    }
                    if (goodProgress == 20) {
                        level--
                        lastLevelChangeDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                        if (changeNeedDaysWithLevel) {
                            when (typeOfGoalHabits) {
                                TypeOfGoalHabits.AT_LEAST -> phantomNeedDays /= 0.8
                                TypeOfGoalHabits.NO_MORE -> phantomNeedDays *= "0.8".toBigDecimal()
                            }
                            needDays =
                                if (phantomNeedDays % 1 != BigDecimal.ZERO) phantomNeedDays.intValue(false) + 1 else phantomNeedDays.intValue(
                                    false
                                )
                        }
                        if (changeNeedGoalWithLevel) {
                            when (typeOfGoalHabits) {
                                TypeOfGoalHabits.AT_LEAST -> needGoal *= "0.8".toBigDecimal()
                                TypeOfGoalHabits.NO_MORE -> needGoal /= "0.8".toBigDecimal()
                            }
                        }
                    }
                }
            }
        }
    }

    fun getToLevelUp(pps: Int = habitDay.size - 1): Float {
        var end = habitDay.size - 20
        end = if (end < 0) 0 else end
        end =
            if (end < lastLevelChangeDate.toEpochDays() - startDate.toEpochDays()) lastLevelChangeDate.toEpochDays() - startDate.toEpochDays() else end

        var progress = 0f
        val isProgressUp = if (progress(this, pps) <= 0.2f) false else true
        for (index in (habitDay.size - 1) downTo end) {
            if (isProgressUp) {
                if (progress(this, pps, index) >= 0.8f) progress++
                else break
            } else {
                if (progress(this, pps, index) <= 0.2f) progress--
                else break
            }
        }
        return progress / 20f
    }

    fun getNeedGoalWhenNewLevel(pps: Int = habitDay.size - 1): BigDecimal {
        val isProgressUp = if (progress(this, pps) <= 0.2f) false else true
        if (changeNeedGoalWithLevel) {
            return if (isProgressUp) {
                when (typeOfGoalHabits) {
                    TypeOfGoalHabits.AT_LEAST -> needGoal / "0.8".toBigDecimal()
                    TypeOfGoalHabits.NO_MORE -> needGoal * "0.8".toBigDecimal()
                }
            } else {
                when (typeOfGoalHabits) {
                    TypeOfGoalHabits.AT_LEAST -> needGoal * "0.8".toBigDecimal()
                    TypeOfGoalHabits.NO_MORE -> needGoal / "0.8".toBigDecimal()
                }
            }
        }
        return needGoal
    }

    fun getPhantomNeedDaysWhenNewLevel(pps: Int = habitDay.size - 1): BigDecimal {
        val isProgressUp = if (progress(this, pps) <= 0.2f) false else true
        if (changeNeedDaysWithLevel) {
            return if (isProgressUp) {
                when (typeOfGoalHabits) {
                    TypeOfGoalHabits.AT_LEAST -> phantomNeedDays * "0.8".toBigDecimal()
                    TypeOfGoalHabits.NO_MORE -> phantomNeedDays / "0.8".toBigDecimal()
                }
            } else {
                when (typeOfGoalHabits) {
                    TypeOfGoalHabits.AT_LEAST -> phantomNeedDays / 0.8
                    TypeOfGoalHabits.NO_MORE -> phantomNeedDays * "0.8".toBigDecimal()
                }
            }
        }
        return phantomNeedDays
    }

    fun getNeedDaysWhenNewLevel(pps: Int = habitDay.size - 1): Int {
        if (changeNeedDaysWithLevel) {
            return if (getPhantomNeedDaysWhenNewLevel(pps) - getPhantomNeedDaysWhenNewLevel(pps).intValue(false) != BigDecimal.ZERO)
                getPhantomNeedDaysWhenNewLevel(pps).intValue(false) + 1
            else
                getPhantomNeedDaysWhenNewLevel(pps).intValue(false)
        }
        return needDays
    }

    fun loadNeedDays(value: Int) {
        needDays = value
        phantomNeedDays = value.toBigDecimal()
    }
}

fun MutableList<Int>.sortSystem() {
    this.sortByDescending { if (habitStreaks(it).isNotEmpty()) habitStreaks(it)[0] else 0 }
    this.sortByDescending { habits[it].level }
    this.sortByDescending { progress(it) }
}