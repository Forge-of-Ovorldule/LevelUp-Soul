package fireforestsoul.levelupsoul

import kotlinx.serialization.json.Json

object LocalSaveManager {
    private const val FILE_NAME = "levelupsoul-local-data.json"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    var data: LocalData = load()
        private set

    private fun load(): LocalData {
        if (AppVersionSaveManager.data.appVersion <= 0) {
            return OldSaveSystem.loadToLocalData()
        } else {
            val raw = SaveStorage.load(FILE_NAME) ?: return LocalData()

            return runCatching {
                json.decodeFromString<LocalData>(raw)
            }.getOrElse {
                LocalData()
            }
        }
    }

    fun save() {
        SaveStorage.save(
            FILE_NAME,
            json.encodeToString(data)
        )
    }
}