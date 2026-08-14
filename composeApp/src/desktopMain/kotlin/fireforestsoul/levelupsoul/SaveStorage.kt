package fireforestsoul.levelupsoul

import okio.FileSystem
import okio.Path.Companion.toPath

actual object SaveStorage {

    actual fun load(fileName: String): String? {
        val file = "$fileName.json".toPath()

        return runCatching {
            FileSystem.SYSTEM.read(file) {
                readUtf8()
            }
        }.getOrNull()
    }

    actual fun save(fileName: String, data: String) {
        val file = "$fileName.json".toPath()

        FileSystem.SYSTEM.write(file) {
            writeUtf8(data)
        }
    }
}