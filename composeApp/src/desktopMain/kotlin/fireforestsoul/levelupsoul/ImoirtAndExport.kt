package fireforestsoul.levelupsoul

import java.io.File
import javax.swing.JFileChooser

actual fun export() {
    saveValue()
    val chooser = JFileChooser()
    chooser.dialogTitle = "Сохранить файл"
    val result = chooser.showSaveDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        val file = chooser.selectedFile
        file.writeText(File(save_file_name).readText())
    }
}

actual fun import(onImported: () -> Unit)
{
    val chooser = JFileChooser()
    val result = chooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        val selectedFile: File = chooser.selectedFile
        File(save_file_name).printWriter().use { out ->
            out.print(selectedFile.readText())
        }
    }
    onImported()
}