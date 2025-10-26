/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(25, 25, 25, 255)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BlueFireFlame()
            Spacer(modifier = Modifier.height(40.dp))
            LoadingTextAnimation()
        }
    }
}

var countFilesLoad = 0
private var indexToDateUpdate = 0

fun loading(viewModel: AppViewModel) {
    indexToDateUpdate = countFilesLoad - 2
    if (countFilesLoad == 1) {
        loadAllValues()
    } else if (indexToDateUpdate >= 0 && indexToDateUpdate < habits.size - 1) {
        habits[indexToDateUpdate].updateDate(false)
    } else if (indexToDateUpdate == habits.size - 1) {
        habits[indexToDateUpdate].updateDate(true)
    } else if (countFilesLoad != 0) {
        changeLanguage()
        viewModel.setStatus(backStatus)
    }

    countFilesLoad++
}
