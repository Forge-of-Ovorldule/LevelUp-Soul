/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.times
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource

@Composable
fun ColorPickerBox(
    initialColor: Color = Color.Red,
    onColorSelected: (Color) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var currentColor by remember { mutableStateOf(initialColor) }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(currentColor, RoundedCornerShape(20.dp))
            .clickable { showDialog = true }
    )

    if (showDialog) {
        var red by remember { mutableStateOf((currentColor.red * 255).toInt()) }
        var green by remember { mutableStateOf((currentColor.green * 255).toInt()) }
        var blue by remember { mutableStateOf((currentColor.blue * 255).toInt()) }

        AlertDialog(
            containerColor = UIC_extra_dark,
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    ts_Color_picker,
                    fontSize = 16.sp,
                    color = UICT_see
                )
            },
            text = {
                Column {
                    Text(
                        "$ts_Red: $red",
                        fontSize = 16.sp,
                        color = UICT_see
                    )
                    Slider(
                        value = red.toFloat(),
                        onValueChange = {
                            red = it.toInt()
                            currentColor = Color(red, green, blue)
                        },
                        valueRange = 0f..255f,
                        colors = SliderColors(
                            thumbColor = Color(red, green, blue),
                            activeTrackColor = Color(red, green, blue),
                            activeTickColor = Color(red, green, blue),
                            inactiveTrackColor = Color(red, green, blue),
                            inactiveTickColor = Color(red, green, blue),
                            disabledThumbColor = Color.Transparent,
                            disabledActiveTrackColor = Color.Transparent,
                            disabledActiveTickColor = Color.Transparent,
                            disabledInactiveTrackColor = Color.Transparent,
                            disabledInactiveTickColor = Color.Transparent
                        )
                    )
                    Text(
                        "$ts_Green: $green",
                        fontSize = 16.sp,
                        color = UICT_see
                    )
                    Slider(
                        value = green.toFloat(),
                        onValueChange = {
                            green = it.toInt()
                            currentColor = Color(red, green, blue)
                        },
                        valueRange = 0f..255f,
                        colors = SliderColors(
                            thumbColor = Color(red, green, blue),
                            activeTrackColor = Color(red, green, blue),
                            activeTickColor = Color(red, green, blue),
                            inactiveTrackColor = Color(red, green, blue),
                            inactiveTickColor = Color(red, green, blue),
                            disabledThumbColor = Color.Transparent,
                            disabledActiveTrackColor = Color.Transparent,
                            disabledActiveTickColor = Color.Transparent,
                            disabledInactiveTrackColor = Color.Transparent,
                            disabledInactiveTickColor = Color.Transparent
                        )
                    )
                    Text(
                        "$ts_Blue: $blue",
                        fontSize = 16.sp,
                        color = UICT_see
                    )
                    Slider(
                        value = blue.toFloat(),
                        onValueChange = {
                            blue = it.toInt()
                            currentColor = Color(red, green, blue)
                        },
                        valueRange = 0f..255f,
                        colors = SliderColors(
                            thumbColor = Color(red, green, blue),
                            activeTrackColor = Color(red, green, blue),
                            activeTickColor = Color(red, green, blue),
                            inactiveTrackColor = Color(red, green, blue),
                            inactiveTickColor = Color(red, green, blue),
                            disabledThumbColor = Color.Transparent,
                            disabledActiveTrackColor = Color.Transparent,
                            disabledActiveTickColor = Color.Transparent,
                            disabledInactiveTrackColor = Color.Transparent,
                            disabledInactiveTickColor = Color.Transparent
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onColorSelected(currentColor)
                }) {
                    Text(
                        ts_OK,
                        fontSize = 16.sp,
                        color = Color(150, 200, 150)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        ts_Cancel,
                        fontSize = 16.sp,
                        color = Color(200, 150, 150)
                    )
                }
            }
        )
    }
}

