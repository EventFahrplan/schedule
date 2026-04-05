package info.metadude.kotlin.library.schedule.v1.serializers

import info.metadude.kotlin.library.schedule.v1.models.RoomDescription
import info.metadude.kotlin.library.schedule.v1.models.RoomDescription.Text
import info.metadude.kotlin.library.schedule.v1.models.RoomDescription.Translations
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive

object RoomDescriptionSerializer : KSerializer<RoomDescription> {

    override val descriptor: SerialDescriptor = JsonObject.serializer().descriptor

    override fun deserialize(decoder: Decoder): RoomDescription {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("RoomDescriptionSerializer supports JSON only.")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> Text(element.content)
            is JsonObject -> Translations(element.mapValues { (_, value) -> value.jsonPrimitive.content })
            else -> error("Unsupported room description value: $element")
        }
    }

    override fun serialize(encoder: Encoder, value: RoomDescription) {
        val jsonEncoder = encoder as? JsonEncoder
            ?: error("RoomDescriptionSerializer supports JSON only.")
        val element = when (value) {
            is Text -> JsonPrimitive(value.value)
            is Translations -> buildJsonObject {
                value.values.forEach { (key, text) -> put(key, JsonPrimitive(text)) }
            }
        }
        jsonEncoder.encodeJsonElement(element)
    }
}
