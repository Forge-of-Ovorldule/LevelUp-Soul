/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import kotlin.math.max

@Composable
fun SoulStatisticsContent() {
    val verticalScroll = rememberScrollState()
    val spaceCell = 8.dp

    var maxDays = 0
    for (habit in habits) {
        maxDays = max(habit.habitDay.size, maxDays)
    }
    val seeColor = getSeeSoulColor()
    val noSeeColor = getNoSeeSoulColor()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UIC_dark)
            .verticalScroll(verticalScroll)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = spaceCell, horizontal = spaceCell / 2),
            verticalArrangement = Arrangement.spacedBy(spaceCell),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var progressPeriod by remember { mutableStateOf(maxDays.toString()) }
            var progressPeriodSetting by remember { mutableStateOf(maxDays) }
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PPSInfoDialog()
                OutlinedTextField(
                    value = progressPeriod,
                    onValueChange = { progressPeriod = it },
                    label = {
                        Text(
                            "$ts_For_all_time 0",
                            fontSize = 12.sp,
                            color = UICT_no_see
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        color = UICT_see
                    ),
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = UICT_see,
                        unfocusedTextColor = UICT_no_see,
                        disabledTextColor = UICT_no_see,
                        focusedContainerColor = UIC_dark_x2,
                        unfocusedContainerColor = UIC_dark_x2,
                        disabledContainerColor = UIC_dark_x2,
                        cursorColor = UICT_see,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.size(125.dp, 55.dp)
                )
                Text(
                    text = ts_Set,
                    fontSize = 16.sp,
                    color = UICT_see,
                    modifier = Modifier
                        .background(Color(25, 50, 25), RoundedCornerShape(15.dp))
                        .clickable {
                            if (progressPeriod.toIntOrNull() != null) {
                                progressPeriodSetting = if (progressPeriod.toInt() == 0)
                                    maxDays
                                else
                                    progressPeriod.toInt()
                            }
                        }
                        .padding(12.5.dp)
                )
            }

            /**
             * Progress: [progress] `progressDay` `progressWeek` `progressMonth` `progressYear`
             */
            val progress = progressAll(maxDays, progressPeriodSetting)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(UIC, RoundedCornerShape(20.dp))
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ts_Progress,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UICT_see,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            val progressDay =
                                plusProgressAll(maxDays, 1, progressPeriodSetting)
                            DonutChart(
                                modifier = Modifier.size(60.dp),
                                values = listOf(progressDay, 1 - progressDay),
                                colors = listOf(seeColor, noSeeColor),
                                strokeWidth = 5.dp
                            )
                            Text(
                                text = (if (progressDay > 0) "+" else "") + "${(progressDay * 100).toInt()}%",
                                fontSize = 12.sp,
                                color = seeColor
                            )
                        }
                        Text(
                            text = ts_day,
                            fontSize = 12.sp,
                            color = UICT_see
                        )
                        Box(contentAlignment = Alignment.Center) {
                            val progressWeek =
                                plusProgressAll(maxDays, 7, progressPeriodSetting)
                            DonutChart(
                                modifier = Modifier.size(60.dp),
                                values = listOf(progressWeek, 1 - progressWeek),
                                colors = listOf(seeColor, noSeeColor),
                                strokeWidth = 5.dp
                            )
                            Text(
                                text = (if (progressWeek > 0) "+" else "") + "${(progressWeek * 100).toInt()}%",
                                fontSize = 12.sp,
                                color = seeColor
                            )
                        }
                        Text(
                            text = ts_week,
                            fontSize = 12.sp,
                            color = UICT_see
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            DonutChart(
                                modifier = Modifier.size(125.dp),
                                values = listOf(progress, 1 - progress),
                                colors = listOf(seeColor, noSeeColor),
                                strokeWidth = 10.dp
                            )
                            Image(
                                painter = painterResource(Res.drawable.soul),
                                contentDescription = ts_Your_soul,
                                modifier = Modifier.size(75.dp),
                                colorFilter = ColorFilter.tint(seeColor),
                            )
                        }
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 16.sp,
                            color = seeColor
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            val progressMonth =
                                plusProgressAll(maxDays, 30, progressPeriodSetting)
                            DonutChart(
                                modifier = Modifier.size(60.dp),
                                values = listOf(progressMonth, 1 - progressMonth),
                                colors = listOf(seeColor, noSeeColor),
                                strokeWidth = 5.dp
                            )
                            Text(
                                text = (if (progressMonth > 0) "+" else "") + "${(progressMonth * 100).toInt()}%",
                                fontSize = 12.sp,
                                color = seeColor
                            )
                        }
                        Text(
                            text = ts_month,
                            fontSize = 12.sp,
                            color = UICT_see
                        )
                        Box(contentAlignment = Alignment.Center) {
                            val progressYear =
                                plusProgressAll(maxDays, 365, progressPeriodSetting)
                            DonutChart(
                                modifier = Modifier.size(60.dp),
                                values = listOf(progressYear, 1 - progressYear),
                                colors = listOf(seeColor, noSeeColor),
                                strokeWidth = 5.dp
                            )
                            Text(
                                text = (if (progressYear > 0) "+" else "") + "${(progressYear * 100).toInt()}%",
                                fontSize = 12.sp,
                                color = seeColor
                            )
                        }
                        Text(
                            text = ts_year,
                            fontSize = 12.sp,
                            color = UICT_see
                        )
                    }
                }
            }

            /**
             * Level changed
             */
            if (kotlin.time.Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays() - soul_last_level_change_date.toEpochDays() >= 20
            ) {
                var goodProgress = 0
                if (progressAll(maxDays) >= 0.8) {
                    for (x in (maxDays - 20) until maxDays) {
                        if (x >= 0) {
                            if (progressAll(maxDays, endIndex = x) >= 0.8) {
                                goodProgress++
                            }
                        }
                    }
                    if (goodProgress == 20) {
                        soul_level++
                        soul_last_level_change_date =
                            kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                    }
                } else if (progressAll(maxDays) <= 0.2) {
                    for (x in (maxDays - 20) until maxDays) {
                        if (x >= 0) {
                            if (progressAll(maxDays, endIndex = x) <= 0.2) {
                                goodProgress++
                            }
                        }
                    }
                    if (goodProgress == 20) {
                        soul_level--
                        soul_last_level_change_date =
                            kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                    }
                }
            }

            /** Level */
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
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var goodProgress by remember { mutableStateOf(0f) }
                    var progressUp by remember { mutableStateOf(false) }

                    goodProgress = 0f
                    if (progressAll(maxDays) >= 0.8) {
                        for (x in (maxDays - (kotlin.time.Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays() - soul_last_level_change_date.toEpochDays())) until maxDays) {
                            if (x >= 0) {
                                if (progressAll(maxDays, endIndex = x.toInt()) >= 0.8) {
                                    goodProgress++
                                } else {
                                    goodProgress = 0f
                                }
                            }
                        }
                        progressUp = true
                    } else if (progressAll(maxDays) <= 0.2) {
                        for (x in (maxDays - (kotlin.time.Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date.toEpochDays() - soul_last_level_change_date.toEpochDays())) until maxDays) {
                            if (x >= 0) {
                                if (progressAll(maxDays, endIndex = x.toInt()) <= 0.2) {
                                    goodProgress++
                                } else {
                                    goodProgress = 0f
                                }
                            }
                        }
                        progressUp = false
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            DonutChart(
                                modifier = Modifier.size(125.dp),
                                values = if (progressUp) listOf(
                                    goodProgress / 20f,
                                    1f - goodProgress / 20f
                                ) else listOf(1f - goodProgress / 20f, goodProgress / 20f),
                                colors = if (progressUp) listOf(seeColor, noSeeColor) else listOf(
                                    noSeeColor,
                                    seeColor
                                ),
                                strokeWidth = 10.dp
                            )
                            Text(
                                text = if (goodProgress == 0f) "$soul_level" else (if (progressUp) "$soul_level ⬆" else "$soul_level ⬇"),
                                fontSize = 16.sp,
                                fontWeight = if (goodProgress == 0f) FontWeight.Normal else FontWeight.Bold,
                                color = if (goodProgress == 0f) UICT_see else (if (progressUp) Color.Green else Color.Red)
                            )
                        }
                    }
                }
            }

            /**
             * Progress graph: `period`>`periodSetting` `step`>`stepSetting`
             */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(UIC, RoundedCornerShape(20.dp))
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ts_Progress_graph,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UICT_see,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var period by remember { mutableStateOf(maxDays.toString()) }
                    var step by remember { mutableStateOf(1.toString()) }
                    var periodSetting by remember { mutableStateOf(maxDays) }
                    var stepSetting by remember { mutableStateOf(1) }

                    AnimatedLineChart(
                        data = listProgressAll(
                            maxDays,
                            periodSetting,
                            stepSetting,
                            progressPeriodSetting
                        ),
                        yMax = 1f,
                        lineAndDotColor = seeColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(16.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = period,
                            onValueChange = { period = it },
                            label = {
                                Text(
                                    "$ts_Period:",
                                    fontSize = 12.sp,
                                    color = UICT_no_see
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = UICT_see
                            ),
                            shape = RoundedCornerShape(15.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = UICT_see,
                                unfocusedTextColor = UICT_no_see,
                                disabledTextColor = UICT_no_see,
                                focusedContainerColor = UIC_dark_x2,
                                unfocusedContainerColor = UIC_dark_x2,
                                disabledContainerColor = UIC_dark_x2,
                                cursorColor = UICT_see,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.size(125.dp, 55.dp)
                        )
                        OutlinedTextField(
                            value = step,
                            onValueChange = { step = it },
                            label = {
                                Text(
                                    "$ts_Step:",
                                    fontSize = 12.sp,
                                    color = UICT_no_see
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = UICT_see
                            ),
                            shape = RoundedCornerShape(15.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = UICT_see,
                                unfocusedTextColor = UICT_no_see,
                                disabledTextColor = UICT_no_see,
                                focusedContainerColor = UIC_dark_x2,
                                unfocusedContainerColor = UIC_dark_x2,
                                disabledContainerColor = UIC_dark_x2,
                                cursorColor = UICT_see,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.size(125.dp, 55.dp)
                        )
                        Text(
                            text = ts_Set,
                            fontSize = 16.sp,
                            color = UICT_see,
                            modifier = Modifier
                                .background(Color(25, 50, 25), RoundedCornerShape(15.dp))
                                .clickable {
                                    if (period.toIntOrNull() != null) {
                                        periodSetting = if (period.toInt() == 0)
                                            maxDays
                                        else
                                            period.toInt()
                                    }
                                    if (step.toIntOrNull() != null) {
                                        stepSetting = if (step.toInt() <= 0)
                                            1
                                        else
                                            step.toInt()
                                    }
                                }
                                .padding(12.5.dp)
                        )
                    }
                }
            }

            /**
             * Results
             * > BarChart
             * > Grid
             */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(UIC, RoundedCornerShape(20.dp))
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ts_Results,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UICT_see,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    /**
                     * BarChart: [step]>`stepSetting`
                     */
                    var step by remember { mutableStateOf(1.toString()) }
                    var stepSetting by remember { mutableStateOf(1) }

                    AnimatedBarChart(
                        data = listTodayAll(maxDays, stepSetting),
                        labels = listTodayDatesAll(maxDays, stepSetting),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(16.dp),
                        barColor = seeColor
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = step,
                            onValueChange = { step = it },
                            label = {
                                Text(
                                    "$ts_Step:",
                                    fontSize = 12.sp,
                                    color = UICT_no_see
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                color = UICT_see
                            ),
                            shape = RoundedCornerShape(15.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = UICT_see,
                                unfocusedTextColor = UICT_no_see,
                                disabledTextColor = UICT_no_see,
                                focusedContainerColor = UIC_dark_x2,
                                unfocusedContainerColor = UIC_dark_x2,
                                disabledContainerColor = UIC_dark_x2,
                                cursorColor = UICT_see,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.size(125.dp, 55.dp)
                        )
                        Text(
                            text = ts_Set,
                            fontSize = 16.sp,
                            color = UICT_see,
                            modifier = Modifier
                                .background(Color(25, 50, 25), RoundedCornerShape(15.dp))
                                .clickable {
                                    if (step.toIntOrNull() != null) {
                                        stepSetting = if (step.toInt() <= 0)
                                            1
                                        else
                                            step.toInt()
                                    }
                                }
                                .padding(12.5.dp)
                        )
                    }

                    /**
                     * HabitGrid
                     */
                    SoulGrid(
                        maxDays,
                        colorBest = seeColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}