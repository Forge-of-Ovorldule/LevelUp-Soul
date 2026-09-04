package fireforestsoul.levelupsoul

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

object EnumSaveSerializer {
    inline fun <reified T : Enum<T>> defaultOnUnknown(
        default: T,
    ): KSerializer<T> = object : KSerializer<T> {

        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Enum(${T::class.simpleName})", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: T) =
            encoder.encodeString(value.name)

        override fun deserialize(decoder: Decoder): T {
            val json = decoder as? JsonDecoder ?: return default
            val element = json.decodeJsonElement()
            val name = (element as? JsonPrimitive)?.contentOrNull ?: return default
            return enumValues<T>().firstOrNull { it.name == name } ?: default
        }
    }

    object ScreenManagerSerializer : KSerializer<ScreenManager> by defaultOnUnknown<ScreenManager>(ScreenManager.TABLE)
    object PrioritySerializer : KSerializer<Priority> by defaultOnUnknown<Priority>(Priority.NO_PRIORITY)
}
