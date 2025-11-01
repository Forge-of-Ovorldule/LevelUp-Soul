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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event

fun setupSaveOnClose() {
    window.addEventListener("beforeunload") { _: Event ->
        if (loadIsGood)
            saveAllValues()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    setupSaveOnClose()
    ComposeViewport(document.body!!) {
        val viewModel = remember { AppViewModel() }
        App(viewModel)
    }
}