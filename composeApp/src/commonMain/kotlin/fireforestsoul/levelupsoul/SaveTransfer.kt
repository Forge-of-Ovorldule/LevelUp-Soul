package fireforestsoul.levelupsoul

expect object SaveTransfer {
    fun exportToFile(fileName: String, content: String)
    fun importFromFile(onResult: (String?) -> Unit)
}
