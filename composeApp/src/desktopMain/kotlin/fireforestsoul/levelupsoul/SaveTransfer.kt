package fireforestsoul.levelupsoul

import java.awt.Dialog
import java.awt.FileDialog
import java.io.File
import java.util.concurrent.CompletableFuture
import javax.swing.SwingUtilities

actual object SaveTransfer {

    actual fun exportToFile(fileName: String, content: String) {
        withEdt {
            val dialog = FileDialog(null as Dialog?, "Сохранить игру", FileDialog.SAVE)
            dialog.file = fileName
            dialog.isVisible = true

            val dir = dialog.directory
            val name = dialog.file
            if (dir != null && name != null) {
                runCatching { File(dir, name).writeText(content) }
                    .onFailure { it.printStackTrace() }
            }
        }
    }

    actual fun importFromFile(onResult: (String?) -> Unit) {
        withEdt {
            val dialog = FileDialog(null as Dialog?, "Выберите файл сохранения", FileDialog.LOAD)
            dialog.isVisible = true

            val dir = dialog.directory
            val name = dialog.file
            val content = if (dir != null && name != null) {
                runCatching { File(dir, name).readText() }.getOrNull()
            } else {
                null
            }
            onResult(content)
        }
    }

    private fun <T> withEdt(block: () -> T): T {
        if (SwingUtilities.isEventDispatchThread()) return block()
        val future = CompletableFuture<T>()
        SwingUtilities.invokeAndWait {
            try {
                future.complete(block())
            } catch (t: Throwable) {
                future.completeExceptionally(t)
            }
        }
        return future.join()
    }
}
