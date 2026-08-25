package fireforestsoul.levelupsoul

import android.app.Activity
import android.content.Intent

actual object SaveTransfer {

    private const val REQUEST_EXPORT = 9001
    private const val REQUEST_IMPORT = 9002

    private var pendingExport: Pair<String, String>? = null
    private var pendingImport: ((String?) -> Unit)? = null

    actual fun exportToFile(fileName: String, content: String) {
        val activity = SaveStorageProvider.currentActivity()
        if (activity != null) {
            pendingExport = fileName to content
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, fileName)
            }
            try {
                activity.startActivityForResult(intent, REQUEST_EXPORT)
                return
            } catch (e: Exception) {
                pendingExport = null
                e.printStackTrace()
            }
        }
        shareText(fileName, content)
    }

    actual fun importFromFile(onResult: (String?) -> Unit) {
        val activity = SaveStorageProvider.currentActivity()
        if (activity == null) {
            onResult(null)
            return
        }
        pendingImport = onResult
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        try {
            activity.startActivityForResult(intent, REQUEST_IMPORT)
        } catch (e: Exception) {
            pendingImport = null
            e.printStackTrace()
            onResult(null)
        }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_EXPORT -> {
                val (fileName, content) = pendingExport ?: return
                pendingExport = null
                if (resultCode != Activity.RESULT_OK) return
                val uri = data?.data ?: return
                runCatching {
                    SaveStorageProvider.getContext()
                        .contentResolver
                        .openOutputStream(uri)
                        ?.use { it.write(content.toByteArray(Charsets.UTF_8)) }
                }.onFailure { it.printStackTrace() }
            }

            REQUEST_IMPORT -> {
                val callback = pendingImport ?: return
                pendingImport = null
                if (resultCode != Activity.RESULT_OK) {
                    callback(null)
                    return
                }
                val uri = data?.data
                val text = uri?.let {
                    runCatching {
                        SaveStorageProvider.getContext()
                            .contentResolver
                            .openInputStream(it)
                            ?.use { input -> input.readBytes().toString(Charsets.UTF_8) }
                    }.getOrNull()
                }
                callback(text)
            }
        }
    }

    private fun shareText(fileName: String, content: String) {
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
        val chooser = Intent.createChooser(send, "Поделиться сохранением")
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        SaveStorageProvider.getContext().startActivity(chooser)
    }
}
