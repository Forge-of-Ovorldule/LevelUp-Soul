package fireforestsoul.levelupsoul

expect object SaveStorage {
    fun load(fileName: String): String?
    fun save(fileName: String, data: String)
}