package info.metadude.kotlin.library.schedule.v1.serializers

import info.metadude.kotlin.library.schedule.v1.models.ConferenceDate
import info.metadude.kotlin.library.schedule.v1.models.ConferenceDate.Date
import info.metadude.kotlin.library.schedule.v1.models.ConferenceDate.DateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

object ConferenceDateSerializer : KSerializer<ConferenceDate> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ConferenceDate", STRING)

    override fun deserialize(decoder: Decoder): ConferenceDate {
        val value = decoder.decodeString()
        return try {
            DateTime(OffsetDateTime.parse(value))
        } catch (_: Exception) {
            Date(LocalDate.parse(value))
        }
    }

    override fun serialize(encoder: Encoder, value: ConferenceDate) {
        val serialized = when (value) {
            is Date -> value.value.toString()
            is DateTime -> value.value.toString()
        }
        encoder.encodeString(serialized)
    }
}
