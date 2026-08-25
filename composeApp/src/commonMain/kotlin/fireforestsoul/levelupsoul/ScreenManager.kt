package fireforestsoul.levelupsoul

import kotlinx.serialization.Serializable

@Serializable
enum class ScreenManager {
    LOADING,
    TABLE,
    CREATE_HABIT,
    HABIT_STATISTICS,
    EDIT_HABIT,
    TABLE_UPDATER,
    SOUL_STATISTICS,
    HABITS_LIST,
    HABITS_LIST_UPDATER
}