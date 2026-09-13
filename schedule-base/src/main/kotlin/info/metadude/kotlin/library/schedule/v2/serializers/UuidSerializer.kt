package info.metadude.kotlin.library.schedule.v2.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.uuid.Uuid

internal object UuidSerializer : KSerializer<Uuid> {

    override val descriptor = PrimitiveSerialDescriptor("Uuid", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Uuid {
        val value = decoder.decodeString()
        return try {
            Uuid.parse(value)
        } catch (e: IllegalArgumentException) {
            throw SerializationException("Invalid UUID: $value", e)
        }
    }

    override fun serialize(encoder: Encoder, value: Uuid) = encoder.encodeString(value.toString())
}
