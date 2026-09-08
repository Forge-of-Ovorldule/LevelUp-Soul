@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package fireforestsoul.levelupsoul

expect object SaveTransfer {
    fun exportToFile(fileName: String, content: String)
    fun importFromFile(onResult: (String?) -> Unit)
}
