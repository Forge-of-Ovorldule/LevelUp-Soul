/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.times
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

var habit_statistics_and_edit_x = 0
private var pps_for_habit_statistic = 0

@Composable
fun HabitStatistics(viewModel: AppViewModel) {
    var seeColorByHabitAndStatisticsEditX by remember { mutableStateOf(soul_color) }

    LaunchedEffect(habit_statistics_and_edit_x) {
        calculateProgressiveColor(
            index = habit_statistics_and_edit_x,
            onColorUpdate = { newColor ->
                seeColorByHabitAndStatisticsEditX = newColor
            }
        )
    }

    var startStatus = listPointsOfHabitStatistic.maxBy { it.value }.key
    if (startStatus == HabitStatisticsStatus.LEVEL && !habits[habit_statistics_and_edit_x].changeLevel) startStatus =
        HabitStatisticsStatus.GOAL
    listPointsOfHabitStatistic[startStatus] = listPointsOfHabitStatistic.getValue(startStatus) + 0.25f

    var habitStatisticsStatus by remember { mutableStateOf(if (sort_habit_statistics_sections_by_frequency_of_use) startStatus else HabitStatisticsStatus.GOAL) }
    var progressPeriodSetting by remember { mutableStateOf(habits[habit_statistics_and_edit_x].habitDay.size) }
    pps_for_habit_statistic = progressPeriodSetting

    val sortedStatuses = remember {
        val filtered = HabitStatisticsStatus.entries.filter {
            it != HabitStatisticsStatus.LEVEL || habits[habit_statistics_and_edit_x].changeLevel
        }

        if (sort_habit_statistics_sections_by_frequency_of_use) {
            filtered.sortedByDescending { listPointsOfHabitStatistic[it] ?: 0f }
        } else {
            filtered
        }
    }

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = sortedStatuses.indexOf(habitStatisticsStatus).coerceAtLeast(0),
        pageCount = { sortedStatuses.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        habitStatisticsStatus = sortedStatuses[pagerState.currentPage]
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            Modifier.fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(backgroundUp)
        )
        Box(
            Modifier.fillMaxSize()
                .background(backgroundDown)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .background(Brush.verticalGradient(listOf(UIC_dark, UIC_black)))
    ) {
        Scaffold(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(UIC_dark, UIC_black))),
            topBar = {
                var maxHeightBox by remember { mutableStateOf(0.dp) }

                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth()
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            maxHeightBox = placeable.height.toDp()
                            layout(placeable.width, placeable.height) {
                                placeable.place(0, 0)
                            }
                        }
                ) {
                    val maxWidthBox = maxWidth
                    val hazeState = rememberHazeState()

                    Box(
                        modifier = Modifier.hazeSource(hazeState)
                    ) {
                        Box(
                            modifier = Modifier.padding(
                                start = maxWidthBox / 1080 * 801.79f,
                                top = maxHeightBox / 536 * 54.07f
                            )
                        ) {
                            Text(
                                text = habits[habit_statistics_and_edit_x].iconChar,
                                color = seeColorByHabitAndStatisticsEditX,
                                fontSize = 60.sp / 1.15f,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.rotate(-28.79f),
                                fontFamily = JetBrainsFont()
                            )
                        }
                        Box(
                            modifier = Modifier.padding(
                                start = maxWidthBox / 1080 * 51.32f,
                                top = maxHeightBox / 536 * 181.29f
                            )
                        ) {
                            Text(
                                text = habits[habit_statistics_and_edit_x].iconChar,
                                color = seeColorByHabitAndStatisticsEditX,
                                fontSize = 51.2.sp / 1.15f,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.rotate(33.94f),
                                fontFamily = JetBrainsFont()
                            )
                        }
                        Box(
                            modifier = Modifier.padding(
                                start = maxWidthBox / 1080 * 677.25f,
                                top = maxHeightBox / 536 * 288.99f
                            )
                        ) {
                            Text(
                                text = habits[habit_statistics_and_edit_x].iconChar,
                                color = seeColorByHabitAndStatisticsEditX,
                                fontSize = 34.sp / 1.15f,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.rotate(-17.23f),
                                fontFamily = JetBrainsFont()
                            )
                        }
                    }

                    IconButton(
                        { viewModel.setStatus(backAppStatus) },
                        modifier = Modifier.padding(14.dp / 1.15f, 12.dp / 1.15f)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.back),
                            contentDescription = ts_Go_back,
                            colorFilter = ColorFilter.tint(UIC, BlendMode.Modulate),
                            modifier = Modifier.size(30.8.dp / 1.15f)
                                .clip(RoundedCornerShape(15.4.dp / 1.15f))
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 52.dp / 1.15f, bottom = 10.8.dp / 1.15f),
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextWithDeployableEllipsis(
                            backgroundColor = UIC_black,
                            newStatusBarInfo = changeStatusBarInfo(
                                backgroundColor = Color.Black,
                                downPanelSize = 48.67.dp,
                                isProcessed = false
                            ),
                            hazeState = hazeState,
                            contentBefore = {
                                Text(
                                    text = "«",
                                    color = UICT_see,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = JetBrainsFont(),
                                    fontSize = 29.57.sp
                                )
                            },
                            text = habits[habit_statistics_and_edit_x].nameOfHabit,
                            contentAfter = {
                                Text(
                                    text = "»",
                                    color = UICT_see,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = JetBrainsFont(),
                                    fontSize = 29.57.sp
                                )
                            },
                            color = UICT_see,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = JetBrainsFont(),
                            fontSize = 29.57.sp
                        )
                        Text(
                            text = ts_statistic,
                            color = UICT_no_see,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = JetBrainsFont(),
                            fontSize = 14.4.sp / 1.15f
                        )
                        Spacer(modifier = Modifier.height(57.6.dp / 1.15f))
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 66.4.dp / 1.15f),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Box(
                                modifier = Modifier.size(maxWidthBox * 0.33f, 40.dp / 1.15f)
                                    .background(
                                        seeColorByHabitAndStatisticsEditX,
                                        RoundedCornerShape(27.2.dp / 1.15f)
                                    )
                                    .border(
                                        0.7.dp,
                                        seeColorByHabitAndStatisticsEditX,
                                        RoundedCornerShape(27.2.dp / 1.15f)
                                    )
                                    .padding(horizontal = 6.43.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ts_Statistic,
                                    color = checkBackgroundBright(
                                        seeColorByHabitAndStatisticsEditX,
                                        UICT_see
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontFamily = JetBrainsFont(),
                                    fontSize = 16.7.sp
                                )
                            }
                            Box(
                                modifier = Modifier.size(maxWidthBox * 0.33f, 40.dp / 1.15f)
                                    .background(Color.Transparent)
                                    .border(
                                        0.7.dp,
                                        seeColorByHabitAndStatisticsEditX,
                                        RoundedCornerShape(27.2.dp / 1.15f)
                                    )
                                    .clickable { viewModel.setStatus(AppStatus.EDIT_HABIT) }
                                    .padding(horizontal = 6.43.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ts_Edit,
                                    color = checkBackgroundBright(
                                        seeColorByHabitAndStatisticsEditX,
                                        UICT_see
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontFamily = JetBrainsFont(),
                                    fontSize = 16.7.sp
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(UIC_dark)
                        .padding(horizontal = 15.56.dp / 1.15f)
                        .padding(bottom = 13.78.dp / 1.15f),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(UIC_light, RoundedCornerShape(18.16.dp))
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 18.16.dp)
                            .height(42.12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        sortedStatuses.forEachIndexed { index, status ->
                            HabitStatisticsStatusIcon(
                                habitStatisticsStatus = status,
                                statusNow = habitStatisticsStatus,
                                icon = getStatusIcon(status),
                                contentDescription = getStatusDescription(status)
                            ) {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }

                                val currentPoints = listPointsOfHabitStatistic[status] ?: 0f
                                listPointsOfHabitStatistic[status] = currentPoints + 1f
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->

            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                UIC_black,
                                seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f)
                            )
                        )
                    )
                    .height(paddingValues.calculateTopPadding() + 66.4.dp / 1.15f),
            )

            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(
                        UIC_dark,
                        RoundedCornerShape(topStart = 66.4.dp / 1.15f, topEnd = 66.4.dp / 1.15f)
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(57.74.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (habitStatisticsStatus) {
                                HabitStatisticsStatus.GOAL -> ts_Goal
                                HabitStatisticsStatus.LEVEL -> ts_Level
                                HabitStatisticsStatus.STREAKS -> ts_Streaks
                                HabitStatisticsStatus.CALENDAR -> ts_Calendar
                                HabitStatisticsStatus.PROGRESS -> ts_Progress
                                HabitStatisticsStatus.BAR_CHART -> ts_Results
                                HabitStatisticsStatus.PROGRESS_GRAPH -> ts_Progress_graph
                                HabitStatisticsStatus.DISTRIBUTION_BY_DAY_OF_THE_WEEK -> ts_Distribution_by_day_of_the_week
                            },
                            fontFamily = JetBrainsFont(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.7.sp,
                            color = UICT_see
                        )
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) { pageIndex ->
                        val statusForPage = sortedStatuses[pageIndex]

                        Box(
                            modifier = Modifier.fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            when (statusForPage) {
                                HabitStatisticsStatus.GOAL -> {
                                    GoalContent(progressPeriodSetting)
                                    LaunchedEffect(Unit) {
                                        while (true) {
                                            progressPeriodSetting = pps_for_habit_statistic
                                            delay(50.milliseconds)
                                        }
                                    }
                                }

                                HabitStatisticsStatus.PROGRESS -> ProgressContent(progressPeriodSetting)
                                HabitStatisticsStatus.LEVEL -> LevelContent(
                                    progressPeriodSetting,
                                )

                                HabitStatisticsStatus.PROGRESS_GRAPH -> ProgressGraphContent(
                                    progressPeriodSetting
                                )

                                HabitStatisticsStatus.BAR_CHART -> BarChartContent()
                                HabitStatisticsStatus.CALENDAR -> CalendarContent()
                                HabitStatisticsStatus.STREAKS -> StreaksContent()
                                HabitStatisticsStatus.DISTRIBUTION_BY_DAY_OF_THE_WEEK -> DistributionByDayOfTheWeekContent()
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class HabitStatisticsStatus {
    GOAL,
    PROGRESS,
    LEVEL,
    PROGRESS_GRAPH,
    BAR_CHART,
    CALENDAR,
    DISTRIBUTION_BY_DAY_OF_THE_WEEK,
    STREAKS
}

var listPointsOfHabitStatistic = mutableMapOf(
    HabitStatisticsStatus.GOAL to 0f,
    HabitStatisticsStatus.PROGRESS to 0f,
    HabitStatisticsStatus.LEVEL to 0f,
    HabitStatisticsStatus.PROGRESS_GRAPH to 0f,
    HabitStatisticsStatus.BAR_CHART to 0f,
    HabitStatisticsStatus.CALENDAR to 0f,
    HabitStatisticsStatus.DISTRIBUTION_BY_DAY_OF_THE_WEEK to 0f,
    HabitStatisticsStatus.STREAKS to 0f
)

@Composable
private fun getStatusIcon(status: HabitStatisticsStatus) = when (status) {
    HabitStatisticsStatus.GOAL -> painterResource(Res.drawable.habit_statistic__goal)
    HabitStatisticsStatus.PROGRESS -> painterResource(Res.drawable.habit_statistic__progress)
    HabitStatisticsStatus.LEVEL -> painterResource(Res.drawable.habit_statistic__level)
    HabitStatisticsStatus.PROGRESS_GRAPH -> painterResource(Res.drawable.habit_statistic__progress_graph)
    HabitStatisticsStatus.BAR_CHART -> painterResource(Res.drawable.habit_statistic__bar_chart)
    HabitStatisticsStatus.CALENDAR -> painterResource(Res.drawable.habit_statistic__calendar)
    HabitStatisticsStatus.STREAKS -> painterResource(Res.drawable.habit_statistic__streaks)
    HabitStatisticsStatus.DISTRIBUTION_BY_DAY_OF_THE_WEEK -> painterResource(Res.drawable.habit_statistic__distribution_by_day_of_the_week)
}

private fun getStatusDescription(status: HabitStatisticsStatus): String = when (status) {
    HabitStatisticsStatus.GOAL -> ts_Goal
    HabitStatisticsStatus.PROGRESS -> ts_Progress
    HabitStatisticsStatus.LEVEL -> ts_Level
    HabitStatisticsStatus.PROGRESS_GRAPH -> ts_Progress_graph
    HabitStatisticsStatus.BAR_CHART -> ts_Bar_chart
    HabitStatisticsStatus.CALENDAR -> ts_Calendar
    HabitStatisticsStatus.STREAKS -> ts_Streaks
    HabitStatisticsStatus.DISTRIBUTION_BY_DAY_OF_THE_WEEK -> ts_Distribution_by_day_of_the_week
}


@Composable
private fun HabitStatisticsStatusIcon(
    habitStatisticsStatus: HabitStatisticsStatus = HabitStatisticsStatus.GOAL,
    statusNow: HabitStatisticsStatus = HabitStatisticsStatus.GOAL,
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    var seeColorByHabitAndStatisticsEditX by remember { mutableStateOf(soul_color) }

    LaunchedEffect(habit_statistics_and_edit_x) {
        calculateProgressiveColor(
            index = habit_statistics_and_edit_x,
            onColorUpdate = { newColor ->
                seeColorByHabitAndStatisticsEditX = newColor
            }
        )
    }


    Box(
        modifier = Modifier.size(35.56.dp / 1.15f)
            .clickable(onClick = onClick)
            .background(
                if (habitStatisticsStatus == statusNow) seeColorByHabitAndStatisticsEditX
                else UIC_light_x2,
                RoundedCornerShape(8.89.dp / 1.15f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(26.67.dp / 1.15f),
            colorFilter = if (habitStatisticsStatus != statusNow) ColorFilter.tint(
                UIC_light_x05,
                BlendMode.Modulate
            )
            else ColorFilter.tint(
                checkBackgroundBright(
                    seeColorByHabitAndStatisticsEditX,
                    reversColor(UIC_light_x05),
                    UIC_light_x05
                ), BlendMode.Modulate
            )
        )
    }
}

@Composable
private fun GoalContent(
    pps: Int,
) {
    @Composable
    fun PPSInfoDialog(smallText: String) {
        var showDialog by remember { mutableStateOf(false) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { showDialog = true }
        )
        {
            Image(
                painter = painterResource(Res.drawable.info),
                contentDescription = ts_Info,
                colorFilter = ColorFilter.tint(UICT_no_see, BlendMode.Modulate),
                modifier = Modifier.size(12.8.dp / 1.15f)
            )
            Text(
                text = smallText,
                fontFamily = JetBrainsFont(),
                fontWeight = FontWeight.ExtraLight,
                fontSize = 12.8.sp / 1.15f,
                color = UICT_no_see,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (showDialog) {
            AlertDialog(
                containerColor = UIC_dark,
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        "$ts_PPS $ts_Info",
                        fontSize = 16.sp / 1.15f,
                        fontWeight = FontWeight.Bold,
                        color = UICT_see
                    )
                },
                text = {
                    Text(
                        ts_PPS_means_Progress_Period_Settings_By_default_progress_is_the_,
                        fontSize = 16.sp / 1.15f,
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
                            fontSize = 16.sp / 1.15f,
                            color = Color(150, 150, 200),
                        )
                    }
                }
            )
        }
    }

    @Composable
    fun GoalParamItem(
        res: Painter,
        contentDescription: String,
        text: String,
        smallText: String,
        isPPS: Boolean = false,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(22.67.dp / 1.15f),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.size(44.44.dp / 1.15f)
                    .background(UIC_extra_light, RoundedCornerShape(22.22.dp / 1.15f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = res,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(35.56.dp / 1.15f),
                    colorFilter = ColorFilter.tint(UIC_light, BlendMode.Modulate)
                )
            }
            Column {
                TextWithDeployableEllipsis(
                    backgroundColor = UIC_dark,
                    newStatusBarInfo = changeStatusBarInfo(
                        backgroundColor = UIC_dark,
                        downPanelSize = 48.67.dp,
                        isProcessed = false
                    ),
                    hazeState = null,
                    text = text,
                    fontFamily = JetBrainsFont(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.7.sp,
                    color = UICT_see
                )
                if (isPPS) PPSInfoDialog(smallText)
                else
                    Text(
                        text = smallText,
                        fontFamily = JetBrainsFont(),
                        fontWeight = FontWeight.ExtraLight,
                        fontSize = 12.8.sp / 1.15f,
                        color = UICT_no_see,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.89.dp / 1.15f),
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 33.2.dp / 1.15f)
            .padding(top = 27.2.dp / 1.15f)
    ) {
        GoalParamItem(
            painterResource(Res.drawable.habit_statistic__goal__type_of_goal),
            ts_Type_of_goal,
            if (habits[habit_statistics_and_edit_x].typeOfGoalHabits == TypeOfGoalHabits.AT_LEAST) ts_At_least
            else ts_No_more,
            ts_type
        )
        GoalParamItem(
            painterResource(Res.drawable.habit_statistic__goal__need_goal),
            ts_Needed_for_the_goal,
            habits[habit_statistics_and_edit_x].needGoal.toBestString() + " " + habits[habit_statistics_and_edit_x].nameOfUnitsOfDimension,
            ts_goal
        )
        GoalParamItem(
            painterResource(Res.drawable.habit_statistic__goal__period),
            ts_Period,
            habits[habit_statistics_and_edit_x].needDays.toString() + " " + ts_days,
            ts_period
        )
        GoalParamItem(
            painterResource(Res.drawable.habit_statistic__goal__PPS),
            ts_PPS,
            "$pps $ts_days",
            " $ts_PPS $ts_for_statistic",
            true
        )
        ValueSetVector(
            pps,
            habits[habit_statistics_and_edit_x].habitDay.size,
            "$ts_PPS (0 $ts_for_full_time):",
            ts_days
        ) {
            pps_for_habit_statistic =
                it.toIntOrNull() ?: habits[habit_statistics_and_edit_x].habitDay.size
            if (pps_for_habit_statistic <= 0) pps_for_habit_statistic =
                habits[habit_statistics_and_edit_x].habitDay.size
        }
    }
}

@Composable
private fun ProgressContent(
    pps: Int
) {
    var seeColorByHabitAndStatisticsEditX by remember { mutableStateOf(soul_color) }

    LaunchedEffect(habit_statistics_and_edit_x) {
        calculateProgressiveColor(
            index = habit_statistics_and_edit_x,
            onColorUpdate = { newColor ->
                seeColorByHabitAndStatisticsEditX = newColor
            }
        )
    }


    @Composable
    fun DonutChart(
        progress: Float,
        modifier: Modifier = Modifier,
        strokeWidth: Dp = 20.dp / 1.15f,
        trackColor: Color = seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f),
        progressColor: Color = seeColorByHabitAndStatisticsEditX,
        label: String = ts_all,
        withLabel: Boolean = false,
        bottomLabel: String = "",
        withBottomLabel: Boolean = !withLabel,
        isPlusProgress: Boolean = !withLabel,
        isBottomLabel: Boolean = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.8.dp / 1.15f)
        ) {
            if (withBottomLabel && !isBottomLabel) {
                Text(
                    text = bottomLabel,
                    fontSize = 12.8.sp / 1.15f,
                    color = UICT_no_see,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = JetBrainsFont()
                )
            }
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

                    drawArc(
                        color = trackColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = stroke,
                        size = Size(size.width, size.height)
                    )

                    drawArc(
                        color = progressColor,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = stroke,
                        size = Size(size.width, size.height)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (withLabel) {
                        Text(
                            text = label,
                            fontSize = 12.8.sp / 1.15f,
                            color = UICT_no_see,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = JetBrainsFont()
                        )
                    }
                    if (isPlusProgress) {
                        Text(
                            text = (if (progress >= 0) "+" else "") + "${(progress * 100).toInt()}%",
                            fontSize = 14.4.sp / 1.15f,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = JetBrainsFont(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if (progress >= 0) UIC_green else UIC_red
                        )
                    } else {
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 25.6.sp / 1.15f,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = JetBrainsFont(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = UICT_see
                        )
                    }
                    if (withLabel) {
                        Text(
                            text = label,
                            fontSize = 12.8.sp / 1.15f,
                            color = Color.Transparent,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = JetBrainsFont()
                        )
                    }
                }
            }
            if (withBottomLabel && isBottomLabel) {
                Text(
                    text = bottomLabel,
                    fontSize = 12.8.sp / 1.15f,
                    color = UICT_no_see,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = JetBrainsFont()
                )
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().padding(horizontal = 37.6.dp / 1.15f)
            .padding(top = 8.8.dp / 1.15f)
    ) {
        DonutChart(
            progress(habit_statistics_and_edit_x, pps),
            Modifier.size(180.dp / 1.15f),
            20.dp / 1.15f,
            withLabel = true
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(66.dp / 1.15f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var progress = plusProgress(habit_statistics_and_edit_x, 1, pps)
                DonutChart(
                    progress = progress,
                    strokeWidth = 8.7.dp,
                    isBottomLabel = true,
                    trackColor = if (progress >= 0f) seeColorByHabitAndStatisticsEditX.multiply(
                        0.5f,
                        0.5f,
                        0.5f
                    ) else reversNoBiggerColor(
                        seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f)
                    ),
                    progressColor = if (progress >= 0f) seeColorByHabitAndStatisticsEditX else reversNoBiggerColor(
                        seeColorByHabitAndStatisticsEditX
                    ),
                    modifier = Modifier.size(90.dp / 1.15f),
                    bottomLabel = ts_day
                )
                progress = plusProgress(habit_statistics_and_edit_x, 7, pps)
                DonutChart(
                    progress = progress,
                    strokeWidth = 8.7.dp,
                    isBottomLabel = true,
                    trackColor = if (progress >= 0f) seeColorByHabitAndStatisticsEditX.multiply(
                        0.5f,
                        0.5f,
                        0.5f
                    ) else reversNoBiggerColor(
                        seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f)
                    ),
                    progressColor = if (progress >= 0f) seeColorByHabitAndStatisticsEditX else reversNoBiggerColor(
                        seeColorByHabitAndStatisticsEditX
                    ),
                    modifier = Modifier.size(90.dp / 1.15f),
                    bottomLabel = ts_week
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var progress = plusProgress(habit_statistics_and_edit_x, 30, pps)
                DonutChart(
                    progress = progress,
                    strokeWidth = 8.7.dp,
                    isBottomLabel = false,
                    trackColor = if (progress >= 0f) seeColorByHabitAndStatisticsEditX.multiply(
                        0.5f,
                        0.5f,
                        0.5f
                    ) else reversNoBiggerColor(
                        seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f)
                    ),
                    progressColor = if (progress >= 0f) seeColorByHabitAndStatisticsEditX else reversNoBiggerColor(
                        seeColorByHabitAndStatisticsEditX
                    ),
                    modifier = Modifier.size(90.dp / 1.15f),
                    bottomLabel = ts_month
                )
                progress = plusProgress(habit_statistics_and_edit_x, 365, pps)
                DonutChart(
                    progress = progress,
                    strokeWidth = 8.7.dp,
                    isBottomLabel = false,
                    trackColor = if (progress >= 0f) seeColorByHabitAndStatisticsEditX.multiply(
                        0.5f,
                        0.5f,
                        0.5f
                    ) else reversNoBiggerColor(
                        seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f)
                    ),
                    progressColor = if (progress >= 0f) seeColorByHabitAndStatisticsEditX else reversNoBiggerColor(
                        seeColorByHabitAndStatisticsEditX
                    ),
                    modifier = Modifier.size(90.dp / 1.15f),
                    bottomLabel = ts_year
                )
            }
        }
    }
}

private enum class TypeOfParamElement {
    GOAL,
    PERIOD
}

@Composable
private fun LevelContent(
    pps: Int,
) {
    var seeColorByHabitAndStatisticsEditX by remember { mutableStateOf(soul_color) }

    LaunchedEffect(habit_statistics_and_edit_x) {
        calculateProgressiveColor(
            index = habit_statistics_and_edit_x,
            onColorUpdate = { newColor ->
                seeColorByHabitAndStatisticsEditX = newColor
            }
        )
    }


    @Composable
    fun CircleImage(
        isNotBad: Boolean,
        kX: Float,
        paddingY: Dp,
        size: Dp
    ) {
        Box(
            modifier = Modifier.padding(top = paddingY)
                .fillMaxWidth(kX),
            contentAlignment = Alignment.TopEnd
        ) {
            Box(
                modifier = Modifier.size(size)
                    .background(
                        if (isNotBad) seeColorByHabitAndStatisticsEditX
                        else reversNoBiggerColor(seeColorByHabitAndStatisticsEditX),
                        RoundedCornerShape(size / 2)
                    )
                    .clip(RoundedCornerShape(size / 2)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(if (isNotBad) Res.drawable.habit_statistic__level__up else Res.drawable.habit_statistic__level__down),
                    contentDescription = ts_Level,
                    colorFilter = ColorFilter.tint(
                        if (isNotBad) seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f)
                        else reversNoBiggerColor(
                            seeColorByHabitAndStatisticsEditX.multiply(
                                0.5f,
                                0.5f,
                                0.5f
                            )
                        ),
                        BlendMode.Modulate
                    ),
                    modifier = Modifier.size(size / 2)
                )
            }
        }
    }

    @Composable
    fun DonutChart(isGood: Boolean) {
        val sizeDp = 150.dp
        val strokeWidthDp = 16.67.dp

        Box(
            modifier = Modifier.size(sizeDp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidthPx = strokeWidthDp.toPx()
                val stroke = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)

                val arcSize = Size(
                    width = size.width - strokeWidthPx,
                    height = size.height - strokeWidthPx
                )

                val topLeftOffset = Offset(
                    x = strokeWidthPx / 2,
                    y = strokeWidthPx / 2
                )

                drawArc(
                    color = if (isGood) seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f)
                    else reversNoBiggerColor(
                        seeColorByHabitAndStatisticsEditX.multiply(
                            0.5f,
                            0.5f,
                            0.5f
                        )
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeftOffset,
                    size = arcSize,
                    style = stroke
                )

                drawArc(
                    color = if (isGood) seeColorByHabitAndStatisticsEditX
                    else reversNoBiggerColor(seeColorByHabitAndStatisticsEditX),
                    startAngle = -90f,
                    sweepAngle = 360f * habits[habit_statistics_and_edit_x].getToLevelUp(pps),
                    useCenter = false,
                    topLeft = topLeftOffset,
                    size = arcSize,
                    style = stroke
                )
            }
            Text(
                text = habits[habit_statistics_and_edit_x].level.toString(),
                fontSize = 21.34.sp,
                color = UICT_see,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = JetBrainsFont(),
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    fun paramElement(
        type: TypeOfParamElement
    ) {
        val currColor = if (habits[habit_statistics_and_edit_x].getToLevelUp(pps) < 0f) UIC_red else UIC_green
        val isChange = if (abs(habits[habit_statistics_and_edit_x].getToLevelUp(pps)) > 0f) true else false
        Column(
            modifier = Modifier.fillMaxWidth()
                .height(51.667.dp)
                .background(currColor.copy(0.08f), RoundedCornerShape(25.167.dp))
                .padding(horizontal = 25.167.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = if (type == TypeOfParamElement.GOAL) ts_Goal else ts_Period,
                fontSize = 10.67.sp,
                color = UICT_no_see,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = JetBrainsFont(),
                fontWeight = FontWeight.Thin,
            )
            if (type == TypeOfParamElement.GOAL) {
                if (habits[habit_statistics_and_edit_x].changeNeedGoalWithLevel && isChange) {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = habits[habit_statistics_and_edit_x].needGoal.toBestString(),
                            fontSize = 13.333.sp,
                            color = UICT_see,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = JetBrainsFont(),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Right
                        )
                        Text(
                            text = " → ",
                            fontSize = 13.333.sp,
                            color = currColor,
                            fontFamily = JetBrainsFont(),
                            fontWeight = FontWeight.Normal,
                        )
                        Text(
                            text = habits[habit_statistics_and_edit_x].getNeedGoalWhenNewLevel(pps).toBestString(),
                            fontSize = 13.333.sp,
                            color = currColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = JetBrainsFont(),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                } else {
                    Text(
                        text = habits[habit_statistics_and_edit_x].needGoal.toBestString(),
                        fontSize = 13.333.sp,
                        color = UICT_see,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = JetBrainsFont(),
                        fontWeight = FontWeight.Normal,
                    )
                }
            } else {
                if (habits[habit_statistics_and_edit_x].changeNeedDaysWithLevel && isChange) {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = habits[habit_statistics_and_edit_x].needDays.toString(),
                            fontSize = 13.333.sp,
                            color = UICT_see,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = JetBrainsFont(),
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.weight(0.5f),
                            textAlign = TextAlign.Right
                        )
                        Text(
                            text = " → ",
                            fontSize = 13.333.sp,
                            color = currColor,
                            fontFamily = JetBrainsFont(),
                            fontWeight = FontWeight.Normal,
                        )
                        Row(
                            modifier = Modifier.weight(0.5f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = habits[habit_statistics_and_edit_x].getNeedDaysWhenNewLevel(pps).toString(),
                                fontSize = 13.333.sp,
                                color = currColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = JetBrainsFont(),
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = " (" + habits[habit_statistics_and_edit_x].getPhantomNeedDaysWhenNewLevel(pps)
                                    .toString() + ")",
                                fontSize = 13.333.sp,
                                color = UICT_no_see,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontFamily = JetBrainsFont(),
                                fontWeight = FontWeight.Thin,
                            )
                        }
                    }
                } else {
                    Text(
                        text = habits[habit_statistics_and_edit_x].needGoal.toBestString(),
                        fontSize = 13.333.sp,
                        color = UICT_see,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = JetBrainsFont(),
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
            Text(
                text = if (type == TypeOfParamElement.GOAL) habits[habit_statistics_and_edit_x].nameOfUnitsOfDimension else ts_days,
                fontSize = 10.67.sp,
                color = UICT_no_see,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = JetBrainsFont(),
                fontWeight = FontWeight.Thin,
            )
        }
    }

    Column(
        modifier = Modifier.padding(top = 7.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val isNotBad = if (progress(habit_statistics_and_edit_x, pps) <= 0.2f) false else true
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            CircleImage(isNotBad, 0.1666f, 20.dp / 1.15f, 49.2.dp / 1.15f)
            CircleImage(isNotBad, 0.237f, 89.6.dp / 1.15f, 16.4.dp / 1.15f)
            CircleImage(isNotBad, 0.1296f, 147.2.dp / 1.15f, 33.2.dp / 1.15f)
            CircleImage(isNotBad, 0.8055f, 29.2.dp / 1.15f, 25.2.dp / 1.15f)
            CircleImage(isNotBad, 0.9462f, 44.dp / 1.15f, 14.8.dp / 1.15f)
            CircleImage(isNotBad, 0.8981f, 114.4.dp / 1.15f, 41.6.dp / 1.15f)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                DonutChart(isNotBad)
            }
        }
        Spacer(modifier = Modifier.height(32.333.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 25.67.dp),
            verticalArrangement = Arrangement.spacedBy(6.33.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            paramElement(TypeOfParamElement.GOAL)
            paramElement(TypeOfParamElement.PERIOD)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = ts_Manual_change,
                fontSize = 12.sp,
                color = UICT_no_see,
                fontFamily = JetBrainsFont(),
                fontWeight = FontWeight.Normal
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.67.dp)
            ) {
                var isHoveredUp by remember { mutableStateOf(false) }
                var isHoveredDown by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier.size(69.dp, 40.dp)
                        .background(UIC_green.copy(if (isHoveredUp) 0.9f else 0.08f), RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while(true) {
                                    val event = awaitPointerEvent()
                                    when (event.type) {
                                        PointerEventType.Enter -> isHoveredUp = true
                                        PointerEventType.Exit -> isHoveredUp = false
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.habit_statistic__level__up),
                        contentDescription = ts_Level_up,
                        colorFilter = ColorFilter.tint( if (isHoveredUp) UIC_dark else UIC_green.copy(0.9f), BlendMode.Modulate),
                        modifier = Modifier.size(25.dp)
                    )
                }
                Box(
                    modifier = Modifier.size(69.dp, 40.dp)
                        .background(UIC_red.copy(if (isHoveredDown) 0.9f else 0.08f), RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while(true) {
                                    val event = awaitPointerEvent()
                                    when (event.type) {
                                        PointerEventType.Enter -> isHoveredDown = true
                                        PointerEventType.Exit -> isHoveredDown = false
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.habit_statistic__level__down),
                        contentDescription = ts_Level_down,
                        colorFilter = ColorFilter.tint(if (isHoveredDown) UIC_dark else UIC_red.copy(0.9f), BlendMode.Modulate),
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressGraphContent(
    pps: Int
) {
    var seeColorByHabitAndStatisticsEditX by remember { mutableStateOf(soul_color) }

    LaunchedEffect(habit_statistics_and_edit_x) {
        calculateProgressiveColor(
            index = habit_statistics_and_edit_x,
            onColorUpdate = { newColor ->
                seeColorByHabitAndStatisticsEditX = newColor
            }
        )
    }


    var isSmooth by remember { mutableStateOf(true) }

    @Composable
    fun SelectSmooth() {
        @Composable
        fun SelectedElement(smooth: Boolean = isSmooth, isSecond: Boolean = false) {
            Box(
                modifier = Modifier.fillMaxWidth(if (isSecond) 1f else 0.5f)
                    .height(35.6.dp / 1.15f)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f),
                                seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f)
                                    .multiply(
                                        0.2f,
                                        0.2f,
                                        0.2f
                                    )
                            ), Offset(0f, 0f), Offset.Infinite
                        ),
                        RoundedCornerShape(17.8.dp / 1.15f)
                    )
                    .border(
                        0.4.dp / 1.15f,
                        seeColorByHabitAndStatisticsEditX,
                        RoundedCornerShape(17.8.dp / 1.15f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (smooth) ts_Smooth else ts_Linear,
                    fontSize = 16.sp / 1.15f,
                    color = UICT_see,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = JetBrainsFont(),
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        @Composable
        fun UnselectedElement(smooth: Boolean = isSmooth, isSecond: Boolean = false) {
            Box(
                modifier = Modifier.fillMaxWidth(if (isSecond) 1f else 0.5f)
                    .height(35.6.dp / 1.15f)
                    .clip(RoundedCornerShape(17.8.dp / 1.15f))
                    .clickable { isSmooth = !smooth },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isSmooth) ts_Linear else ts_Smooth,
                    fontSize = 16.sp / 1.15f,
                    color = UICT_no_see,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = JetBrainsFont(),
                    fontWeight = FontWeight.Thin
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .height(45.2.dp / 1.15f)
                .background(
                    seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f)
                        .multiply(0.4f, 0.4f, 0.4f, 0.4f),
                    RoundedCornerShape(22.7.dp / 1.15f)
                )
                .padding(horizontal = 6.4.dp / 1.15f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSmooth) {
                SelectedElement()
                UnselectedElement(isSecond = true)
            } else {
                UnselectedElement()
                SelectedElement(isSecond = true)
            }
        }
    }

    @Composable
    fun SmoothLineChart(
        data: List<Float>,
        modifier: Modifier = Modifier.fillMaxWidth().height(180.8.dp / 1.15f),
        lineColor: Color = seeColorByHabitAndStatisticsEditX,
        gradientStart: Color = seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f),
        gradientEnd: Color = UIC_dark,
        gridColor: Color = UIC_light,
        strokeWidth: Dp = 2.dp / 1.15f,
        gridLines: Int = 6
    ) {
        if (data.isEmpty()) return

        Canvas(modifier = modifier) {
            val path = Path()
            val gradientPath = Path()

            val maxY = 1f
            val minY = 0f
            val range = (maxY - minY).takeIf { it != 0f } ?: 1f

            val chartWidth = size.width
            val chartHeight = size.height
            val stepX = chartWidth / (data.size - 1)

            val gridSpacing = chartHeight / (gridLines - 1)
            repeat(gridLines) { i ->
                val y = chartHeight - i * gridSpacing
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(chartWidth, y),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f))
                )
            }

            val points = data.mapIndexed { i, y ->
                Offset(i * stepX, chartHeight - (y - minY) / range * chartHeight)
            }

            path.moveTo(points.first().x, points.first().y)
            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val cur = points[i]
                if (isSmooth) {
                    val midX = (prev.x + cur.x) / 2f
                    path.quadraticTo(prev.x, prev.y, midX, (prev.y + cur.y) / 2f)
                } else {
                    path.lineTo(cur.x, cur.y)
                }
            }
            path.lineTo(points.last().x, points.last().y)

            gradientPath.addPath(path)
            gradientPath.lineTo(points.last().x, chartHeight)
            gradientPath.lineTo(points.first().x, chartHeight)
            gradientPath.close()

            val brush = Brush.verticalGradient(
                colors = listOf(gradientStart, gradientEnd),
                startY = 0f,
                endY = chartHeight
            )

            drawPath(
                path = gradientPath,
                brush = brush
            )

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }

    var period by remember { mutableStateOf(habits[habit_statistics_and_edit_x].habitDay.size) }
    var step by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 29.2.dp / 1.15f)
            .padding(top = 36.8.dp / 1.15f),
        verticalArrangement = Arrangement.spacedBy(36.dp / 1.15f)
    ) {
        SmoothLineChart(listProgress(habit_statistics_and_edit_x, period, step, pps))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp / 1.15f)
        ) {
            SelectSmooth()
            ValueSetVector(
                period,
                habits[habit_statistics_and_edit_x].habitDay.size,
                "$ts_Period (0 $ts_For_all_time)",
                ts_days
            ) {
                period = it.toIntOrNull() ?: habits[habit_statistics_and_edit_x].habitDay.size
                if (period < 2) period = 2
            }
            ValueSetVector(
                step,
                habits[habit_statistics_and_edit_x].habitDay.size,
                ts_Step,
                ts_days
            ) {
                step = it.toIntOrNull() ?: 1
                if (step < 1) step = 1
            }
        }
    }
}

