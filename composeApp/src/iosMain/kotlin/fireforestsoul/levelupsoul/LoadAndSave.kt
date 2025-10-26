package fireforestsoul.levelupsoul

import platform.Foundation.NSUserDefaults

private val userDefaults = NSUserDefaults.standardUserDefaults()

actual fun saveValue() {
    userDefaults.setInteger(app_version, forKey = "app_version")
}

actual fun old1001000000LoadAllValues() {
    val oldAppVersion =
        if (userDefaults.objectForKey("app_version") != null) userDefaults.integerForKey("app_version") else app_version
//    if (oldAppVersion) {
//        loading old type
//    }
}