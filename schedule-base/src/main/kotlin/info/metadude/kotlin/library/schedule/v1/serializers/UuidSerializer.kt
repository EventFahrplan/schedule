package info.metadude.kotlin.library.schedule.v1.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.uuid.Uuid

object UuidSerializer : KSerializer<Uuid> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Uuid", STRING)

    override fun deserialize(decoder: Decoder): Uuid = Uuid.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: Uuid) {
        encoder.encodeString(value.toString())
    }
}
