/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SaveStorageProvider.init(this)

        initStorage(applicationContext)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        hideSystemBars()

        WindowCompat.getInsetsController(window, window.decorView)
            .addOnControllableInsetsChangedListener { controller, _ ->
                if (controller.isAppearanceLightStatusBars) {
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                }
            }

        setContent {
            App()
        }
    }

    private fun hideSystemBars() {
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SaveTransfer.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        if (loadIsGood)
            LocalSaveManager.save()
        super.onStop()
    }

    override fun onDestroy() {
        if (loadIsGood)
            LocalSaveManager.save()
        super.onDestroy()
    }

    override fun onPause() {
        if (loadIsGood)
            LocalSaveManager.save()
        super.onPause()
    }
}