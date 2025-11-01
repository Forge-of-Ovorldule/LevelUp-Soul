package fireforestsoul.levelupsoul

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = {
            if (loadIsGood)
                saveAllValues()
            exitApplication()
        },
        title = "LevelUp-Soul",
        icon = painterResource("app_icon.png")
    ) {
        val viewModel = remember { AppViewModel() }
        App(viewModel)
    }
}