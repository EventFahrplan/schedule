package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.LocalizableString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

internal object LocalizableStringSerializer : KSerializer<LocalizableString> {

    private val mapSerializer = MapSerializer(String.serializer(), String.serializer())

    override val descriptor = PrimitiveSerialDescriptor("LocalizableString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalizableString {
        val d = decoder as? JsonDecoder
            ?: throw SerializationException("This serializer can be used only with JSON")
        return when (val e = d.decodeJsonElement()) {
            is JsonPrimitive -> LocalizableString.Text(e.content)
            is JsonObject -> LocalizableString.Translations(e.mapValues { it.value.jsonPrimitive.content })
            else -> throw SerializationException(
                "Unsupported localized value: $e"
            )
        }
    }

    override fun serialize(encoder: Encoder, value: LocalizableString) {
        val e = encoder as? JsonEncoder ?: throw SerializationException("This serializer can be used only with JSON")
        e.encodeJsonElement(
            if (value is LocalizableString.Text) JsonPrimitive(value.value)
            else e.json.encodeToJsonElement(mapSerializer, (value as LocalizableString.Translations).values)
        )
    }
}
