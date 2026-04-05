package info.metadude.kotlin.library.schedule.v1.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.Duration

object DurationSerializer : KSerializer<Duration> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Duration", STRING)

    override fun deserialize(decoder: Decoder): Duration {
        val value = decoder.decodeString()
        val parts = value.split(":")
        require(parts.size == 2) { "Expected duration in H+:MM format but was $value" }
        val hours = parts[0].toLong()
        val minutes = parts[1].toLong()
        require(minutes in 0..59) { "Expected minute value between 00 and 59 but was $value" }
        return Duration.ofHours(hours).plusMinutes(minutes)
    }

    override fun serialize(encoder: Encoder, value: Duration) {
        val totalMinutes = value.toMinutes()
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        encoder.encodeString("%02d:%02d".format(hours, minutes))
    }
}
