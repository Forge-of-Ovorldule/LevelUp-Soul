package fireforestsoul.levelupsoul

import androidx.compose.ui.graphics.Color
import com.ionspin.kotlin.bignum.decimal.BigDecimal
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

object BigDecimalAsStringSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toStringExpanded())
    }

    override fun deserialize(decoder: Decoder): BigDecimal =
        BigDecimal.parseString(decoder.decodeString())
}

object ColorAsStringSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("androidx.compose.ui.graphics.Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString(value.value.toString(16).padStart(16, '0'))
    }

    override fun deserialize(decoder: Decoder): Color {
        val hex = decoder.decodeString()
        return Color(hex.toULongOrNull(16) ?: "ffffffff00000000".toULong(16))
    }
}