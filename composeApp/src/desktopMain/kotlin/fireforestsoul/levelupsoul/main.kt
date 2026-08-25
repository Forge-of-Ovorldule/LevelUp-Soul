/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main(): Unit = application {
    Window(
        onCloseRequest = {
            if (loadIsGood)
                LocalSaveManager.save()
            exitApplication()
        },
        title = "LevelUp-Soul",
        icon = painterResource("app_icon.png")
    ) {
        val viewModel = remember { AppViewModel() }
        App(viewModel)
    }
}