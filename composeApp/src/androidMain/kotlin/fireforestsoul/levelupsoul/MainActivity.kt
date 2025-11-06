/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import androidx.activity.compose.BackHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initStorage(applicationContext)

        setContent {
            val viewModel = remember { AppViewModel() }
            App(viewModel)

            BackHandler(enabled = true) {
                when (viewModel.appStatus.value) {
                    AppStatus.LOADING -> {}
                    AppStatus.TABLE -> viewModel.setStatus(AppStatus.HABITS_LIST)
                    AppStatus.HABITS_LIST -> viewModel.setStatus(AppStatus.TABLE)
                    AppStatus.EDIT_HABIT -> viewModel.setStatus(AppStatus.HABIT_STATISTICS)
                    else -> {
                        viewModel.setStatus(backAppStatus)
                    }
                }
            }
        }
    }

    override fun onStop() {
        if (loadIsGood)
            saveAllValues()
        super.onStop()
    }

    override fun onDestroy() {
        if (loadIsGood)
            saveAllValues()
        super.onDestroy()
    }

    override fun onPause() {
        if (loadIsGood)
            saveAllValues()
        super.onPause()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val viewModel = remember { AppViewModel() }
    App(viewModel)
}