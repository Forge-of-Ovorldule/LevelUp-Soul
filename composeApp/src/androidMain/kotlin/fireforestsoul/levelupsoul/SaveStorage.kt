package fireforestsoul.levelupsoul

import java.io.File

actual object SaveStorage {

    actual fun load(fileName: String): String? {
        val file = getFile(fileName)
        return if (file.exists()) {
            try {
                file.readText()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    actual fun save(fileName: String, data: String) {
        val file = getFile(fileName)
        try {
            file.parentFile?.mkdirs()
            file.writeText(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getFile(fileName: String): File {
        val context = SaveStorageProvider.getContext()
        return File(context.filesDir, "$fileName.json")
    }
}
