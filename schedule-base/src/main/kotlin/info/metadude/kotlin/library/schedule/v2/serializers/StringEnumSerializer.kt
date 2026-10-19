package info.metadude.kotlin.library.schedule.v2.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal abstract class StringEnumSerializer<T>(private val name: String) : KSerializer<T> {

    override val descriptor = PrimitiveSerialDescriptor(name, PrimitiveKind.STRING)

    abstract fun fromWire(value: String): T

    abstract fun toWire(value: T): String

    override fun deserialize(decoder: Decoder): T = fromWire(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(toWire(value))
}
