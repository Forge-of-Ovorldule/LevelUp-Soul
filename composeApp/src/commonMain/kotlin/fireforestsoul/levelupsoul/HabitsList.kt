/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal

@Composable
fun HabitsListContent(verticalScrollState: ScrollState, viewModel: AppViewModel) {
    backAppStatus = AppStatus.HABITS_LIST_UPDATER
    val sortedHabits = MutableList(habits.size) { it }
    sortedHabits.sortSystem()

    Box(
        modifier = Modifier.fillMaxSize()
            .background(UIC_dark)
            .verticalScroll(verticalScrollState)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(13.4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.51.dp)
        ) {
            for (x in 0 until habits.size) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(101.15.dp)
                        .background(UIC, RoundedCornerShape(13.03.dp))
                        .clickable {
                            habit_statistics_and_edit_x = sortedHabits[x]
                            viewModel.setStatus(AppStatus.HABIT_STATISTICS)
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.48.dp, 15.71.dp, 14.94.dp, 11.49.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.26.dp)
                    ) {
                        val seeColorByX = seeColorByIndex(sortedHabits[x])

                        Text(
                            text = habits[sortedHabits[x]].iconChar,
                            textAlign = TextAlign.Center,
                            color = seeColorByX,
                            modifier = Modifier.size(57.47.dp),
                            maxLines = 1,
                            fontSize = 45.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            style = TextStyle(shadow = Shadow(blurRadius = 1f))
                        )
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = habits[sortedHabits[x]].nameOfHabit,
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.W500,
                                color = UICT_see,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(shadow = Shadow(blurRadius = 1f))

                            )

                            var inputText by remember { mutableStateOf("") }
                            var showDialog by remember { mutableStateOf(false) }

                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .height(48.dp)
                                    .clickable {
                                        showDialog = true
                                    },
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "$ts_Completed ${habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].today.toBestString()} ${habits[sortedHabits[x]].nameOfUnitsOfDimension}",
                                    color = UICT_no_see,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = TextStyle(shadow = Shadow(blurRadius = 1f))
                                )
                                BoxWithConstraints(
                                    modifier = Modifier.fillMaxWidth()
                                        .height(5.75.dp)
                                        .background(
                                            if (habits[sortedHabits[x]].typeOfGoalHabits == TypeOfGoalHabits.AT_LEAST) UIC_light
                                            else seeColorByX,
                                            RoundedCornerShape(2.88.dp)
                                        )
                                        .shadow(5.dp)
                                ) {
                                    val needToday =
                                        habits[sortedHabits[x]].needGoal - habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].totalOfAPeriod + habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].today
                                    Box(
                                        modifier = Modifier.fillMaxHeight()
                                            .background(
                                                if (habits[sortedHabits[x]].typeOfGoalHabits == TypeOfGoalHabits.AT_LEAST) seeColorByX
                                                else UIC_light,
                                                RoundedCornerShape(2.88.dp)
                                            )
                                            .width(
                                                if (habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].totalOfAPeriod < habits[sortedHabits[x]].needGoal)
                                                    maxWidth * (1.toBigDecimal()
                                                        .saveDiv(if (needToday != BigDecimal.ZERO) needToday else BigDecimal.ONE) * habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].today).toString()
                                                        .toFloat()
                                                else
                                                    maxWidth
                                            )
                                    )
                                }
                                Text(
                                    text = if (habits[sortedHabits[x]].typeOfGoalHabits == TypeOfGoalHabits.AT_LEAST)
                                        if (habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].totalOfAPeriod < habits[sortedHabits[x]].needGoal)
                                            "$ts_You_need ${(habits[sortedHabits[x]].needGoal - habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].totalOfAPeriod).toBestString()} ${habits[sortedHabits[x]].nameOfUnitsOfDimension} $ts_more"
                                        else
                                            ts_Its_all_done
                                    else
                                        if (habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].totalOfAPeriod <= habits[sortedHabits[x]].needGoal)
                                            "$ts_You_can_have ${(habits[sortedHabits[x]].needGoal - habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].totalOfAPeriod).toBestString()} ${habits[sortedHabits[x]].nameOfUnitsOfDimension} $ts_more"
                                        else
                                            ts_You_failed,
                                    color = UICT_no_see,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = TextStyle(shadow = Shadow(blurRadius = 1f))
                                )
                            }
                            if (showDialog) {
                                AlertDialog(
                                    containerColor = UIC,
                                    onDismissRequest = { showDialog = false },
                                    title = {
                                        Text(
                                            text = "$ts_Do_you_want_to_set_a_value_for \"${habits[sortedHabits[x]].nameOfHabit}\"?",
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                            color = UICT_see
                                        )
                                    },
                                    text = {
                                        OutlinedTextField(
                                            value = inputText,
                                            onValueChange = { inputText = it },
                                            label = {
                                                Text(
                                                    "$ts_Old: ${habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].today.toBestString()}",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = UICT_no_see
                                                )
                                            },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            singleLine = true,
                                            textStyle = TextStyle(
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = UICT_see
                                            ),
                                            shape = RoundedCornerShape(15.dp),
                                            colors = TextFieldDefaults.colors(
                                                focusedTextColor = UICT_see,
                                                unfocusedTextColor = UICT_no_see,
                                                disabledTextColor = UICT_no_see,
                                                focusedContainerColor = UIC_dark,
                                                unfocusedContainerColor = UIC_dark,
                                                disabledContainerColor = UIC_dark,
                                                cursorColor = UICT_see,
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent,
                                                disabledIndicatorColor = Color.Transparent
                                            )
                                        )
                                    },
                                    dismissButton = {
                                        Text(
                                            text = "❌ $ts_Cancel",
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                            color = Color(200, 150, 150),
                                            modifier = Modifier.clickable {
                                                showDialog = false
                                                viewModel.setStatus(AppStatus.HABITS_LIST_UPDATER)
                                            }
                                        )
                                    },
                                    confirmButton = {
                                        Text(
                                            text = "✅ $ts_Confirm",
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                            color = Color(150, 200, 150),
                                            modifier = Modifier.clickable {
                                                val value = inputText.toDoubleOrNull()
                                                if (value != null) {
                                                    habits[sortedHabits[x]].habitDay[habits[sortedHabits[x]].habitDay.size - 1].today =
                                                        inputText.toBigDecimal()
                                                    habits[sortedHabits[x]].saveHabitDays(sortedHabits[x])
                                                    habits[sortedHabits[x]].update(sortedHabits)
                                                }
                                                showDialog = false
                                                viewModel.setStatus(AppStatus.HABITS_LIST_UPDATER)
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}