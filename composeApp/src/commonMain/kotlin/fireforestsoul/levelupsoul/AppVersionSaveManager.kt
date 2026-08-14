package fireforestsoul.levelupsoul

import kotlinx.serialization.json.Json

object AppVersionSaveManager {
    private const val FILE_NAME = "levelupsoul-app-version-data.json"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    var data: AppVersionData = load()
        private set

    private fun load(): AppVersionData {
        val raw = SaveStorage.load(FILE_NAME) ?: return AppVersionData()

        return runCatching {
            json.decodeFromString<AppVersionData>(raw)
        }.getOrElse {
            AppVersionData()
        }
    }

    fun save() {
        SaveStorage.save(
            FILE_NAME,
            json.encodeToString(data)
        )
    }
}