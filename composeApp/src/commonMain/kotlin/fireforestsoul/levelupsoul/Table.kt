/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun Table(
    screenChanger: (newScreen: ScreenManager) -> Unit,
) {
    LocalSaveManager.data.backAppStatus = ScreenManager.TABLE

    var startDate by remember { mutableStateOf(dateNow()) }
    var canSee by remember { mutableStateOf(1) }
    var haveToNameOfHabit by remember { mutableStateOf(140.uiDp()) }

    var importIndex by remember { mutableStateOf(0) }

    var highPrioritySorted by remember(importIndex) {
        mutableStateOf(
            LocalSaveManager.data.habits.getPriority(Priority.HIGH_PRIORITY)
        )
    }
    var mediumPrioritySorted by remember(importIndex) {
        mutableStateOf(
            LocalSaveManager.data.habits.getPriority(Priority.MEDIUM_PRIORITY)
        )
    }
    var lowPrioritySorted by remember(importIndex) {
        mutableStateOf(
            LocalSaveManager.data.habits.getPriority(Priority.LOW_PRIORITY)
        )
    }
    var noPrioritySorted by remember(importIndex) {
        mutableStateOf(
            LocalSaveManager.data.habits.getPriority(Priority.NO_PRIORITY)
        )
    }

    val backgroundColor = Color(0xFF0F141A)

    LaunchedEffect(importIndex) {
        val highSource = highPrioritySorted.toList()
        val mediumSource = mediumPrioritySorted.toList()
        val lowSource = lowPrioritySorted.toList()
        val noPrioritySource = noPrioritySorted.toList()

        val sorted = withContext(Dispatchers.Default) {
            listOf(
                async { highSource.sortSystem() },
                async { mediumSource.sortSystem() },
                async { lowSource.sortSystem() },
                async { noPrioritySource.sortSystem() },
            ).awaitAll()
        }

        highPrioritySorted = sorted[0]
        mediumPrioritySorted = sorted[1]
        lowPrioritySorted = sorted[2]
        noPrioritySorted = sorted[3]
    }

    var setValDialogOpen by remember { mutableStateOf(false) }
    val backgroundBlur by animateDpAsState(
        targetValue = if (setValDialogOpen) 10.dp else 0.dp,
        animationSpec = tween(durationMillis = 180)
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(backgroundColor)
            .blur(backgroundBlur)
    ) {
        @Composable
        fun topBar() {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 35.uiDp(), start = 15.uiDp(), end = 21.uiDp()),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                @Composable
                fun DateChooseBlock() {
                    Row(
                        modifier = Modifier.size(138.uiDp(), 34.uiDp())
                            .clip(RoundedCornerShape(11.uiDp()))
                            .background(Color(0XFF111114))
                            .insideBorder(1.uiDp(), color = Color(0xFF36363D), shape = RoundedCornerShape(11.uiDp())),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier.width((22 + 5.66).uiDp())
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(11.uiDp()))
                                .clickable { startDate = startDate.minusDays(canSee) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.left),
                                contentDescription = null,
                                tint = Color(0xFF36363D),
                                modifier = Modifier.size(5.66.uiDp(), 8.66.uiDp())
                            )
                        }
                        Text(
                            text = "${startDate.minusDays(canSee - 1).day} — ${startDate.day} ${startDate.month.toThreeString()}",
                            color = Color(0xFFEEEEEE),
                            fontSize = 10.uiSp(),
                            maxLines = 1,
                            fontFamily = jetBrainsFont(),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                        Box(
                            modifier = Modifier.width((22 + 5.66).uiDp())
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(11.uiDp()))
                                .clickable { startDate = startDate.plusDays(canSee) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.left),
                                contentDescription = null,
                                tint = Color(0xFF36363D),
                                modifier = Modifier.size(5.66.uiDp(), 8.66.uiDp())
                                    .rotate(180f)
                            )
                        }
                    }
                }

                DateChooseBlock()
                Row(
                    modifier = Modifier.size(70.uiDp(), 29.uiDp()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    @Composable
                    fun LanguageChooseBlock() {
                        Box {
                            var expanded by remember { mutableStateOf(false) }

                            Box(
                                modifier = Modifier.size(32.uiDp(), 29.uiDp())
                                    .clip(RoundedCornerShape(8.uiDp()))
                                    .background(Color(0XFF111114))
                                    .insideBorder(
                                        1.uiDp(),
                                        color = Color(0xFF36363D),
                                        shape = RoundedCornerShape(8.uiDp())
                                    )
                                    .clickable { expanded = !expanded },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (LocalSaveManager.data.language) {
                                        Languages.RU -> TranslatedStrings.Table.RU
                                        Languages.EN -> TranslatedStrings.Table.EN
                                    },
                                    color = Color(0xFF848484),
                                    fontSize = 10.uiSp(),
                                    maxLines = 1,
                                    fontFamily = jetBrainsFont(),
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center
                                )
                            }

                            if (expanded) {
                                Popup(
                                    alignment = Alignment.TopStart,
                                    onDismissRequest = { expanded = false },
                                    properties = PopupProperties(
                                        focusable = true,
                                    ),
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .width(32.uiDp())
                                            .clip(RoundedCornerShape(8.uiDp()))
                                            .background(Color(0xFF111114))
                                            .insideBorder(
                                                width = 1.uiDp(),
                                                color = Color(0xFF36363D),
                                                shape = RoundedCornerShape(8.uiDp()),
                                            ),
                                    ) {
                                        Languages.entries.forEach { lang ->
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(29.uiDp())
                                                    .clickable {
                                                        LocalSaveManager.data.language = lang
                                                        LocalSaveManager.save()
                                                        changeLanguage()
                                                        TranslatedStrings.changeLanguage()
                                                        expanded = false
                                                    },
                                                contentAlignment = Alignment.Center,
                                            ) {
                                                Text(
                                                    text = when (lang) {
                                                        Languages.RU -> TranslatedStrings.Table.RU
                                                        Languages.EN -> TranslatedStrings.Table.EN
                                                    },
                                                    color = Color(0xFF848484),
                                                    fontSize = 10.uiSp(),
                                                    maxLines = 1,
                                                    fontFamily = jetBrainsFont(),
                                                    fontWeight = FontWeight.Normal,
                                                    textAlign = TextAlign.Center,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Composable
                    fun SettingsBlock() {
                        Box {
                            var expanded by remember { mutableStateOf(false) }

                            Box(
                                modifier = Modifier.size(29.uiDp().toRoundDp())
                                    .clip(CircleShape)
                                    .background(Color(0XFF111114))
                                    .insideBorder(
                                        1.uiDp(),
                                        color = Color(0xFF36363D),
                                        shape = CircleShape
                                    )
                                    .clickable { expanded = !expanded },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.settings),
                                    contentDescription = null,
                                    tint = Color(0xFF848484),
                                    modifier = Modifier.size(25.uiDp().toRoundDp())
                                )
                            }

                            if (expanded) {
                                SettingsDialog(onImport = { importIndex++ }) { expanded = false }
                            }
                        }
                    }


                    LanguageChooseBlock()
                    SettingsBlock()
                }
            }
        }

        @Composable
        fun BottomBar() {
            @Composable
            fun AddHabitBlock() {
                Image(
                    painter = painterResource(Res.drawable.add_habit),
                    contentDescription = null,
                    modifier = Modifier.offset(x = (-27).uiDp())
                        .size(44.uiDp())
                        .clip(CircleShape)
                        .clickable {
                            screenChanger(ScreenManager.CREATE_HABIT)
                        }
                )
            }

            @Composable
            fun ShadowBlock() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(69.uiDp())
                        .drawWithCache {
                            val radius = 774.uiDp().toPx()

                            val gradient = Brush.radialGradient(
                                colorStops = arrayOf(
                                    0f to Color(0xFF070709),
                                    0.910435f to Color(0xFF070709).copy(alpha = 0.66f),
                                    1f to Color(0xFF070709).copy(alpha = 0f),
                                ),
                                center = Offset(
                                    x = size.width / 2f,
                                    y = radius,
                                ),
                                radius = radius,
                            )

                            onDrawBehind {
                                drawRect(brush = gradient)
                            }
                        }
                )
            }

            @Composable
            fun ScreenSwitcherBlock() {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(79.uiDp())
                        .background(Color(0xFF070709))
                        .outsideBorder(width = 1.uiDp(), color = Color(0xFF36363D), RectangleShape)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .height(4.uiDp())
                    ) {
                        repeat(3) {
                            BoxWithConstraints(Modifier.weight(1f)) {
                                val cnt = ((maxWidth - 4.uiDp()) / 5.uiDp()).toInt()
                                Row(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    repeat(cnt) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Box(
                                            modifier = Modifier.fillMaxHeight()
                                                .width(1.uiDp()).background(Color(0XFF36363D))
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                            Box(
                                modifier = Modifier.fillMaxHeight()
                                    .width(1.uiDp()).background(Color(0XFF36363D))
                            )
                        }
                        BoxWithConstraints(Modifier.weight(1f)) {
                            val cnt = ((maxWidth - 4.uiDp()) / 5.uiDp()).toInt()
                            Row(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                repeat(cnt) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Box(
                                        modifier = Modifier.fillMaxHeight()
                                            .width(1.uiDp()).background(Color(0XFF36363D))
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier.size(1.uiDp(), 9.uiDp())
                                .background(Color(0XFF36363D)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.requiredSize(29.uiDp(), 42.uiDp())
                                    .offset(y = 16.5.uiDp())
                                    .clickable {
                                        screenChanger(ScreenManager.HABITS_LIST)
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.uiDp()),
                            ) {
                                Box(
                                    modifier = Modifier.size(1.uiDp(), 9.uiDp())
                                )
                                Icon(
                                    painter = painterResource(Res.drawable.list),
                                    contentDescription = null,
                                    tint = Color(0xFF36363D),
                                    modifier = Modifier.size(14.uiDp())
                                )
                                Text(
                                    text = TranslatedStrings.Table.LIST,
                                    color = Color(0xFF36363D),
                                    fontSize = 8.uiSp(),
                                    maxLines = 1,
                                    fontFamily = jetBrainsFont(),
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier.size(1.uiDp(), 14.uiDp())
                                .background(Color(0XFFEEEEEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.requiredSize(39.uiDp(), 50.uiDp())
                                    .offset(y = 18.uiDp()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.uiDp()),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    val width = 7.uiDp()
                                    val height = 17.uiDp()

                                    val centerY = 5.1.uiDp()
                                    val radiusX = 3.16999.uiDp()
                                    val radiusY = 11.9.uiDp()

                                    Canvas(
                                        modifier = Modifier.size(
                                            width = width,
                                            height = height,
                                        ),
                                    ) {
                                        val center = Offset(
                                            x = size.width / 2f,
                                            y = centerY.toPx(),
                                        )

                                        val radiusXPx = radiusX.toPx()
                                        val radiusYPx = radiusY.toPx()

                                        val brush = Brush.radialGradient(
                                            colorStops = arrayOf(
                                                0f to Color.White.copy(alpha = 0.75f),
                                                1f to Color.White.copy(alpha = 0f),
                                            ),
                                            center = center,
                                            radius = radiusYPx,
                                        )

                                        clipRect {
                                            scale(
                                                scaleX = radiusXPx / radiusYPx,
                                                scaleY = 1f,
                                                pivot = center,
                                            ) {
                                                drawCircle(
                                                    brush = brush,
                                                    center = center,
                                                    radius = radiusYPx,
                                                    alpha = 0.47f,
                                                )
                                            }
                                        }
                                    }
                                    Box(
                                        modifier = Modifier.size(1.uiDp(), 14.uiDp())
                                    )
                                }
                                Icon(
                                    painter = painterResource(Res.drawable.table),
                                    contentDescription = null,
                                    tint = Color(0xFFEEEEEE),
                                    modifier = Modifier.size(19.uiDp(), 17.uiDp())
                                )
                                Text(
                                    text = TranslatedStrings.Table.TABLE,
                                    color = Color(0xFF848484),
                                    fontSize = 8.uiSp(),
                                    maxLines = 1,
                                    fontFamily = jetBrainsFont(),
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    letterSpacing = 0.8.uiSp(),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier.size(1.uiDp(), 9.uiDp())
                                .background(Color(0XFF36363D)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.requiredSize(48.uiDp(), 42.uiDp())
                                    .offset(y = 16.5.uiDp())
                                    .clickable {
                                        screenChanger(ScreenManager.SOUL_STATISTICS)
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.uiDp()),
                            ) {
                                Box(
                                    modifier = Modifier.size(1.uiDp(), 9.uiDp())
                                )
                                Icon(
                                    painter = painterResource(Res.drawable.statistic),
                                    contentDescription = null,
                                    tint = Color(0xFF36363D),
                                    modifier = Modifier.size(13.uiDp(), 14.uiDp())
                                )
                                Text(
                                    text = TranslatedStrings.Table.STATISTICS,
                                    color = Color(0xFF36363D),
                                    fontSize = 8.uiSp(),
                                    maxLines = 1,
                                    fontFamily = jetBrainsFont(),
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                Box(contentAlignment = Alignment.TopEnd) {
                    AddHabitBlock()
                    ShadowBlock()
                }

                ScreenSwitcherBlock()
            }
        }

        @Composable
        fun MainBlock() {
            @Composable
            fun DateBlock() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.uiDp()),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.size(1.uiDp(), 29.uiDp())
                            .background(Color(0xFF25272D))
                    )
                    repeat(canSee) { id ->
                        Column(
                            modifier = Modifier.size(37.uiDp(), 27.uiDp()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Spacer(Modifier.height(1.uiDp()))
                            Text(
                                text = startDate.minusDays(id).dayOfWeek.toThreeString(),
                                color = Color(0xFF45454C),
                                fontSize = 9.uiSp(),
                                maxLines = 1,
                                fontFamily = jetBrainsFont(),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = startDate.minusDays(id).day.toString(),
                                color = Color(0xFF848484),
                                fontSize = 11.uiSp(),
                                maxLines = 1,
                                fontFamily = jetBrainsFont(),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.offset(y = (-2).uiDp())
                            )
                        }
                    }
                }
            }

            fun LazyListScope.onePriorityHabitBlock(
                minimumVisibility: Float,
                maximumVisibility: Float,
                priority: Priority
            ) {
                if (when (priority) {
                        Priority.HIGH_PRIORITY -> highPrioritySorted
                        Priority.MEDIUM_PRIORITY -> mediumPrioritySorted
                        Priority.LOW_PRIORITY -> lowPrioritySorted
                        Priority.NO_PRIORITY -> noPrioritySorted
                    }.isNotEmpty()
                ) {
                    item(key = priority.name) {
                        Box(
                            modifier = Modifier.height(39.uiDp())
                                .fillMaxWidth()
                                .padding(start = 17.uiDp(), top = 11.uiDp())
                        ) {
                            Text(
                                text = priority.toTranslatedString(),
                                color = Color(0xFF848484),
                                fontSize = 13.uiSp(),
                                maxLines = 1,
                                fontFamily = jetBrainsFont(),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Left,
                            )
                        }
                    }
                    items(
                        items = when (priority) {
                            Priority.HIGH_PRIORITY -> highPrioritySorted
                            Priority.MEDIUM_PRIORITY -> mediumPrioritySorted
                            Priority.LOW_PRIORITY -> lowPrioritySorted
                            Priority.NO_PRIORITY -> noPrioritySorted
                        },
                        key = { curPriorityPair -> curPriorityPair.second }
                    ) { curPriorityPair ->

                        var habit by remember(importIndex) { mutableStateOf(curPriorityPair.first) }
                        var id by remember(importIndex) { mutableStateOf(curPriorityPair.second) }
                        var progressiveColor by remember(importIndex) { mutableStateOf(habit.progressiveColorCache) }

                        val updateScope = rememberCoroutineScope()

                        LaunchedEffect(id) {
                            withContext(Dispatchers.Default) {
                                habit.calculateProgressiveColor {
                                    updateScope.launch {
                                        progressiveColor = it
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.height(39.uiDp())
                                .fillMaxWidth()
                                .animateItem(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier.size(5.uiDp(), 39.uiDp()),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize()
                                        .clip(RoundedCornerShape(2.5.uiDp()))
                                        .background(Color(0XFF25272D))
                                )
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                        .fillMaxHeight(progress(habit))
                                        .clip(RoundedCornerShape(2.5.uiDp()))
                                        .background(progressiveColor)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.uiDp()))
                            Column(
                                modifier = Modifier.fillMaxHeight()
                                    .width(haveToNameOfHabit)
                                    .padding(top = 5.uiDp())
                                    .clickable {
                                        habit_statistics_and_edit_x = id
                                        screenChanger(ScreenManager.HABIT_STATISTICS)
                                    },
                                verticalArrangement = Arrangement.spacedBy(4.uiDp())
                            ) {
                                Row {
                                    Text(
                                        text = habit.nameOfHabit,
                                        color = Color(0xFFEEEEEE),
                                        fontSize = 13.uiSp(),
                                        maxLines = 1,
                                        fontFamily = jetBrainsFont(),
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Left,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f),
                                    )
                                    if (habit.changeLevel)
                                        Text(
                                            text = "${TranslatedStrings.Table.LVL}${habit.level}",
                                            color = closestColorWithVisibility(
                                                backgroundColor,
                                                progressiveColor,
                                                minimumVisibility
                                            ),
                                            fontSize = 9.uiSp(),
                                            maxLines = 1,
                                            fontFamily = jetBrainsFont(),
                                            fontWeight = FontWeight.Light,
                                            textAlign = TextAlign.Right,
                                            modifier = Modifier.offset(y = 4.uiDp())
                                        )
                                }
                                Row {
                                    Text(
                                        text = "${
                                            when (habit.typeOfGoal) {
                                                TypeOfGoalHabit.NO_MORE -> "<="
                                                TypeOfGoalHabit.AT_LEAST -> ">="
                                            }
                                        } ${habit.numericalGoal.toBestString()} ${habit.nameOfUnitsOfDimension}",
                                        color = Color(0xFF848484),
                                        fontSize = 8.uiSp(),
                                        maxLines = 1,
                                        fontFamily = jetBrainsFont(),
                                        fontWeight = FontWeight.Light,
                                        textAlign = TextAlign.Left,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f, false),
                                    )
                                    Text(
                                        text = " / ${habit.periodForGoalCompletion} ${TranslatedStrings.Table.DAYS}",
                                        color = Color(0xFF848484),
                                        fontSize = 8.uiSp(),
                                        maxLines = 1,
                                        fontFamily = jetBrainsFont(),
                                        fontWeight = FontWeight.Light,
                                        textAlign = TextAlign.Left,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(6.uiDp()))
                            Box(
                                modifier = Modifier.fillMaxHeight()
                                    .width(1.uiDp())
                                    .background(Color(0xFF25272D))
                            )
                            repeat(canSee) { index ->
                                Spacer(modifier = Modifier.width(6.uiDp()))
                                val fixedColor by remember(progressiveColor) {
                                    mutableStateOf(
                                        closestColorWithVisibility(
                                            backgroundColor,
                                            progressiveColor,
                                            minimumVisibility,
                                            maximumVisibility
                                        )
                                    )
                                }
                                var curValDialogOpen by rememberSaveable { mutableStateOf(false) }

                                Box(
                                    modifier = Modifier.size(37.uiDp())
                                        .clip(RoundedCornerShape(5.uiDp()))
                                        .insideBorder(
                                            1.uiDp(),
                                            fixedColor,
                                            RoundedCornerShape(5.uiDp())
                                        )
                                        .background(
                                            fixedColor.copy(
                                                if (habit.correctly(startDate.minusDays(index))) 1f else 0.1f
                                            )
                                        )
                                        .clickable {
                                            setValDialogOpen = true
                                            curValDialogOpen = true
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = habit.habitDay[startDate.minusDays(index)]?.today?.toBestString()
                                            ?: BigDecimal.ZERO.toBestString(),
                                        color = if (habit.correctly(startDate.minusDays(index))) bestVisibleColor(
                                            backgroundColor,
                                            listOf(Color(0xFFEEEEEE), Color(0XFF000000))
                                        ) else fixedColor,
                                        fontSize = 10.uiSp(),
                                        maxLines = 1,
                                        fontFamily = jetBrainsFont(),
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                    )
                                }

                                if (curValDialogOpen) {
                                    Dialog(
                                        onDismissRequest = {
                                            setValDialogOpen = false
                                            curValDialogOpen = false
                                        },
                                        properties = DialogProperties(usePlatformDefaultWidth = false),
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize()
                                                .padding(bottom = 80.uiDp()),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            var curVar by remember {
                                                mutableStateOf(
                                                    habit.habitDay[startDate.minusDays(index)]?.today?.toBestString()
                                                        ?: "0"
                                                )
                                            }
                                            var curVarBigDecimal by remember {
                                                mutableStateOf(
                                                    curVar.toDoubleOrNull()?.toBigDecimal()
                                                        ?: (habit.habitDay[startDate.minusDays(index)]?.today
                                                            ?: BigDecimal.ZERO)
                                                )
                                            }

                                            Column(
                                                modifier = Modifier.width(297.uiDp())
                                                    .clip(RoundedCornerShape(25.uiDp()))
                                                    .background(Color(0xFF070709))
                                                    .insideBorder(
                                                        2.uiDp(),
                                                        Color(0xFF36363D),
                                                        RoundedCornerShape(25.uiDp())
                                                    )
                                                    .padding(25.uiDp())
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                ) {
                                                    Text(
                                                        text = habit.nameOfHabit,
                                                        color = Color(0xFFEEEEEE),
                                                        fontSize = 16.uiSp(),
                                                        maxLines = 1,
                                                        fontFamily = jetBrainsFont(),
                                                        fontWeight = FontWeight.Medium,
                                                        textAlign = TextAlign.Left,
                                                        overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier.weight(1f),
                                                    )
                                                    if (habit.changeLevel)
                                                        Text(
                                                            text = "${TranslatedStrings.Table.LVL}${habit.level}",
                                                            color = progressiveColor,
                                                            fontSize = 12.uiSp(),
                                                            maxLines = 1,
                                                            fontFamily = jetBrainsFont(),
                                                            fontWeight = FontWeight.Normal,
                                                            textAlign = TextAlign.Right,
                                                        )
                                                }
                                                Spacer(modifier = Modifier.height(37.uiDp()))
                                                Column(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.spacedBy(6.uiDp()),
                                                ) {
                                                    Column(
                                                        modifier = Modifier.fillMaxWidth()
                                                            .padding(horizontal = 13.uiDp()),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(2.uiDp()),
                                                    ) {
                                                        BasicTextField(
                                                            value = curVar,
                                                            onValueChange = {
                                                                curVar = it
                                                                curVarBigDecimal =
                                                                    curVar.toDoubleOrNull()?.toBigDecimal()
                                                                        ?: (habit.habitDay[startDate.minusDays(index)]?.today
                                                                            ?: BigDecimal.ZERO)
                                                            },
                                                            modifier = Modifier,
                                                            singleLine = true,
                                                            cursorBrush = SolidColor(Color(0xFF848484)),
                                                            textStyle = TextStyle(
                                                                color = Color(0xFF848484),
                                                                fontSize = 12.uiSp(),
                                                                fontFamily = jetBrainsFont(),
                                                                fontWeight = FontWeight.Medium,
                                                                textAlign = TextAlign.Center,
                                                            ),
                                                            decorationBox = { innerTextField ->
                                                                innerTextField()
                                                            },
                                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                                        )
                                                        Box(
                                                            modifier = Modifier.fillMaxWidth()
                                                                .height(1.uiDp())
                                                                .background(Color(0xFF36363D))
                                                        )
                                                    }
                                                    Box(modifier = Modifier.fillMaxWidth()) {
                                                        Box(
                                                            modifier = Modifier.fillMaxWidth()
                                                                .height(7.uiDp())
                                                                .clip(RoundedCornerShape(3.5.uiDp()))
                                                                .background(Color(0xFF111114))
                                                        )
                                                        Box(
                                                            modifier = Modifier.fillMaxWidth(
                                                                habit.habitDayProgress(
                                                                    startDate.minusDays(index),
                                                                    curVarBigDecimal
                                                                )
                                                            )
                                                                .height(7.uiDp())
                                                                .clip(RoundedCornerShape(3.5.uiDp()))
                                                                .background(progressiveColor)
                                                        )
                                                    }
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth()
                                                            .height(45.uiDp())
                                                            .clip(RoundedCornerShape(13.uiDp()))
                                                            .background(Color(0xFF111114))
                                                            .insideBorder(
                                                                1.uiDp(),
                                                                Color(0xFF36363D),
                                                                RoundedCornerShape(13.uiDp())
                                                            )
                                                            .padding(horizontal = 13.uiDp()),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                    ) {
                                                        Text(
                                                            text = TranslatedStrings.Table.OLD + ":",
                                                            color = Color(0xFF45454C),
                                                            fontSize = 10.uiSp(),
                                                            maxLines = 1,
                                                            fontFamily = jetBrainsFont(),
                                                            fontWeight = FontWeight.Normal,
                                                            textAlign = TextAlign.Left
                                                        )
                                                        Spacer(modifier = Modifier.width(13.uiDp()))
                                                        Text(
                                                            text = habit.habitDay[startDate.minusDays(index)]?.today?.toBestString()
                                                                ?: "0",
                                                            color = Color(0xFF848484),
                                                            fontSize = 12.uiSp(),
                                                            maxLines = 1,
                                                            fontFamily = jetBrainsFont(),
                                                            fontWeight = FontWeight.Medium,
                                                            textAlign = TextAlign.Right
                                                        )
                                                    }
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth()
                                                            .height(45.uiDp())
                                                            .clip(RoundedCornerShape(13.uiDp()))
                                                            .background(Color(0xFF111114))
                                                            .insideBorder(
                                                                1.uiDp(),
                                                                Color(0xFF36363D),
                                                                RoundedCornerShape(13.uiDp())
                                                            )
                                                            .padding(horizontal = 13.uiDp()),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                    ) {
                                                        Text(
                                                            text = TranslatedStrings.Table.GOAL + ":",
                                                            color = Color(0xFF45454C),
                                                            fontSize = 10.uiSp(),
                                                            maxLines = 1,
                                                            fontFamily = jetBrainsFont(),
                                                            fontWeight = FontWeight.Normal,
                                                            textAlign = TextAlign.Left
                                                        )
                                                        Spacer(modifier = Modifier.width(13.uiDp()))
                                                        Row {
                                                            Text(
                                                                text = when (habit.typeOfGoal) {
                                                                    TypeOfGoalHabit.AT_LEAST -> ">="
                                                                    TypeOfGoalHabit.NO_MORE -> "<="
                                                                } + habit.numericalGoal.toBestString() + " " + habit.nameOfUnitsOfDimension,
                                                                color = Color(0xFF848484),
                                                                fontSize = 12.uiSp(),
                                                                maxLines = 1,
                                                                fontFamily = jetBrainsFont(),
                                                                fontWeight = FontWeight.Medium,
                                                                textAlign = TextAlign.Right,
                                                                overflow = TextOverflow.Ellipsis,
                                                                modifier = Modifier.weight(1f, false),
                                                            )
                                                            Text(
                                                                text = " / " + habit.periodForGoalCompletion + " " + TranslatedStrings.Table.DAYS,
                                                                color = Color(0xFF848484),
                                                                fontSize = 12.uiSp(),
                                                                maxLines = 1,
                                                                fontFamily = jetBrainsFont(),
                                                                fontWeight = FontWeight.Medium,
                                                                textAlign = TextAlign.Right
                                                            )
                                                        }
                                                    }
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth()
                                                            .height(45.uiDp())
                                                            .clip(RoundedCornerShape(13.uiDp()))
                                                            .background(Color(0xFF111114))
                                                            .insideBorder(
                                                                1.uiDp(),
                                                                Color(0xFF36363D),
                                                                RoundedCornerShape(13.uiDp())
                                                            )
                                                            .padding(horizontal = 13.uiDp()),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                    ) {
                                                        Text(
                                                            text = TranslatedStrings.Table.DATE + ":",
                                                            color = Color(0xFF45454C),
                                                            fontSize = 10.uiSp(),
                                                            maxLines = 1,
                                                            fontFamily = jetBrainsFont(),
                                                            fontWeight = FontWeight.Normal,
                                                            textAlign = TextAlign.Left
                                                        )
                                                        Spacer(modifier = Modifier.width(13.uiDp()))
                                                        Text(
                                                            text = startDate.minusDays(index).day.toString() + " " + startDate.minusDays(
                                                                index
                                                            ).month.toThreeString(),
                                                            color = Color(0xFF848484),
                                                            fontSize = 12.uiSp(),
                                                            maxLines = 1,
                                                            fontFamily = jetBrainsFont(),
                                                            fontWeight = FontWeight.Medium,
                                                            textAlign = TextAlign.Right
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(12.uiDp()))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth()
                                                        .height(45.uiDp()),
                                                    horizontalArrangement = Arrangement.spacedBy(12.uiDp())
                                                ) {
                                                    Box(
                                                        modifier = Modifier.weight(109f)
                                                            .fillMaxHeight()
                                                            .clip(RoundedCornerShape(13.uiDp()))
                                                            .clickable {
                                                                setValDialogOpen = false
                                                                curValDialogOpen = false
                                                            }
                                                            .background(Color(0xFF111114))
                                                            .insideBorder(
                                                                1.uiDp(),
                                                                Color(0xFF36363D),
                                                                RoundedCornerShape(13.uiDp())
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = TranslatedStrings.Table.CANCEL,
                                                            color = Color(0xFF848484),
                                                            fontSize = 12.uiSp(),
                                                            maxLines = 1,
                                                            fontFamily = jetBrainsFont(),
                                                            fontWeight = FontWeight.Medium,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }

                                                    val scope = rememberCoroutineScope()
                                                    Box(
                                                        modifier = Modifier.weight(154f)
                                                            .fillMaxHeight()
                                                            .clip(RoundedCornerShape(13.uiDp()))
                                                            .clickable {
                                                                LocalSaveManager.data.habits[id].setDayValue(
                                                                    startDate.minusDays(
                                                                        index
                                                                    ),
                                                                    curVar.toDoubleOrNull()?.toBigDecimal()
                                                                        ?: (habit.habitDay[startDate.minusDays(index)]?.today
                                                                            ?: BigDecimal.ZERO)
                                                                )
                                                                LocalSaveManager.save()
                                                                scope.launch {
                                                                    val highSource = highPrioritySorted.toList()
                                                                    val mediumSource = mediumPrioritySorted.toList()
                                                                    val lowSource = lowPrioritySorted.toList()
                                                                    val noPrioritySource = noPrioritySorted.toList()

                                                                    val sorted = withContext(Dispatchers.Default) {
                                                                        listOf(
                                                                            async { highSource.sortSystem() },
                                                                            async { mediumSource.sortSystem() },
                                                                            async { lowSource.sortSystem() },
                                                                            async { noPrioritySource.sortSystem() },
                                                                        ).awaitAll()
                                                                    }

                                                                    highPrioritySorted = sorted[0]
                                                                    mediumPrioritySorted = sorted[1]
                                                                    lowPrioritySorted = sorted[2]
                                                                    noPrioritySorted = sorted[3]
                                                                }
                                                                setValDialogOpen = false
                                                                curValDialogOpen = false
                                                            }
                                                            .background(Color(0xFF111114))
                                                            .insideBorder(
                                                                1.uiDp(),
                                                                Color(0xFF36363D),
                                                                RoundedCornerShape(13.uiDp())
                                                            ),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = TranslatedStrings.Table.APPLY,
                                                            color = Color(0xFF848484),
                                                            fontSize = 12.uiSp(),
                                                            maxLines = 1,
                                                            fontFamily = jetBrainsFont(),
                                                            fontWeight = FontWeight.Medium,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                canSee = ((maxWidth - 172.uiDp()) / (37.uiDp() + 6.uiDp())).toInt()
                haveToNameOfHabit =
                    max(140.uiDp(), maxWidth - canSee * (37.uiDp() + 6.uiDp()) - 6.uiDp() - 25.uiDp() - 7.uiDp())
            }

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(start = 6.uiDp(), top = 102.uiDp()),
                verticalArrangement = Arrangement.spacedBy(8.uiDp()),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(29.uiDp())
                        .padding(start = haveToNameOfHabit + 25.5.uiDp()),
                ) {
                    DateBlock()
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = rememberLazyListState(
                        initialFirstVisibleItemIndex = 0,
                        initialFirstVisibleItemScrollOffset = with(LocalDensity.current) {
                            (39 + 8).uiDp().toPx().toInt()
                        }),
                    verticalArrangement = Arrangement.spacedBy(8.uiDp()),
                ) {
                    val minimumVisibility = 0.05f
                    val maximumVisibility = 0.9f

                    onePriorityHabitBlock(minimumVisibility, maximumVisibility, Priority.HIGH_PRIORITY)
                    onePriorityHabitBlock(minimumVisibility, maximumVisibility, Priority.MEDIUM_PRIORITY)
                    onePriorityHabitBlock(minimumVisibility, maximumVisibility, Priority.LOW_PRIORITY)
                    onePriorityHabitBlock(minimumVisibility, maximumVisibility, Priority.NO_PRIORITY)

                    item(key = "spacer") {
                        Spacer(modifier = Modifier.height(300.uiDp()))
                    }
                }
            }
        }

        BackgroundCircle()
        MainBlock()
        BottomBar()
        topBar()
    }
}

@Composable
private fun BackgroundCircle() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.uiDp()),
    ) {
        val radius = 180.uiDp().toPx()
        val center = Offset(
            x = size.width / 2f,
            y = 0f,
        )

        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to Color(0xFF5C5C5C).copy(alpha = 0.24f),
                    1f to Color.Transparent,
                ),
                center = center,
                radius = radius,
            ),
            radius = radius,
            center = center,
        )
    }
}