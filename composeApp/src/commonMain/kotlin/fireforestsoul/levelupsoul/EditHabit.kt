/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

@Composable
fun EditHabit(viewModel: AppViewModel) {
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()
    var expanded0 by remember { mutableStateOf(false) }
    var expanded1 by remember { mutableStateOf(false) }

    var nameOfHabit by remember { mutableStateOf(habits[habit_statistics_and_edit_x].nameOfHabit) }
    var icon by remember { mutableStateOf(habits[habit_statistics_and_edit_x].iconChar) }
    var typeOfColorHabits by remember { mutableStateOf(habits[habit_statistics_and_edit_x].typeOfColorHabits) }
    var colorGood by remember { mutableStateOf(habits[habit_statistics_and_edit_x].colorGood) }
    var typeOfGoalHabits by remember { mutableStateOf(habits[habit_statistics_and_edit_x].typeOfGoalHabits) }
    var needGoal by remember { mutableStateOf(habits[habit_statistics_and_edit_x].needGoal.toBestString()) }
    var nameOfUnitsOfDimension by remember { mutableStateOf(habits[habit_statistics_and_edit_x].nameOfUnitsOfDimension) }
    var needDays by remember { mutableStateOf(habits[habit_statistics_and_edit_x].needDays.toString()) }
    var changeLevel by remember { mutableStateOf(habits[habit_statistics_and_edit_x].changeLevel) }
    var changeNeedGoalWithLevel by remember { mutableStateOf(habits[habit_statistics_and_edit_x].changeNeedGoalWithLevel) }
    var changeNeedDaysWithLevel by remember { mutableStateOf(habits[habit_statistics_and_edit_x].changeNeedDaysWithLevel) }

    val spaceX = 4.dp
    val spaceY = 4.dp
    val spaceXY = 15.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UIC_dark)
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UIC)
                        .height(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ts_Edit + " \"${habits[habit_statistics_and_edit_x].nameOfHabit}\"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = UICT_see,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
            },
            bottomBar = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(UIC)
                        .padding(horizontal = 50.dp)
                ) {
                    Text(
                        text = "❌ $ts_Cancel",
                        fontSize = 16.sp,
                        color = Color(200, 150, 150),
                        modifier = Modifier.clickable {
                            viewModel.setStatus(AppStatus.HABIT_STATISTICS)
                        }
                    )
                    Text(
                        text = "✅ $ts_Confirm",
                        fontSize = 16.sp,
                        color = Color(150, 200, 150),
                        modifier = Modifier.clickable {
                            habits[habit_statistics_and_edit_x].nameOfHabit = nameOfHabit
                            habits[habit_statistics_and_edit_x].typeOfColorHabits = typeOfColorHabits
                            habits[habit_statistics_and_edit_x].colorGood = colorGood
                            habits[habit_statistics_and_edit_x].typeOfGoalHabits = typeOfGoalHabits
                            habits[habit_statistics_and_edit_x].needGoal =
                                if (needGoal.toDoubleOrNull() != null) needGoal.toBigDecimal() else habits[habit_statistics_and_edit_x].needGoal
                            habits[habit_statistics_and_edit_x].nameOfUnitsOfDimension = nameOfUnitsOfDimension
                            habits[habit_statistics_and_edit_x].needDays =
                                (if (needDays.toIntOrNull() != null) needDays.toIntOrNull() else habits[habit_statistics_and_edit_x].needDays)!!
                            habits[habit_statistics_and_edit_x].changeLevel = changeLevel
                            habits[habit_statistics_and_edit_x].changeNeedGoalWithLevel = changeNeedGoalWithLevel
                            habits[habit_statistics_and_edit_x].changeNeedDaysWithLevel = changeNeedDaysWithLevel
                            habits[habit_statistics_and_edit_x].iconChar = icon
                            habits[habit_statistics_and_edit_x].phantomNeedDays =
                                habits[habit_statistics_and_edit_x].needDays.toBigDecimal()
                            habits[habit_statistics_and_edit_x].update()
                            habits[habit_statistics_and_edit_x].save(habit_statistics_and_edit_x)
                            viewModel.setStatus(AppStatus.HABIT_STATISTICS)
                        }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(UIC_dark)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(verticalScroll)
                        .padding(spaceXY),
                    verticalArrangement = Arrangement.spacedBy(spaceY)
                ) {
                    //nameOfHabit
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spaceX),
                    ) {
                        Text(
                            text = "$ts_Title: ",
                            fontSize = 16.sp,
                            color = UICT_see
                        )
                        OutlinedTextField(
                            value = nameOfHabit,
                            onValueChange = { nameOfHabit = it },
                            label = {
                                Text(
                                    "$ts_Old: ${habits[habit_statistics_and_edit_x].nameOfHabit}",
                                    fontSize = 12.sp,
                                    color = UICT_no_see
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = UICT_see
                            ),
                            shape = RoundedCornerShape(15.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = UICT_see,
                                unfocusedTextColor = UICT_no_see,
                                disabledTextColor = UICT_no_see,
                                focusedContainerColor = UIC_extra_dark,
                                unfocusedContainerColor = UIC_extra_dark,
                                disabledContainerColor = UIC_extra_dark,
                                cursorColor = UICT_see,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                        )
                    }
                    //icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spaceX),
                    ) {
                        Text(
                            text = "$ts_Icon ($ts_one_symbol): ",
                            fontSize = 16.sp,
                            color = UICT_see
                        )
                        OutlinedTextField(
                            value = icon,
                            onValueChange = { icon = it },
                            label = {
                                Text(
                                    habits[habit_statistics_and_edit_x].iconChar,
                                    fontSize = 12.sp,
                                    color = UICT_no_see
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = UICT_see
                            ),
                            shape = RoundedCornerShape(15.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = UICT_see,
                                unfocusedTextColor = UICT_no_see,
                                disabledTextColor = UICT_no_see,
                                focusedContainerColor = UIC_extra_dark,
                                unfocusedContainerColor = UIC_extra_dark,
                                disabledContainerColor = UIC_extra_dark,
                                cursorColor = UICT_see,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.width(75.dp)
                        )
                    }
                    //colors
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spaceX),
                    ) {
                        Text(
                            text = "$ts_Color: ",
                            fontSize = 16.sp,
                            color = UICT_see,
                        )
                        Column {
                            Button(
                                onClick = { expanded0 = true },
                                colors = ButtonColors(
                                    containerColor = UIC_extra_dark,
                                    contentColor = UICT_see,
                                    disabledContainerColor = UIC_extra_dark,
                                    disabledContentColor = UICT_no_see
                                )
                            ) {
                                Text(
                                    "$ts_type: ${typeOfColorHabits.name}",
                                    fontSize = 16.sp,
                                    color = UICT_see,
                                )
                            }
                            DropdownMenu(
                                expanded = expanded0,
                                onDismissRequest = { expanded0 = false },
                                modifier = Modifier.background(UIC_extra_dark)
                            ) {
                                TypeOfColorHabits.entries.forEach { mode ->
                                    DropdownMenuItem(
                                        onClick = {
                                            typeOfColorHabits = mode
                                            expanded0 = false
                                        },
                                        text = {
                                            Text(
                                                text = mode.name,
                                                fontSize = 16.sp,
                                                color = UICT_no_see
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        ColorPickerBox(
                            initialColor = colorGood,
                            onColorSelected = { colorGood = it }
                        )
                    }
                    //Goal
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(UIC, RoundedCornerShape(20.dp))
                            .height(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ts_Goal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = UICT_see,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .horizontalScroll(horizontalScroll),
                        verticalArrangement = Arrangement.spacedBy(spaceY)
                    ) {
                        //type
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(spaceX),
                        ) {
                            Text(
                                text = "$ts_Type: ",
                                fontSize = 16.sp,
                                color = UICT_see,
                            )
                            Column {
                                Button(
                                    onClick = { expanded1 = true },
                                    colors = ButtonColors(
                                        containerColor = UIC_extra_dark,
                                        contentColor = UICT_see,
                                        disabledContainerColor = UIC_extra_dark,
                                        disabledContentColor = UICT_no_see
                                    )
                                ) {
                                    Text(
                                        "$ts_type: ${typeOfGoalHabits.name}",
                                        fontSize = 16.sp,
                                        color = UICT_see,
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded1,
                                    onDismissRequest = { expanded1 = false },
                                    modifier = Modifier.background(UIC_extra_dark)
                                ) {
                                    TypeOfGoalHabits.entries.forEach { mode ->
                                        DropdownMenuItem(
                                            onClick = {
                                                typeOfGoalHabits = mode
                                                expanded1 = false
                                            },
                                            text = {
                                                Text(
                                                    text = mode.name,
                                                    fontSize = 16.sp,
                                                    color = UICT_no_see
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        //count
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(spaceX),
                        ) {
                            Text(
                                text = "$ts_Count: ",
                                fontSize = 16.sp,
                                color = UICT_see,
                            )
                            OutlinedTextField(
                                value = needGoal,
                                onValueChange = { needGoal = it },
                                label = {
                                    Text(
                                        "$ts_Old: ${habits[habit_statistics_and_edit_x].needGoal.toBestString()}",
                                        fontSize = 12.sp,
                                        color = UICT_no_see
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    color = UICT_see
                                ),
                                shape = RoundedCornerShape(15.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = UICT_see,
                                    unfocusedTextColor = UICT_no_see,
                                    disabledTextColor = UICT_no_see,
                                    focusedContainerColor = UIC_extra_dark,
                                    unfocusedContainerColor = UIC_extra_dark,
                                    disabledContainerColor = UIC_extra_dark,
                                    cursorColor = UICT_see,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                            )
                            OutlinedTextField(
                                value = nameOfUnitsOfDimension,
                                onValueChange = { nameOfUnitsOfDimension = it },
                                label = {
                                    Text(
                                        "$ts_Old: ${habits[habit_statistics_and_edit_x].nameOfUnitsOfDimension}",
                                        fontSize = 12.sp,
                                        color = UICT_no_see
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    color = UICT_see
                                ),
                                shape = RoundedCornerShape(15.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = UICT_see,
                                    unfocusedTextColor = UICT_no_see,
                                    disabledTextColor = UICT_no_see,
                                    focusedContainerColor = UIC_extra_dark,
                                    unfocusedContainerColor = UIC_extra_dark,
                                    disabledContainerColor = UIC_extra_dark,
                                    cursorColor = UICT_see,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                            )
                        }
                        //period
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(spaceX),
                        ) {
                            Text(
                                text = "$ts_Period: ",
                                fontSize = 16.sp,
                                color = UICT_see,
                            )
                            OutlinedTextField(
                                value = needDays,
                                onValueChange = { needDays = it },
                                label = {
                                    Text(
                                        "$ts_Old: ${habits[habit_statistics_and_edit_x].needDays}",
                                        fontSize = 12.sp,
                                        color = UICT_no_see
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    color = UICT_see
                                ),
                                shape = RoundedCornerShape(15.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = UICT_see,
                                    unfocusedTextColor = UICT_no_see,
                                    disabledTextColor = UICT_no_see,
                                    focusedContainerColor = UIC_extra_dark,
                                    unfocusedContainerColor = UIC_extra_dark,
                                    disabledContainerColor = UIC_extra_dark,
                                    cursorColor = UICT_see,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                            )
                            Text(
                                text = " $ts_days",
                                fontSize = 16.sp,
                                color = UICT_see,
                            )
                        }
                    }

                    //Level
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(UIC, RoundedCornerShape(20.dp))
                            .height(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ts_Level,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = UICT_see,
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(spaceY)
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = changeLevel,
                                onCheckedChange = { changeLevel = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = UICT_no_see,
                                    uncheckedColor = UICT_no_see,
                                    checkmarkColor = UICT_see
                                )
                            )
                            Text(
                                text = if (changeLevel) ts_Change_level else ts_No_change_level,
                                fontSize = 16.sp,
                                color = UICT_see
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = changeNeedGoalWithLevel,
                                onCheckedChange = { changeNeedGoalWithLevel = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = UICT_no_see,
                                    uncheckedColor = UICT_no_see,
                                    checkmarkColor = UICT_see
                                )
                            )
                            Text(
                                text = if (changeNeedGoalWithLevel) ts_Change_goal_with_level else ts_No_change_goal_with_level,
                                fontSize = 16.sp,
                                color = UICT_see
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = changeNeedDaysWithLevel,
                                onCheckedChange = { changeNeedDaysWithLevel = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = UICT_no_see,
                                    uncheckedColor = UICT_no_see,
                                    checkmarkColor = UICT_see
                                )
                            )
                            Text(
                                text = if (changeNeedDaysWithLevel) ts_Change_period_for_goal_with_level else ts_No_change_period_for_goal_with_level,
                                fontSize = 16.sp,
                                color = UICT_see
                            )
                        }
                    }

                    //Delete
                    DeleteHabitConfirm(habit_statistics_and_edit_x) {
                        habits[habit_statistics_and_edit_x].delete(habit_statistics_and_edit_x)
                        habits.removeAt(habit_statistics_and_edit_x)
                        habits.save()
                        habit_statistics_and_edit_x = 0
                        viewModel.setStatus(backAppStatus)
                    }
                }
            }
        }
    }
}