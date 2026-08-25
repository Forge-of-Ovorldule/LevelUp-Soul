package fireforestsoul.levelupsoul

import platform.Foundation.NSUserDefaults

actual object SaveStorage {

    actual fun load(fileName: String): String? {
        return NSUserDefaults.standardUserDefaults
            .stringForKey(fileName)
    }

    actual fun save(fileName: String, data: String) {
        NSUserDefaults.standardUserDefaults
            .setObject(data, forKey = fileName)
    }
}