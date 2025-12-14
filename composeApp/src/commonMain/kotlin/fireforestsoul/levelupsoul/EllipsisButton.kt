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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
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
    var isOverflowing by remember(text, Unit) { mutableStateOf(false) }

    Row {

        contentBefore()

        if (!isOverflowing) {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                textAlign = textAlign,
                onTextLayout = { result ->
                    if (result.hasVisualOverflow) {
                        isOverflowing = true
                    }
                },
                softWrap = false
            )
        } else {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier.weight(1f),
                    color = color,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    fontFamily = fontFamily,
                    textAlign = textAlign,
                    softWrap = false
                )

                Box(
                    modifier = Modifier
                        .clickable {
                            newStatusBarInfo.text = text
                            newStatusBarInfo.textColor = color
                            statusBarInfo = newStatusBarInfo
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .border(
                                0.35.dp,
                                color.multiply(0.5f, 0.5f, 0.5f, 0.5f),
                                RoundedCornerShape(5.22.dp)
                            )
                            .clip(RoundedCornerShape(5.22.dp))
                            .hazeEffect(
                                hazeState,
                                HazeStyle(
                                    tint = null,
                                    backgroundColor = backgroundColor.copy(0.25f),
                                    blurRadius = 8.7.dp,
                                    noiseFactor = 0f
                                )
                            )
                            .padding(horizontal = 3.48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "…",
                            color = color.multiply(0.5f, 0.5f, 0.5f),
                            fontSize = fontSize,
                            fontWeight = fontWeight,
                            fontFamily = fontFamily
                        )
                    }
                }
            }
        }

        contentAfter()
    }
}
