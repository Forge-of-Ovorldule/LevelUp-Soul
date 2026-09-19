@file:Suppress("unused")

package fireforestsoul.levelupsoul

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun Dp.toUIDp(): Dp {
    return this * LocalSaveManager.data.settings.uiDpScale
}

fun Dp.toRoundDp(): Dp {
    return this.value.toInt().dp
}

fun TextUnit.toUISp(): TextUnit {
    return this * LocalSaveManager.data.settings.uiDpScale
}

fun Int.uiDp(): Dp {
    return this.dp.toUIDp()
}

fun Float.uiDp(): Dp {
    return this.dp.toUIDp()
}

fun Double.uiDp(): Dp {
    return this.dp.toUIDp()
}

fun Int.uiSp(): TextUnit {
    return this.sp.toUISp()
}

fun Float.uiSp(): TextUnit {
    return this.sp.toUISp()
}

fun Double.uiSp(): TextUnit {
    return this.sp.toUISp()
}