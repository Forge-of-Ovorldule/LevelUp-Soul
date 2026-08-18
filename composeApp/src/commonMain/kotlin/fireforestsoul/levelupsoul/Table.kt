/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.datetime.*
import kotlin.math.max
import kotlin.math.min

expect fun export()
expect fun import(onImported: () -> Unit)

@Composable
fun TableContent(
    viewModel: AppViewModel,
    verticalScroll: ScrollState,
    horizontalScroll: ScrollState,
    countdownDate: LocalDate,
) {
    val sortedHabits = MutableList(LocalSaveManager.data.habits.size) { it }
    sortedHabits.sortSystem()

    LocalSaveManager.data.backAppStatus = AppStatus.TABLE_UPDATER

    val firstCellSizeX = 200.dp
    val firstCellSizeY = 40.dp
    val nextCellSizeX = 45.dp
    val spacedCell = 3.dp
    val sizeOfBorder = 1.dp
    val roundedBorder = 7.5.dp
    val firstSellFontSize = 16.sp
    val firstSellSmallFontSize = 9.sp
    val dataSellFontSize = 11.sp
    var maxDays = 0
    for (habit in LocalSaveManager.data.habits) {
        maxDays = max(habit.totalDays(), maxDays)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(UIC_dark)
    ) {
        val maxCellX =
            ((maxWidth - firstCellSizeX) / (if (nextCellSizeX + spacedCell != 0.dp) (nextCellSizeX + spacedCell) else 1.dp)).toInt()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacedCell),
            modifier = Modifier.verticalScroll(verticalScroll)
        ) {
            //first column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacedCell),
                modifier = Modifier.width(firstCellSizeX)
            ) {
                Box(
                    modifier = Modifier.size(firstCellSizeX, firstCellSizeY),
                    contentAlignment = Alignment.Center
                ) {}
                for (y in LocalSaveManager.data.habits.indices) {
                    var seeColor by remember { mutableStateOf(LocalSaveManager.data.soulColor) }
                    var noSeeColor by remember {
                        mutableStateOf(
                            seeColor.multiply(
                                0.5f,
                                0.5f,
                                0.5f
                            )
                        )
                    }

                    LaunchedEffect(sortedHabits[y], progress(sortedHabits[y])) {
                        calculateProgressiveColor(
                            index = sortedHabits[y],
                            onColorUpdate = { newColor ->
                                seeColor = newColor
                                noSeeColor = newColor.multiply(0.5f, 0.5f, 0.5f)
                            }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(firstCellSizeX, firstCellSizeY)
                            .border(
                                sizeOfBorder,
                                color = noSeeColor,
                                shape = RoundedCornerShape(roundedBorder)
                            )
                            .clickable {
                                habit_statistics_and_edit_x = sortedHabits[y]
                                viewModel.setStatus(AppStatus.HABIT_STATISTICS)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                DonutChart(
                                    values = listOf(
                                        progress(sortedHabits[y]),
                                        1f - progress(sortedHabits[y])
                                    ),
                                    colors = listOf(seeColor, noSeeColor),
                                    modifier = Modifier
                                        .size(24.25.dp)
                                        .padding(start = 1.75.dp, top = 1.75.dp),
                                    strokeWidth = 3.5.dp
                                )
                                Text(
                                    text = "${LocalSaveManager.data.habits[sortedHabits[y]].level}",
                                    fontSize = 11.sp,
                                    color = seeColor
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextWithDeployableEllipsis(
                                    backgroundColor = UIC_black,
                                    newStatusBarInfo = changeStatusBarInfo(
                                        backgroundColor = UIC_dark,
                                        downPanelSize = 48.dp,
                                        isProcessed = false
                                    ),
                                    hazeState = null,
                                    text = LocalSaveManager.data.habits[sortedHabits[y]].nameOfHabit,
                                    color = seeColor,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = jetBrainsFont(),
                                    fontSize = firstSellFontSize / 1.15f
                                )
                                val needOrCanMore =
                                    LocalSaveManager.data.habits[sortedHabits[y]].numericalGoal - LocalSaveManager.data.habits[sortedHabits[y]].totalOfAPeriod(
                                        dateNow()
                                    )
                                if (needOrCanMore > BigDecimal.ZERO) {
                                    Text(
                                        text = if (LocalSaveManager.data.habits[sortedHabits[y]].typeOfGoal == TypeOfGoalHabit.AT_LEAST)
                                            "$ts_Need ${needOrCanMore.toBestString()} $ts_more"
                                        else "$ts_You_can_have ${needOrCanMore.toBestString()} $ts_more",
                                        color = noSeeColor,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = firstSellSmallFontSize,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            //main table body
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(horizontalScroll)
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(spacedCell),
                ) {
                    //dates
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacedCell),
                        modifier = Modifier.height(firstCellSizeY)
                    ) {
                        for (x in 0 until max(maxCellX, 10)) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.size(nextCellSizeX, firstCellSizeY)
                            ) {
                                Text(
                                    text = (countdownDate.minus(
                                        x,
                                        DateTimeUnit.DAY
                                    )).day.toString(),
                                    color = UICT_no_see,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = dataSellFontSize
                                )
                                Text(
                                    text = (countdownDate.minus(
                                        x,
                                        DateTimeUnit.DAY
                                    )).dayOfWeek.toString().take(3),
                                    color = UICT_no_see,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = dataSellFontSize
                                )
                            }
                        }
                    }
                    //results
                    for (y in LocalSaveManager.data.habits.indices) {
                        var seeColor by remember { mutableStateOf(LocalSaveManager.data.soulColor) }
                        var noSeeColor by remember {
                            mutableStateOf(
                                seeColor.multiply(
                                    0.5f,
                                    0.5f,
                                    0.5f
                                )
                            )
                        }

                        LaunchedEffect(sortedHabits[y], progress(sortedHabits[y])) {
                            calculateProgressiveColor(
                                index = sortedHabits[y],
                                onColorUpdate = { newColor ->
                                    seeColor = newColor
                                    noSeeColor = newColor.multiply(0.5f, 0.5f, 0.5f)
                                }
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .height(firstCellSizeY)
                                .border(
                                    sizeOfBorder,
                                    color = noSeeColor,
                                    shape = RoundedCornerShape(roundedBorder)
                                )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(spacedCell),
                            ) {
                                for (x in 0 until min(
                                    maxDays + countdownDate.toEpochDays() - kotlin.time.Clock.System.now()
                                        .toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays(),
                                    max(maxCellX, 10).toLong()
                                )) {
                                    val xDay =
                                        dateNow().minusDays(x.toInt())
                                            .plusDays((countdownDate.toEpochDays() - dateNow().toEpochDays()).toInt())
                                    Box(
                                        modifier = Modifier
                                            .width(nextCellSizeX)
                                            .height(firstCellSizeY * 7 / 16),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (xDay < dateNow()) {
                                            var inputText by remember { mutableStateOf("") }
                                            var showDialog by remember { mutableStateOf(false) }

                                            Text(
                                                text = (LocalSaveManager.data.habits[sortedHabits[y]].habitDay[xDay]?.today
                                                    ?: BigDecimal.ZERO).toBestString(),
                                                color = if (LocalSaveManager.data.habits[sortedHabits[y]].correctly(
                                                        xDay
                                                    )
                                                ) seeColor else noSeeColor,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = firstSellFontSize,
                                                modifier = Modifier.clickable {
                                                    showDialog = true
                                                }
                                            )

                                            if (showDialog) {
                                                AlertDialog(
                                                    containerColor = UIC,
                                                    onDismissRequest = { showDialog = false },
                                                    title = {
                                                        Text(
                                                            text = "$ts_Do_you_want_to_set_a_value_for ${xDay.month} ${xDay.day}, ${xDay.year} $ts_for_habit ${LocalSaveManager.data.habits[sortedHabits[y]].nameOfHabit}?",
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
                                                                    "$ts_Old: ${(LocalSaveManager.data.habits[sortedHabits[y]].habitDay[xDay]?.today ?: BigDecimal.ZERO).toBestString()}",
                                                                    fontSize = 12.sp,
                                                                    fontWeight = FontWeight.Normal,
                                                                    color = UICT_no_see
                                                                )
                                                            },
                                                            keyboardOptions = KeyboardOptions(
                                                                keyboardType = KeyboardType.Number
                                                            ),
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
                                                                viewModel.setStatus(AppStatus.TABLE_UPDATER)
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
                                                                val value =
                                                                    inputText.toDoubleOrNull()
                                                                if (value != null) {
                                                                    LocalSaveManager.data.habits[sortedHabits[y]].habitDay[xDay] =
                                                                        HabitDay(inputText.toBigDecimal())
                                                                    LocalSaveManager.save()
                                                                    LocalSaveManager.data.habits[sortedHabits[y]].update(
                                                                        sortedHabits
                                                                    )
                                                                }
                                                                showDialog = false
                                                                viewModel.setStatus(AppStatus.TABLE_UPDATER)
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier.padding(spacedCell * 2)
                            ) {
                                Text(
                                    text = LocalSaveManager.data.habits[sortedHabits[y]].nameOfUnitsOfDimension,
                                    color = noSeeColor,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = firstSellSmallFontSize
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}