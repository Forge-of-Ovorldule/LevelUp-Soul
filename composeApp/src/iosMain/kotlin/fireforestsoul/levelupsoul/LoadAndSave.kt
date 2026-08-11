package fireforestsoul.levelupsoul

import platform.Foundation.NSUserDefaults

private val userDefaults = NSUserDefaults.standardUserDefaults()

actual fun saveValue() {
    userDefaults.setInteger(app_version, forKey = "app_version")
}

actual fun old1001000000LoadAllValues() {
    //    if (oldAppVersion) {
//        loading old type
//    }
}