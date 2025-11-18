package fireforestsoul.levelupsoul

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.times
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect

@Composable
fun TextWithDeployableEllipsis(
    backgroundColor: Color,
    newStatusBarInfo: StatusBarInfo,
    hazeState: HazeState,
    text: String,
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

    BoxWithConstraints {
        var width by mutableStateOf(maxWidth)

        if (displayText.length > with(LocalDensity.current) { width.toPx() / (51f / 85 * displayFontSize.toPx()) }) {
            Row {
                Text(
                    text = text.take(with(LocalDensity.current) { width.toPx() / (51f / 85 * displayFontSize.toPx()) }.toInt() - 2),
                    maxLines = 1,
                    color = displayColor,
                    fontSize = displayFontSize,
                    fontWeight = fontWeight,
                    fontFamily = fontFamily,
                    textAlign = textAlign
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            println("\n" + displayText)
                            newStatusBarInfo.text = displayText
                            newStatusBarInfo.textColor = displayColor
                            statusBarInfo = newStatusBarInfo
                            println("$statusBarInfo\n$newStatusBarInfo")
                                   },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .border(0.35.dp, displayColor.multiply(0.5f, 0.5f, 0.5f, 0.5f), RoundedCornerShape(5.22.dp))
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
}
