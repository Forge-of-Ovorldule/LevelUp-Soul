package fireforestsoul.levelupsoul

object SaveImportExport {

    const val DEFAULT_FILE_NAME = "levelupsoul-save.json"

    fun exportSave() {
        SaveTransfer.exportToFile(DEFAULT_FILE_NAME, LocalSaveManager.exportData())
    }

    fun importSave(onResult: (Boolean) -> Unit = {}) {
        SaveTransfer.importFromFile { raw ->
            onResult(raw != null && LocalSaveManager.importData(raw))
        }
    }

    fun restoreBackup(): Boolean = LocalSaveManager.restoreBackup()
}
