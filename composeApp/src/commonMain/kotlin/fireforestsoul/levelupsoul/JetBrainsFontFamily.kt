/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font

@Composable
fun jetBrainsFont(): FontFamily {
    return FontFamily(
        Font(Res.font.JetBrainsMono_ExtraBold, FontWeight.ExtraBold, FontStyle.Normal),
        Font(Res.font.JetBrainsMono_Italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.JetBrainsMono_Medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.JetBrainsMono_Bold, FontWeight.Bold, FontStyle.Normal),
        Font(Res.font.JetBrainsMono_Regular, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.JetBrainsMono_Bold_Italic, FontWeight.Bold, FontStyle.Italic),
        Font(Res.font.JetBrainsMono_ExtraBold_Italic, FontWeight.ExtraBold, FontStyle.Italic),
        Font(Res.font.JetBrainsMono_Medium_Italic, FontWeight.Normal, FontStyle.Italic)
    )
}