package fireforestsoul.levelupsoul

import android.content.Context
import java.io.File

object SaveStorageProvider {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context
    }

    fun getContext(): Context {
        return appContext ?: throw IllegalStateException(
            "SaveStorageProvider not initialized! Call SaveStorageProvider.init(context) in Application.onCreate()"
        )
    }
}

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
