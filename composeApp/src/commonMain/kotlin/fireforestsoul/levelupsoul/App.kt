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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.sync.Mutex

var backgroundUp: Color = UIC_black
var backgroundDown: Color = UIC_dark
var lock = Mutex()

@Composable
fun App() {
    var screen by remember { mutableStateOf(ScreenManager.LOADING) }

    val verticalScrollForTableContent = rememberScrollState()
    val horizontalScrollForTableContent = rememberScrollState()

    val verticalScrollForHabitsListContent = rememberScrollState()

    val showMainMenu by remember(screen) {
        mutableStateOf(
            when (screen) {
                ScreenManager.HABITS_LIST_UPDATER,
                ScreenManager.SOUL_STATISTICS,
                ScreenManager.HABITS_LIST -> true

                else -> false
            }
        )
    }

    val hazeState = rememberHazeState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .hazeSource(hazeState)
    ) {
        key(screen) {
            when (screen) {
                ScreenManager.TABLE -> {
                    TableContent { screen = it }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(UIC_dark)
                            .windowInsetsPadding(WindowInsets.systemBars)
                    )
                    when (screen) {
                        ScreenManager.LOADING -> LoadingContent { screen = it }
                        ScreenManager.CREATE_HABIT -> CreateHabit { screen = it }
                        ScreenManager.HABIT_STATISTICS -> HabitStatistics { screen = it }
                        ScreenManager.EDIT_HABIT -> EditHabit { screen = it }
                        else -> {
                            if (screen == ScreenManager.HABITS_LIST_UPDATER) {
                                LaunchedEffect(Unit) {
                                    screen = ScreenManager.HABITS_LIST
                                }
                            }
                        }
                    }
                }
            }
            if (showMainMenu) {
                MainMenuContent(
                    { screen = it },
                    verticalScrollForTableContent,
                    horizontalScrollForTableContent,
                    verticalScrollForHabitsListContent,
                    screen
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        StatusBar(hazeState)
    }
}