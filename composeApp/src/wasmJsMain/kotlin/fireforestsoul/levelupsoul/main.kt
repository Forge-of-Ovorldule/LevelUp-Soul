package fireforestsoul.levelupsoul

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.events.Event

fun setupSaveOnClose() {
    window.addEventListener("beforeunload") { _: Event ->
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