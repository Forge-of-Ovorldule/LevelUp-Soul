/**Copyright 2025 Forge-of-Ovorldule (https://github.com/Forge-of-Ovorldule) and Mr-Soul-Forest (https://github.com/Mr-Soul-Forest)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package fireforestsoul.levelupsoul

import java.io.File
import javax.swing.JFileChooser

actual fun export() {
    val chooser = JFileChooser()
    chooser.dialogTitle = "Сохранить файл"
    val result = chooser.showSaveDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        val file = chooser.selectedFile
        file.writeText(File("$save_file_name.json").readText())
    }
}

actual fun import(onImported: () -> Unit) {
    val chooser = JFileChooser()
    val result = chooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        val selectedFile: File = chooser.selectedFile
        File("$old1001000000_save_file_name.json").writeText(selectedFile.readText())
        File("$save_file_name.json").writeText(selectedFile.readText())
    }
    onImported()
}