/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import android.annotation.SuppressLint
import android.content.Context
import fireforestsoul.levelupsoul.OldSaveSystem.loadedElementToVal

@SuppressLint("StaticFieldLeak")
var context: Context? = null

fun initStorage(appContext: Context) {
    context = appContext
}

actual object HelpOldSaveSystem {
    private const val SAVE_FILE_NAME: String = "levelup-soul-saving-by-forge-of-ovorldule"

    actual fun <T> loadValue(value: T, name: String): T {
        val serialized =
            (context ?: return value).getSharedPreferences(SAVE_FILE_NAME, Context.MODE_PRIVATE).getString(name, null)
                ?: return value

        return serialized.loadedElementToVal(value)
    }

}