package fireforestsoul.levelupsoul

import kotlinx.serialization.json.Json

object LocalSaveManager {
    private const val FILE_NAME = "levelupsoul-local-data.json"
    private const val BACKUP_FILE_NAME = "levelupsoul-local-data-backup.json"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    private val prettyJson = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
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

    fun exportData(): String = prettyJson.encodeToString(data)

    fun importData(raw: String): Boolean {
        val newData = runCatching {
            prettyJson.decodeFromString<LocalData>(raw.trim())
        }.getOrNull() ?: return false

        backupCurrent()

        data = newData
        save()
        return true
    }

    fun restoreBackup(): Boolean {
        val raw = SaveStorage.load(BACKUP_FILE_NAME) ?: return false
        val restored = runCatching {
            prettyJson.decodeFromString<LocalData>(raw)
        }.getOrNull() ?: return false

        data = restored
        save()
        return true
    }

    private fun backupCurrent() {
        val raw = runCatching { prettyJson.encodeToString(data) }.getOrNull() ?: return
        SaveStorage.save(BACKUP_FILE_NAME, raw)
    }

}