package fireforestsoul.levelupsoul

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun TextWithDeployableEllipsis(
    backgroundColor: Color,
    newStatusBarInfo: StatusBarInfo,
    hazeState: HazeState?,
    contentBefore: @Composable () -> Unit = {},
    text: String,
    contentAfter: @Composable () -> Unit = {},
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    textAlign: TextAlign = TextAlign.Unspecified
) {
    var isOverflowing by remember(text) { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {

        contentBefore()

        Row(
            modifier = Modifier.weight(1f, fill = false),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                maxLines = 1,
                modifier = Modifier.weight(1f, fill = false),
                overflow = TextOverflow.Clip,
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                textAlign = textAlign,
                onTextLayout = { result ->
                    if (isOverflowing != result.hasVisualOverflow) {
                        isOverflowing = result.hasVisualOverflow
                    }
                },
                softWrap = false
            )

            if (isOverflowing) {

                Box(
                    modifier = Modifier
                        .clickable {
                            newStatusBarInfo.text = text
                            newStatusBarInfo.textColor = color
                            updateTimerForStatusBar = true
                            statusBarInfo = newStatusBarInfo
                        }
                        .padding(
                            start = with(LocalDensity.current) { fontSize.toDp() / 6.8f },
                            end = with(LocalDensity.current) { fontSize.toDp() / 3.4f }),
                    contentAlignment = Alignment.Center
                ) {
                    var boxMod = Modifier
                        .border(
                            with(LocalDensity.current) { fontSize.toDp() / 85f },
                            color.multiply(0.5f, 0.5f, 0.5f, 0.5f),
                            RoundedCornerShape(with(LocalDensity.current) { fontSize.toDp() / 5.67f })
                        )
                        .clip(RoundedCornerShape(with(LocalDensity.current) { fontSize.toDp() / 5.67f }))
                    if (hazeState != null) {
                        boxMod = boxMod.hazeEffect(state = hazeState) {
                            style = HazeStyle(
                                tint = HazeTint(UIC_white.copy(0f)),
                                blurRadius = 8.7.dp,
                                noiseFactor = 0f,
                                backgroundColor = backgroundColor,
                            )
                        }

                    }
                    Box(
                        modifier = boxMod
                            .padding(horizontal = with(LocalDensity.current) { fontSize.toDp() / 8.5f }),
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
