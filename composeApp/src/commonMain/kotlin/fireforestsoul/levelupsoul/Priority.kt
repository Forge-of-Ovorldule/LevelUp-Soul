package fireforestsoul.levelupsoul

import kotlinx.serialization.Serializable

@Serializable(with = EnumSaveSerializer.PrioritySerializer::class)
enum class Priority {
    NO_PRIORITY,
    HIGH_PRIORITY,
    MEDIUM_PRIORITY,
    LOW_PRIORITY
}