@Composable
private fun BarChartContent() {
    var seeColorByHabitAndStatisticsEditX by remember { mutableStateOf(soul_color) }

    LaunchedEffect(habit_statistics_and_edit_x) {
        calculateProgressiveColor(
            index = habit_statistics_and_edit_x,
            onColorUpdate = { newColor ->
                seeColorByHabitAndStatisticsEditX = newColor
            }
        )
    }


    @Composable
    fun BarChart(
        values: List<BigDecimal>,
        dates: List<String>,
        positiveGradient: List<Color> = listOf(
            seeColorByHabitAndStatisticsEditX,
            seeColorByHabitAndStatisticsEditX.multiply(0.59f, 0.59f, 0.59f)
        ),
        negativeGradient: List<Color> = listOf(
            reversNoBiggerColor(seeColorByHabitAndStatisticsEditX),
            reversNoBiggerColor(
                seeColorByHabitAndStatisticsEditX.multiply(
                    0.59f,
                    0.59f,
                    0.59f
                )
            )
        ),
        modifier: Modifier = Modifier.height(173.91.dp).fillMaxWidth(),
        barWidth: Dp = 15.65.dp,
        barSpacing: Dp = 8.7.dp,
        cornerRadius: Dp = 5.91.dp,
        axisColor: Color = UIC_light,
    ) {
        if (values.isEmpty()) return

        val scrollState = rememberScrollState()
        val density = LocalDensity.current
        val textMeasurer = rememberTextMeasurer()

        val barWidthPx = with(density) { barWidth.toPx() }
        val spacingPx = with(density) { barSpacing.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val labelOffsetPx = with(density) { (0.7.dp).toPx() }

        LaunchedEffect(values) {
            snapshotFlow { scrollState.maxValue }.collect { max ->
                scrollState.scrollTo(max)
            }
        }

        val totalWidthDp = ((values.size + (values.last()
            .toBestString().length - 3) / 3) * (barWidth + barSpacing))

        if (values.isNotEmpty()) {
            Box(
                modifier = modifier.horizontalScroll(scrollState),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .width(totalWidthDp)
                        .fillMaxHeight()
                ) {
                    var minValue = values.minOf { it.doubleValue(false) }
                    if (minValue > 0.0) minValue = 0.0
                    var maxValue = values.maxOf { it.doubleValue(false) }
                    if (maxValue < 0.0) maxValue = 0.0
                    val range = maxValue - minValue
                    val chartHeight = size.height
                    val chartWidth = size.width

                    val zeroY =
                        if (range == 0.0) chartHeight / 2f
                        else (chartHeight * (maxValue / range)).toFloat()

                    drawLine(
                        color = axisColor,
                        start = Offset(0f, zeroY),
                        end = Offset(chartWidth, zeroY),
                        strokeWidth = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f))
                    )

                    values.forEachIndexed { i, value ->
                        val v = value.doubleValue(false).toFloat()
                        val x = i * (barWidthPx + spacingPx)
                        val barHeight =
                            (chartHeight * abs(v / (if (range != 0.0) range.toFloat() else 1f)))

                        val top = if (v >= 0f) zeroY - barHeight else zeroY
                        val bottom = if (v >= 0f) zeroY else zeroY + barHeight

                        val brush = Brush.verticalGradient(
                            colors = if (v >= 0f) positiveGradient else negativeGradient,
                            startY = top,
                            endY = bottom
                        )

                        drawRoundRect(
                            brush = brush,
                            topLeft = Offset(x, top),
                            size = Size(barWidthPx, bottom - top),
                            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                        )
                    }
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .width(totalWidthDp)
                        .fillMaxHeight()
                ) {
                    val boxHeightPx = with(density) { maxHeight.toPx() }
                    var minValue = values.minOf { it.doubleValue(false) }
                    if (minValue > 0.0) minValue = 0.0
                    var maxValue = values.maxOf { it.doubleValue(false) }
                    if (maxValue < 0.0) maxValue = 0.0
                    val range = maxValue - minValue
                    val zeroY = if (range == 0.0) boxHeightPx / 2f
                    else (boxHeightPx * (maxValue / range)).toFloat()

                    values.forEachIndexed { i, value ->
                        val v = value.doubleValue(false).toFloat()
                        if (v == 0f) return@forEachIndexed

                        val xCenter = i * (barWidthPx + spacingPx) + barWidthPx / 2f
                        val barHeight = (boxHeightPx * abs(v / range.toFloat()))

                        val top = if (v >= 0f) zeroY - barHeight else zeroY
                        val bottom = if (v >= 0f) zeroY else zeroY + barHeight

                        val label = value.toBestString()

                        val textLayout = textMeasurer.measure(
                            text = AnnotatedString(label),
                            style = TextStyle(
                                fontSize = 9.74.sp,
                                fontFamily = JetBrainsFont(),
                                fontWeight = FontWeight.Thin,
                                color = Color.Unspecified
                            ),
                            constraints = Constraints()
                        )
                        val textWidth = textLayout.size.width.toFloat()
                        val textHeight = textLayout.size.height.toFloat()

                        val labelX = (xCenter - textWidth / 2f).roundToInt()
                        val labelY = if (v >= 0f) {
                            (top - labelOffsetPx - textHeight).roundToInt()
                        } else {
                            (bottom + labelOffsetPx).roundToInt()
                        }

                        Box(
                            modifier = Modifier.offset { IntOffset(labelX, labelY) }
                        ) {
                            Text(
                                text = label,
                                textAlign = TextAlign.Center,
                                color = UICT_see,
                                fontSize = 9.74.sp,
                                fontFamily = JetBrainsFont(),
                                fontWeight = FontWeight.Thin,
                                maxLines = 1
                            )
                        }

                        val dateLabel = if (i < dates.size) dates[i] else ""
                        if (dateLabel.isNotEmpty()) {
                            val dateLayout = textMeasurer.measure(
                                text = AnnotatedString(dateLabel),
                                style = TextStyle(
                                    fontSize = 9.74.sp,
                                    fontFamily = JetBrainsFont(),
                                    fontWeight = FontWeight.Light,
                                    color = UICT_see
                                ),
                                constraints = Constraints()
                            )
                            val dateWidth = dateLayout.size.width.toFloat()

                            val dateX = (xCenter - dateWidth / 2f).roundToInt()
                            val dateY =
                                (zeroY + labelOffsetPx + with(density) { (2.dp / 1.15f).toPx() }).roundToInt()

                            Box(
                                modifier = Modifier.offset { IntOffset(dateX, dateY) }
                            ) {
                                Text(
                                    text = dateLabel,
                                    textAlign = TextAlign.Center,
                                    color = UICT_see,
                                    fontSize = 10.sp / 1.15f,
                                    fontFamily = JetBrainsFont(),
                                    fontWeight = FontWeight.Light,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    var step by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 29.2.dp / 1.15f)
            .padding(top = 36.8.dp / 1.15f),
        verticalArrangement = Arrangement.spacedBy(36.dp / 1.15f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            BarChart(
                listToday(habit_statistics_and_edit_x, step),
                listTodayDates(habit_statistics_and_edit_x, step)
            )
        }
        ValueSetVector(
            step,
            habits[habit_statistics_and_edit_x].habitDay.size,
            ts_Step,
            ts_days
        ) {
            step = it.toIntOrNull() ?: 1
            if (step < 1) step = 1
        }
    }
}

@Composable
fun CalendarContent() {
    var seeColorByHabitAndStatisticsEditX by remember { mutableStateOf(soul_color) }

    LaunchedEffect(habit_statistics_and_edit_x) {
        calculateProgressiveColor(
            index = habit_statistics_and_edit_x,
            onColorUpdate = { newColor ->
                seeColorByHabitAndStatisticsEditX = newColor
            }
        )
    }


    @Composable
    fun HabitGrid(
        startDay: Int = when (habits[habit_statistics_and_edit_x].startDate.dayOfWeek) {
            DayOfWeek.MONDAY -> 0
            DayOfWeek.TUESDAY -> 1
            DayOfWeek.WEDNESDAY -> 2
            DayOfWeek.THURSDAY -> 3
            DayOfWeek.FRIDAY -> 4
            DayOfWeek.SATURDAY -> 5
            DayOfWeek.SUNDAY -> 6
            else -> 0
        },
        labels: List<Int> = listDaysNumbers(habit_statistics_and_edit_x),
        goods: List<Boolean> = listDaysBoolean(habit_statistics_and_edit_x),
        goodColor: Color = seeColorByHabitAndStatisticsEditX,
        badColor: Color = seeColorByHabitAndStatisticsEditX.multiply(0.5f, 0.5f, 0.5f),
        modifier: Modifier = Modifier
    ) {
        val scrollState = rememberScrollState()

        LaunchedEffect(goods) {
            snapshotFlow { scrollState.maxValue }.collect { max ->
                scrollState.scrollTo(max)
            }
        }

        Box(modifier = modifier.horizontalScroll(scrollState)) {
            var index = 0
            Row(horizontalArrangement = Arrangement.spacedBy(9.2.dp / 1.15f)) {
                while (index < goods.size + startDay) {
                    Column(verticalArrangement = Arrangement.spacedBy(9.2.dp / 1.15f)) {
                        @Composable
                        fun MiniDataBox() {
                            if (index < startDay) {
                                Box(modifier = Modifier.size(15.65.dp))
                            } else {
                                Box(
                                    modifier = Modifier.size(15.65.dp)
                                        .background(
                                            if (goods[index - startDay]) goodColor else badColor,
                                            RoundedCornerShape(3.dp / 1.15f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = labels[index - startDay].toString(),
                                        fontSize = 10.8.sp / 1.15f,
                                        color = checkBackgroundBright(
                                            if (goods[index - startDay]) goodColor else badColor,
                                            UICT_see
                                        ),
                                        maxLines = 1,
                                        fontFamily = JetBrainsFont(),
                                        fontWeight = FontWeight.ExtraLight
                                    )
                                }
                            }
                            index++
                        }

                        MiniDataBox()
                        while ((index) % 7 != 0 && index < goods.size + startDay) {
                            MiniDataBox()
                        }
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 29.2.dp / 1.15f)
            .padding(top = 32.8.dp / 1.15f),
        contentAlignment = Alignment.Center
    ) {
        HabitGrid()
    }
}

@Composable
fun StreaksContent() {
    var seeColorByHabitAndStatisticsEditX by remember { mutableStateOf(soul_color) }

    LaunchedEffect(habit_statistics_and_edit_x) {
        calculateProgressiveColor(
            index = habit_statistics_and_edit_x,
            onColorUpdate = { newColor ->
                seeColorByHabitAndStatisticsEditX = newColor
            }
        )
    }


    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 29.2.dp / 1.15f)
            .padding(top = 32.8.dp / 1.15f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(11.2.dp / 1.15f)
    ) {
        val streaks = habitStreaks(habit_statistics_and_edit_x)
        if (streaks.isNotEmpty()) {
            val maxStreak = streaks.max()

            for (streak in streaks) {
                val k = streak.toFloat() / maxStreak.toFloat()
                Box(
                    modifier = Modifier.fillMaxWidth(k)
                        .height(24.4.dp / 1.15f)
                        .background(
                            seeColorByHabitAndStatisticsEditX.multiply(k, k, k),
                            RoundedCornerShape(8.8.dp / 1.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$streak $ts_days",
                        color = checkBackgroundBright(
                            seeColorByHabitAndStatisticsEditX.multiply(k, k, k),
                            UICT_see
                        ),
                        fontSize = 12.8.sp / 1.15f,
                        fontFamily = JetBrainsFont(),
                        fontWeight = FontWeight.Thin,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun DistributionByDayOfTheWeekContent() {
    var seeColorByHabitAndStatisticsEditX by remember { mutableStateOf(soul_color) }

    LaunchedEffect(habit_statistics_and_edit_x) {
        calculateProgressiveColor(
            index = habit_statistics_and_edit_x,
            onColorUpdate = { newColor ->
                seeColorByHabitAndStatisticsEditX = newColor
            }
        )
    }


    data class DataBox(var x: Float, var y: Float, var width: Float, var height: Float)

    var dataBoxes = mutableListOf<DataBox>()

    fun calculateProportionalAreaChart(
        modifier: DataBox,
        values: List<BigDecimal>,
        spacing: Float = 14.4f / 1.15f,
        horizontal: Boolean = true,
        first: Boolean = false
    ): MutableList<DataBox> {
        if (values.isEmpty()) return dataBoxes

        var sumValues = BigDecimal.ZERO
        for (i in values) {
            sumValues += i
        }

        val height = modifier.height
        val width = modifier.width
        val x = modifier.x
        val y = modifier.y

        if (horizontal) {
            val needHeight =
                if (values.size <= 2) height
                else (height - spacing) * ((values[0] + values[1]).saveDiv(sumValues)).floatValue(
                    false
                )

            val db1Width =
                if (values.size == 1) width
                else (width - spacing) * (values[0].saveDiv(values[0] + values[1])).floatValue(false)
            if (first)
                dataBoxes = mutableListOf(
                    DataBox(
                        x,
                        y,
                        db1Width,
                        needHeight
                    )
                )
            else dataBoxes.add(
                DataBox(
                    x,
                    y,
                    db1Width,
                    needHeight
                )
            )

            if (values.size != 1) {
                dataBoxes.add(
                    DataBox(
                        x + db1Width + spacing,
                        y,
                        width - spacing - db1Width,
                        needHeight
                    )
                )
            }

            if (values.size > 2) {
                calculateProportionalAreaChart(
                    modifier = DataBox(
                        x,
                        y + needHeight + spacing,
                        width,
                        height - needHeight - spacing
                    ),
                    values = values.subList(2, values.size),
                    spacing = spacing,
                    horizontal = false
                )
            }
        } else {
            val needWidth =
                if (values.size <= 2) width
                else (width - spacing) * ((values[0] + values[1]).saveDiv(sumValues)).floatValue(
                    false
                )

            val db1Height =
                if (values.size == 1) height
                else (height - spacing) * (values[0].saveDiv(values[0] + values[1])).floatValue(
                    false
                )
            if (first) dataBoxes = mutableListOf(
                DataBox(
                    x,
                    y,
                    needWidth,
                    db1Height
                )
            )
            else dataBoxes.add(
                DataBox(
                    x,
                    y,
                    needWidth,
                    db1Height
                )
            )

            if (values.size != 1) {
                dataBoxes.add(
                    DataBox(
                        x,
                        y + db1Height + spacing,
                        needWidth,
                        height - db1Height - spacing
                    )
                )
            }

            if (values.size > 2) {
                calculateProportionalAreaChart(
                    modifier = DataBox(
                        x + needWidth + spacing,
                        y,
                        width - needWidth - spacing,
                        height
                    ),
                    values = values.subList(2, values.size),
                    spacing = spacing,
                    horizontal = true
                )
            }
        }

        return dataBoxes
    }

    @Composable
    fun ProportionalAreaChart(
        modifier: Modifier = Modifier.fillMaxWidth().height(400.dp),
        labels: List<String>,
        values: List<BigDecimal>,
        realValues: List<BigDecimal>,
        spacing: Dp = 7.6.dp / 1.15f,
        horizontal: Boolean = true
    ) {
        @Composable
        fun labelText(
            index: Int,
            k: Float
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = labels[index],
                    color = checkBackgroundBright(
                        seeColorByHabitAndStatisticsEditX.multiply(k, k, k),
                        UICT_see
                    ),
                    fontSize = 12.8.sp / 1.15f,
                    fontFamily = JetBrainsFont(),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${(k * 100).toInt()}%",
                    color = checkBackgroundBright(
                        seeColorByHabitAndStatisticsEditX.multiply(k, k, k),
                        UICT_see
                    ),
                    fontSize = 16.sp / 1.15f,
                    fontFamily = JetBrainsFont(),
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${realValues[index].toBestString()} ${habits[habit_statistics_and_edit_x].nameOfUnitsOfDimension}",
                    color = checkBackgroundBright(
                        seeColorByHabitAndStatisticsEditX.multiply(k, k, k),
                        UICT_see
                    ),
                    fontSize = 12.8.sp / 1.15f,
                    fontFamily = JetBrainsFont(),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (values.isEmpty()) return

        var sumValues = BigDecimal.ZERO
        for (i in values) {
            sumValues += i
        }

        BoxWithConstraints(modifier = modifier) {
            val boxes = remember(maxWidth, maxHeight, values, horizontal) {
                calculateProportionalAreaChart(
                    DataBox(0f, 0f, maxWidth.value, maxHeight.value),
                    values = values,
                    spacing = spacing.value,
                    horizontal = horizontal,
                    first = true
                )
            }

            for (i in boxes.indices) {
                val k = values[i].saveDiv(sumValues).floatValue(false)

                Box(
                    modifier = Modifier.offset(boxes[i].x.dp, boxes[i].y.dp)
                        .size(boxes[i].width.dp, boxes[i].height.dp)
                        .background(
                            seeColorByHabitAndStatisticsEditX.multiply(k, k, k),
                            RoundedCornerShape(14.4.dp / 1.15f)
                        )
                ) {
                    labelText(i, k)
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 29.2.dp / 1.15f)
            .padding(top = 32.8.dp / 1.15f),
        contentAlignment = Alignment.Center
    ) {
        val uncheckLabels = mutableListOf<String>()
        val uncheckRealValues = mutableListOf<BigDecimal>()
        var uncheckValues = mutableListOf<BigDecimal>()
        var minValue: BigDecimal = Double.MAX_VALUE.toBigDecimal()

        for (i in habitDistributionByDayOfTheWeekContent(habit_statistics_and_edit_x)) {
            uncheckLabels.add(i.dayOfWeek)
            uncheckRealValues.add(i.value)
            minValue = minOf(minValue, i.value)
        }

        if (minValue < 0) {
            for (i in uncheckRealValues) {
                uncheckValues.add(i - minValue)
            }
        } else uncheckValues = uncheckRealValues

        val labels = mutableListOf<String>()
        val realValues = mutableListOf<BigDecimal>()
        val values = mutableListOf<BigDecimal>()

        for (i in uncheckValues.indices) {
            if (uncheckValues[i] != BigDecimal.ZERO) {
                labels.add(uncheckLabels[i])
                realValues.add(uncheckRealValues[i])
                values.add(uncheckValues[i])
            }
        }

        ProportionalAreaChart(
            labels = labels,
            values = values,
            realValues = realValues
        )
    }
}