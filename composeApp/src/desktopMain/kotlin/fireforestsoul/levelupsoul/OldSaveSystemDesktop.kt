/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import fireforestsoul.levelupsoul.OldSaveSystem.loadedElementToVal
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File

private const val save_file_name: String = "levelup-soul-saving-by-forge-of-ovorldule"
private val json = Json { prettyPrint = true }
private val settingsFile = File("$save_file_name.json")

internal fun readSettings(): MutableMap<String, JsonElement> {
    if (!settingsFile.exists()) return mutableMapOf()
    val text = settingsFile.readText()
    if (text.isBlank()) return mutableMapOf()
    return json.parseToJsonElement(text).jsonObject.toMutableMap()
}

actual object HelpOldSaveSystem {
    actual fun <T> loadValue(value: T, name: String): T {
        val settings = readSettings()
        val jsonElement = settings[name] ?: return value
        var element = jsonElement.jsonPrimitive.toString()
        element = element.substring(1, element.length - 1)

        return element.loadedElementToVal(value)
    }
}