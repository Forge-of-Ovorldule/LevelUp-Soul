package fireforestsoul.levelupsoul

import androidx.compose.ui.unit.Dp

fun Dp.toUIDp(): Dp {
    return this * LocalSaveManager.data.settings.uiDpScale
}