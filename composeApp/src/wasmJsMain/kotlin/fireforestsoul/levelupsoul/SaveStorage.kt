package fireforestsoul.levelupsoul

import kotlinx.browser.window

actual object SaveStorage {

    actual fun load(fileName: String): String? {
        return window.localStorage.getItem(fileName)
    }

    actual fun save(fileName: String, data: String) {
        window.localStorage.setItem(fileName, data)
    }
}