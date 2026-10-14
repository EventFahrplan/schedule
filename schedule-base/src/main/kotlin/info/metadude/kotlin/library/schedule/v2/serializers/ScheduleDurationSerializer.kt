package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.ScheduleDuration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object ScheduleDurationSerializer : KSerializer<ScheduleDuration> {

    private val regex = Regex("^P(?!$)(?:\\d+Y)?(?:\\d+M)?(?:\\d+W)?(?:\\d+D)?(?:T(?=\\d)(?:\\d+H)?(?:\\d+M)?(?:\\d+(?:\\.\\d+)?S)?)?$")

    override val descriptor = PrimitiveSerialDescriptor("ScheduleDuration", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ScheduleDuration {
        val value = decoder.decodeString()
        require(regex.matches(value)) { "Invalid ISO 8601 duration: $value" }
        return ScheduleDuration(value)
    }

    override fun serialize(encoder: Encoder, value: ScheduleDuration) = encoder.encodeString(value.value)
}
