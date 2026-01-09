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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(viewModel: AppViewModel) {
    val appStatus by viewModel.appStatus.collectAsState()

    val verticalScrollForTableContent = rememberScrollState()
    val horizontalScrollForTableContent = rememberScrollState()

    val verticalScrollForHabitsListContent = rememberScrollState()

    val showMainMenu = when (appStatus) {
        AppStatus.TABLE,
        AppStatus.TABLE_UPDATER,
        AppStatus.HABITS_LIST_UPDATER,
        AppStatus.SOUL_STATISTICS,
        AppStatus.HABITS_LIST -> true

        else -> false
    }

    val hazeState = rememberHazeState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UIC_dark)
            .padding(WindowInsets.systemBars.asPaddingValues())
            .hazeSource(hazeState)
    ) {
        when (appStatus) {
            AppStatus.LOADING -> LoadingContent(viewModel)
            AppStatus.CREATE_HABIT -> CreateHabit(viewModel)
            AppStatus.HABIT_STATISTICS -> HabitStatistics(viewModel)
            AppStatus.EDIT_HABIT -> EditHabit(viewModel)
            else -> {
                if (appStatus == AppStatus.TABLE_UPDATER) {
                    LaunchedEffect(Unit) {
                        viewModel.setStatus(AppStatus.TABLE)
                    }
                }
                if (appStatus == AppStatus.HABITS_LIST_UPDATER) {
                    LaunchedEffect(Unit) {
                        viewModel.setStatus(AppStatus.HABITS_LIST)
                    }
                }
            }
        }
        if (showMainMenu) {
            MainMenuContent(
                viewModel,
                verticalScrollForTableContent,
                horizontalScrollForTableContent,
                verticalScrollForHabitsListContent
            )
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