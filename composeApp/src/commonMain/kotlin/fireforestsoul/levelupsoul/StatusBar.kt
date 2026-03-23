package fireforestsoul.levelupsoul

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.String

var statusBarInfo by mutableStateOf(StatusBarInfo())
var updateTimerForStatusBar by mutableStateOf(false)

fun changeStatusBarInfo(
    text: String = "",
    textColor: Color? = null,
    backgroundColor: Color? = null,
    downPanelSize: Dp? = null,
    isProcessed: Boolean? = null
): StatusBarInfo {
    return StatusBarInfo(
        text = text,
        textColor = textColor ?: statusBarInfo.textColor,
        backgroundColor = backgroundColor ?: statusBarInfo.backgroundColor,
        downPanelSize = downPanelSize ?: statusBarInfo.downPanelSize,
        isProcessed = isProcessed ?: statusBarInfo.isProcessed,
    )
}

data class StatusBarInfo(
    var text: String = "",
    var textColor: Color = UICT_see,
    var backgroundColor: Color = UIC_dark,
    var downPanelSize: Dp = 48.7.dp,
    var isProcessed: Boolean = false
)

var listProgressedStatusBar = mutableListOf<String>()

@Composable
fun StatusBar(hazeState: HazeState) {

    var displayTextColor by mutableStateOf(statusBarInfo.textColor)
    var displayBackgroundColor by mutableStateOf(statusBarInfo.backgroundColor)
    var displayDownPanelSize by mutableStateOf(statusBarInfo.downPanelSize)

    LaunchedEffect(
        Unit,
        updateTimerForStatusBar,
        statusBarInfo,
        statusBarInfo.text,
        statusBarInfo.backgroundColor,
        statusBarInfo.downPanelSize,
        statusBarInfo.isProcessed,
        statusBarInfo.textColor
    ) {
        displayTextColor = statusBarInfo.textColor
        displayBackgroundColor = statusBarInfo.backgroundColor
        displayDownPanelSize = statusBarInfo.downPanelSize
        updateTimerForStatusBar = false
        makeTextForStatusBar()
    }

    if (statusBarTextNow != "") {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = displayDownPanelSize),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 8.35.dp)
                    .border(
                        0.34.dp,
                        displayTextColor.copy(0.5f),
                        RoundedCornerShape(4.87.dp)
                    )
                    .clip(RoundedCornerShape(4.87.dp))
                    .hazeEffect(
                        hazeState,
                        HazeStyle(
                            tint = HazeTint(UIC_white.copy(0f)),
                            blurRadius = 8.7.dp,
                            noiseFactor = 0f,
                            backgroundColor = checkBackgroundBright(
                                displayBackgroundColor,
                                displayBackgroundColor.multiply(2f, 2f, 2f, 0.25f),
                                displayBackgroundColor.multiply(0.5f, 0.5f, 0.5f, 0.25f)
                            ),
                        )
                    )
                    .padding(8.7.dp, 3.78.dp)
            ) {
                Text(
                    text = statusBarTextNow,
                    color = displayTextColor,
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 12.52.sp,
                    fontFamily = JetBrainsFont(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private var statusBarTextNow by mutableStateOf("")
private var workText: String = statusBarInfo.text
private var startSize = listProgressedStatusBar.size
private var isProcessed = statusBarInfo.isProcessed
private val mutex = Mutex()

private suspend fun makeTextForStatusBar() = withContext(Dispatchers.Default) {
    if (!statusBarInfo.isProcessed) {
        workText = statusBarInfo.text
        isProcessed = false
    }
    startSize = listProgressedStatusBar.size

    while (true) {
        val currentText = mutex.withLock { workText }

        if (!currentText.isNullOrEmpty()) { //!!! проверка на Null необходима
            val currentListSize = mutex.withLock { listProgressedStatusBar.size }

            if (currentListSize != startSize) {
                workText = ""
                continue
            }

            if (statusBarTextNow.length < currentText.length) {
                statusBarTextNow = currentText.take(statusBarTextNow.length + 1)
                delay(75)
                continue
            }

            if (statusBarTextNow.length == currentText.length) {
                statusBarTextNow = currentText
                if (isProcessed) {
                    while (currentListSize == mutex.withLock { listProgressedStatusBar.size }) {
                        statusBarTextNow = currentText + ".".repeat(addedEllipsis)
                        addedEllipsis++
                        if (addedEllipsis == 4) addedEllipsis = 0
                        delay(400)
                    }
                    addedEllipsis = 0
                } else {
                    delay(5000)
                }
                workText = ""
                continue
            }

            mutex.withLock {
                if (statusBarTextNow.isNotEmpty()) {
                    statusBarTextNow = statusBarTextNow.dropLast(1)
                }
            }
            delay(75)
            continue
        }

        var hasNewWork = false
        mutex.withLock {
            if (listProgressedStatusBar.isNotEmpty()) {
                workText = listProgressedStatusBar.last()
                startSize = listProgressedStatusBar.size
                isProcessed = true
                hasNewWork = true
            }
        }

        if (hasNewWork) {
            continue
        }

        if (statusBarTextNow.isNotEmpty()) {
            statusBarTextNow = statusBarTextNow.dropLast(1)
        }
        delay(75)
    }
}

private var addedEllipsis = 0