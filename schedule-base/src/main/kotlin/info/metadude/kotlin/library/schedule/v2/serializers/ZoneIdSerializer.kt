package info.metadude.kotlin.library.schedule.v2.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.threeten.bp.ZoneId

internal object ZoneIdSerializer : KSerializer<ZoneId> {

    override val descriptor = PrimitiveSerialDescriptor("ZoneId", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ZoneId = ZoneId.of(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: ZoneId) = encoder.encodeString(value.id)
}
