package fireforestsoul.levelupsoul

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect

@Composable
fun TextWithDeployableEllipsis(
    backgroundColor: Color,
    newStatusBarInfo: StatusBarInfo,
    hazeState: HazeState,
    contentBefore: @Composable () -> Unit = {},
    text: String,
    contentAfter: @Composable () -> Unit = {},
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    textAlign: TextAlign = TextAlign.Unspecified
) {
    val displayText by mutableStateOf(text)
    val displayColor by mutableStateOf(color)
    val displayBackgroundColor by mutableStateOf(backgroundColor)
    val displayFontSize by mutableStateOf(fontSize)
    val displayHazeState by mutableStateOf(hazeState)

    var rowWidth by remember { mutableStateOf<Int?>(null) }
    var beforeWidth by remember { mutableStateOf<Int?>(null) }
    var afterWidth by remember { mutableStateOf<Int?>(null) }

    Row(modifier = Modifier.onSizeChanged { rowWidth = it.width }) {
        Box(modifier = Modifier.onSizeChanged { beforeWidth = it.width }) {
            contentBefore()
        }

        val availableTextWidth = (rowWidth ?: 0) - (beforeWidth ?: 0) - (afterWidth ?: 0)
        BoxWithConstraints(
            if (displayText.length > with(LocalDensity.current) { availableTextWidth / (51f / 85 * displayFontSize.toPx()) })
                Modifier.weight(1f) else Modifier
        ) {
            var width by mutableStateOf(maxWidth)

            if (displayText.length > with(LocalDensity.current) { width.toPx() / (51f / 85 * displayFontSize.toPx()) }) {
                Row {
                    val needTake =
                        with(LocalDensity.current) { width.toPx() / (51f / 85 * displayFontSize.toPx()) }.toInt() - 2
                    if (needTake > 0) {
                        Text(
                            text = text.take(needTake),
                            maxLines = 1,
                            color = displayColor,
                            fontSize = displayFontSize,
                            fontWeight = fontWeight,
                            fontFamily = fontFamily,
                            textAlign = textAlign
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                newStatusBarInfo.text = displayText
                                newStatusBarInfo.textColor = displayColor
                                statusBarInfo = newStatusBarInfo
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .border(
                                    0.35.dp,
                                    displayColor.multiply(0.5f, 0.5f, 0.5f, 0.5f),
                                    RoundedCornerShape(5.22.dp)
                                )
                                .padding(horizontal = with(LocalDensity.current) { (10f / 85 * displayFontSize.toPx()).toDp() })
                                .clip(RoundedCornerShape(5.22.dp))
                                .hazeEffect(
                                    displayHazeState,
                                    HazeStyle(
                                        tint = null,
                                        backgroundColor = displayBackgroundColor.copy(0.25f),
                                        blurRadius = 8.7.dp,
                                        noiseFactor = 0f
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "…",
                                color = displayColor.multiply(0.5f, 0.5f, 0.5f),
                                fontSize = displayFontSize,
                                fontWeight = fontWeight,
                                fontFamily = fontFamily,
                                textAlign = textAlign
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = text,
                    color = displayColor,
                    fontSize = displayFontSize,
                    fontWeight = fontWeight,
                    fontFamily = fontFamily,
                    textAlign = textAlign
                )
            }
        }

        Box(modifier = Modifier.onSizeChanged { afterWidth = it.width }) {
            contentAfter()
        }
    }
}
