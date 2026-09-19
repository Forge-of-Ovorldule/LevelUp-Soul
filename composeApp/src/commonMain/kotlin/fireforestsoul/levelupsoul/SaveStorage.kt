@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package fireforestsoul.levelupsoul

expect object SaveStorage {
    fun load(fileName: String): String?
    fun save(fileName: String, data: String)
}