@Composable
fun DeleteHabitConfirm(index: Int, onDeleteConfirmed: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(200, 40, 40), RoundedCornerShape(20.dp))
            .height(48.dp)
            .clickable { showDialog = true },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "\uD83D\uDDD1 $ts_Delete",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = UICT_see
        )
    }

    if (showDialog) {
        AlertDialog(
            containerColor = UIC_extra_dark,
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "\uD83D\uDDD1 $ts_Delete_habit_confirm",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UICT_see,
                )
            },
            text = {
                Text(
                    text = "$ts_Are_you_sure_you_want_to_remove_the <${habits[index].nameOfHabit}>",
                    fontSize = 16.sp,
                    color = UICT_see,
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteConfirmed()
                    showDialog = false
                }) {
                    Text(
                        "\uD83D\uDDD1 $ts_Delete",
                        fontSize = 16.sp,
                        color = Color(200, 150, 150)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(
                        "❌ $ts_Cancel",
                        fontSize = 16.sp,
                        color = Color(150, 200, 150),
                    )
                }
            }
        )
    }
}

@Composable
fun DatePickerDialog(
    initialDate: LocalDate,
    onConfirm: (LocalDate) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var day by remember { mutableStateOf(initialDate.dayOfMonth.toString()) }
    var month by remember { mutableStateOf(initialDate.monthNumber.toString()) }
    var year by remember { mutableStateOf(initialDate.year.toString()) }

    IconButton(onClick = {
        showDialog = true
    }) {
        Image(
            painter = painterResource(Res.drawable.calendar),
            contentDescription = ts_Open_calendar,
            modifier = Modifier.size(28.dp),
            colorFilter = ColorFilter.tint(getSoulRealColor())
        )
    }

    if (showDialog) {
        AlertDialog(
            containerColor = UIC_dark,
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    ts_Date_selection,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UICT_see
                )
            },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = day,
                            onValueChange = { day = it },
                            label = {
                                Text(
                                    ts_Day,
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
                                focusedContainerColor = UIC,
                                unfocusedContainerColor = UIC,
                                disabledContainerColor = UIC,
                                cursorColor = UICT_see,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.width(80.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = month,
                            onValueChange = { month = it },
                            label = {
                                Text(
                                    ts_Month,
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
                                focusedContainerColor = UIC,
                                unfocusedContainerColor = UIC,
                                disabledContainerColor = UIC,
                                cursorColor = UICT_see,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.width(80.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = year,
                            onValueChange = { year = it },
                            label = {
                                Text(
                                    ts_Year,
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
                                focusedContainerColor = UIC,
                                unfocusedContainerColor = UIC,
                                disabledContainerColor = UIC,
                                cursorColor = UICT_see,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.width(100.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        val d = day.toIntOrNull()
                        val m = month.toIntOrNull()
                        val y = year.toIntOrNull()
                        if (d != null && m != null && y != null) {
                            try {
                                onConfirm(LocalDate(y, m, d))
                            } catch (_: Exception) {
                                // можно добавить сообщение об ошибке
                            }
                        }
                    },
                    colors = ButtonColors(
                        containerColor = UIC,
                        contentColor = UICT_see,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = "✅ $ts_Apply",
                        fontSize = 16.sp,
                        color = Color(150, 200, 150),
                    )
                }
            },
            dismissButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = { showDialog = false },
                        colors = ButtonColors(
                            containerColor = UIC,
                            contentColor = UICT_see,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Transparent
                        )
                    ) {
                        Text(
                            "❌ $ts_Cancel",
                            fontSize = 16.sp,
                            color = Color(200, 150, 150),
                        )
                    }
                    TextButton(
                        onClick = {
                            showDialog = false
                            onConfirm(
                                Clock.System.now()
                                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                            )
                        },
                        colors = ButtonColors(
                            containerColor = UIC,
                            contentColor = UICT_see,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Transparent
                        )
                    ) {
                        Text(
                            "\uD83D\uDCC5 $ts_Today",
                            fontSize = 16.sp,
                            color = Color(200, 150, 150),
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun SettingsDialog() {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = {
        showDialog = true
    }) {
        Image(
            painter = painterResource(Res.drawable.settings),
            contentDescription = ts_Settings,
            modifier = Modifier.size(28.dp),
            colorFilter = ColorFilter.tint(getSoulRealColor())
        )
    }

    var typeOfColor by remember { mutableStateOf(soul_color_type) }
    var exponent by remember { mutableStateOf(withExponent) }
    var soulName by remember { mutableStateOf(soul_name) }
    var soulColor by remember { mutableStateOf(soul_color) }
    var sortHabitStatisticsSectionsByFrequencyOfUse by remember {
        mutableStateOf(
            sort_habit_statistics_sections_by_frequency_of_use
        )
    }
    var smartSort by remember { mutableStateOf(smart_sort) }

    if (showDialog) {
        AlertDialog(
            containerColor = UIC_dark,
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    ts_Settings,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UICT_see
                )
            },
            text = {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$ts_Soul:",
                            fontSize = 16.sp,
                            color = UICT_see
                        )
                        OutlinedTextField(
                            value = soulName,
                            onValueChange = {
                                soulName = it
                            },
                            label = {
                                Text(
                                    ts_Mr,
                                    fontSize = 12.sp,
                                    color = UICT_no_see
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                            modifier = Modifier.height(55.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var expanded by remember { mutableStateOf(false) }

                        ColorPickerBox(soulColor) {
                            soulColor = it
                        }
                        Column {
                            Button(
                                onClick = { expanded = true },
                                colors = ButtonColors(
                                    containerColor = UIC_extra_dark,
                                    contentColor = UICT_see,
                                    disabledContainerColor = UIC_extra_dark,
                                    disabledContentColor = UICT_no_see
                                )
                            ) {
                                Text(
                                    "$ts_type: ${typeOfColor.name}",
                                    fontSize = 16.sp,
                                    color = UICT_see,
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(Color.Black)
                            ) {
                                TypeOfColorHabits.entries.forEach { mode ->
                                    DropdownMenuItem(
                                        onClick = {
                                            typeOfColor = mode
                                            expanded = false
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = exponent,
                            onCheckedChange = { exponent = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = UICT_no_see,
                                uncheckedColor = UICT_no_see,
                                checkmarkColor = UICT_see
                            )
                        )
                        Text(
                            text = if (exponent) ts_Write_with_an_exponent else ts_Write_without_exponents,
                            fontSize = 16.sp,
                            color = UICT_see
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = sortHabitStatisticsSectionsByFrequencyOfUse,
                            onCheckedChange = { sortHabitStatisticsSectionsByFrequencyOfUse = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = UICT_no_see,
                                uncheckedColor = UICT_no_see,
                                checkmarkColor = UICT_see
                            )
                        )
                        Text(
                            text = ts_Sort_habit_statistics_sections_by_frequency_of_use,
                            fontSize = 16.sp,
                            color = UICT_see
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = smartSort,
                            onCheckedChange = { smartSort = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = UICT_no_see,
                                uncheckedColor = UICT_no_see,
                                checkmarkColor = UICT_see
                            )
                        )
                        Text(
                            text = ts_Smart_sort,
                            fontSize = 16.sp,
                            color = UICT_see
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        withExponent = exponent
                        soul_name = soulName
                        soul_color = soulColor
                        soul_color_type = typeOfColor
                        sort_habit_statistics_sections_by_frequency_of_use = sortHabitStatisticsSectionsByFrequencyOfUse
                        smart_sort = smartSort
                        saveSettings()
                        showDialog = false
                    },
                    colors = ButtonColors(
                        containerColor = UIC,
                        contentColor = UICT_see,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = "✅ $ts_Apply",
                        fontSize = 16.sp,
                        color = Color(150, 200, 150),
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    colors = ButtonColors(
                        containerColor = UIC,
                        contentColor = UICT_see,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Text(
                        "❌ $ts_Cancel",
                        fontSize = 16.sp,
                        color = Color(200, 150, 150),
                    )
                }
            }
        )
    }
}

@Composable
fun DonutChart(
    values: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 40.dp
) {
    val total = values.sum()
    var startAngle = -90f

    Canvas(modifier = modifier) {
        values.forEachIndexed { i, value ->
            val sweepAngle = (value / (if (total != 0f) total else 1f)) * 360f
            drawArc(
                color = colors.getOrElse(i) { UIC_dark_x05 },
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx())
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
fun PPSInfoDialog() {
    var showDialog by remember { mutableStateOf(false) }

    Text(
        text = "\uD83D\uDEC8 $ts_PPS: ",
        fontSize = 16.sp,
        color = UICT_see,
        modifier = Modifier.clickable { showDialog = true }
    )

    if (showDialog) {
        AlertDialog(
            containerColor = UIC_dark,
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "$ts_PPS $ts_Info",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = UICT_see
                )
            },
            text = {
                Text(
                    ts_PPS_means_Progress_Period_Settings_By_default_progress_is_the_,
                    fontSize = 16.sp,
                    color = UICT_see
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonColors(
                        containerColor = UIC,
                        contentColor = UICT_see,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = ts_Close,
                        fontSize = 16.sp,
                        color = Color(150, 150, 200),
                    )
                }
            }
        )
    }
}

@Composable
fun AnimatedLineChart(
    data: List<Float>,
    yMax: Float,
    ySteps: Int = 5,
    lineAndDotColor: Color = Color(0xFF3F51B5),
    modifier: Modifier = Modifier
) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        )
    }

    val backgroundColor = UIC_dark_x2
    val gridColor = UICT_see.copy(alpha = 0.15f)

    BoxWithConstraints(
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        val chartWidth = constraints.maxWidth.toFloat()
        val chartHeight = constraints.maxHeight.toFloat()

        val spacing = chartWidth / (if (data.isNotEmpty()) data.size else 1).coerceAtLeast(1)

        val points = data.mapIndexed { index, value ->
            Offset(
                x = index * spacing + spacing / 2,
                y = chartHeight * (1 - (value / (if (yMax != 0f) yMax else 1f)))
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                for (i in 0..ySteps) {
                    val y = chartHeight * (i.toFloat() / (if (ySteps != 0) ySteps else 1))
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(chartWidth, y),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                    )
                }

                if (points.size >= 2) {
                    for (i in 0 until points.size - 1) {
                        val start = points[i]
                        val end = points[i + 1]
                        drawLine(
                            color = lineAndDotColor,
                            start = start,
                            end = Offset(
                                x = start.x + (end.x - start.x) * animatedProgress.value,
                                y = start.y + (end.y - start.y) * animatedProgress.value
                            ),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }

                points.forEach {
                    drawCircle(
                        color = lineAndDotColor,
                        center = it,
                        radius = 4.dp.toPx()
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedBarChart(
    data: List<BigDecimal>,
    labels: List<String>,
    modifier: Modifier = Modifier,
    maxY: BigDecimal = data.maxOrNull() ?: 1.toBigDecimal(),
    barColor: Color = Color(0xFF4CAF50)
) {
    val scrollState = rememberScrollState()
    val barWidth = 16.dp
    val spacing = 12.dp

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(UIC_dark_x2)
            .horizontalScroll(scrollState)
    ) {
        val totalHeight = maxHeight
        val barMaxHeight = totalHeight - 40.dp

        LaunchedEffect(Unit) {
            scrollState.scrollTo(scrollState.maxValue)
        }

        Row(verticalAlignment = Alignment.Bottom) {
            data.forEachIndexed { index, value ->
                val animatedHeight by animateDpAsState(
                    targetValue = (barMaxHeight * value.toString().toFloat() / (if (maxY.toString()
                            .toFloat() != 0f
                    ) maxY.toString().toFloat() else 1f)).coerceAtLeast(
                        1.dp
                    ),
                    animationSpec = tween(durationMillis = 600),
                    label = "barHeight"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier
                        .padding(horizontal = spacing / 2)
                        .height(totalHeight)
                ) {
                    Text(
                        text = value.toBestString(),
                        fontSize = 11.sp,
                        color = UICT_see,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Box(
                        modifier = Modifier
                            .height(animatedHeight)
                            .width(barWidth)
                            .clip(RoundedCornerShape(4.dp))
                            .background(barColor)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = labels.getOrNull(index) ?: "",
                        fontSize = 11.sp,
                        color = UICT_no_see,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun SoulGrid(
    maxDays: Int,
    states: List<BigDecimal> = listTodayAll(maxDays, 1),
    colorBest: Color,
    modifier: Modifier = Modifier
) {
    var oldestHabit = Habit()
    for (habit in habits) {
        if (habit.startDate.toEpochDays() < oldestHabit.startDate.toEpochDays()) oldestHabit = habit
    }
    val values: List<Int> = listDaysNumbers(oldestHabit)
    val startDate = oldestHabit.startDate

    val backgroundColor = UIC_dark_x2
    val boxSize = 20.dp
    val space = 4.dp
    val horizontalScroll = rememberScrollState()
    val dayOfWeekOffset = (startDate.dayOfWeek.isoDayNumber + 6) % 7
    val paddedValues = List(dayOfWeekOffset) { null } + values.mapIndexed { i, v -> v to states[i] }
    val weeks = paddedValues.chunked(7)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(8.dp)
            .horizontalScroll(horizontalScroll)
    ) {
        LaunchedEffect(Unit) {
            horizontalScroll.scrollTo(horizontalScroll.maxValue)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(space)) {
            for (col in weeks) {
                Column(verticalArrangement = Arrangement.spacedBy(space)) {
                    for (cell in col) {
                        if (cell == null) {
                            Spacer(modifier = Modifier.size(boxSize))
                        } else {
                            val (value, state) = cell
                            val color = Color(
                                ((colorBest.red * state).saveDiv(if (habits.isNotEmpty()) habits.size else 1)).toString()
                                    .toFloat(),
                                ((colorBest.green * state).saveDiv(if (habits.isNotEmpty()) habits.size else 1)).toString()
                                    .toFloat(),
                                ((colorBest.blue * state).saveDiv(if (habits.isNotEmpty()) habits.size else 1)).toString()
                                    .toFloat()
                            )
                            Box(
                                modifier = Modifier
                                    .size(boxSize)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(color),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = value.toString(),
                                    color = if (color.red * 255 + color.green * 255 + color.blue * 255 < 383) Color.White else Color.Black,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ValueSetVector(
    value: Int,
    maxValue: Int,
    subtitle: String,
    label: String,
    isGreen: Boolean = false,
    labelIsBold: Boolean = false,
    onValueChange: (stringValue: String) -> Unit
) {
    var stringValue by remember { mutableStateOf(value.toString()) }

    Box(
        modifier = Modifier.fillMaxWidth().height(50.22.dp / 1.15f)
            .background(UIC_x2green, RoundedCornerShape(88.89.dp / 1.15f))
            .clip(RoundedCornerShape(88.89.dp / 1.15f))
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(value.toFloat() / maxValue.toFloat())
                .height(50.22.dp / 1.15f)
                .background(
                    Brush.horizontalGradient(listOf(UIC_x2green, UIC_x2green_x1o5white)),
                    RoundedCornerShape(88.89.dp / 1.15f)
                )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = subtitle,
                fontFamily = JetBrainsFont(),
                fontWeight = FontWeight.Thin,
                fontSize = 12.8.sp / 1.15f,
                color = UICT_no_see,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(4.dp / 1.15f))
            BasicTextField(
                value = stringValue,
                onValueChange = {
                    stringValue = it
                    onValueChange(it)
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = JetBrainsFont(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp / 1.15f,
                    color = if (isGreen) UIC_green else UICT_see,
                    textAlign = TextAlign.End
                ),
                singleLine = true,
                decorationBox = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            it()
                            Text(
                                text = "/$maxValue",
                                fontFamily = JetBrainsFont(),
                                fontWeight = FontWeight.Thin,
                                fontSize = 9.4.sp / 1.15f,
                                color = UICT_no_see,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = " $label" + " ".repeat(if (10 - stringValue.length > 0) 10 - stringValue.length else 0),
                                fontFamily = JetBrainsFont(),
                                fontWeight = if (labelIsBold) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 16.sp / 1.15f,
                                color = if (isGreen) UIC_green else UICT_see,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}