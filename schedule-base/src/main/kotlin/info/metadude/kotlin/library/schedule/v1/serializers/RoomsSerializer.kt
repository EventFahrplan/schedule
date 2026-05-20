package info.metadude.kotlin.library.schedule.v1.serializers

import info.metadude.kotlin.library.schedule.Logging
import info.metadude.kotlin.library.schedule.Logging.Companion.None
import info.metadude.kotlin.library.schedule.v1.models.Event
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class RoomsSerializer(
    private val logging: Logging = None,
) : KSerializer<Map<String, List<Event>>> {

    private val delegateSerializer = MapSerializer(String.serializer(), ListSerializer(Event.serializer()))

    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun deserialize(decoder: Decoder): Map<String, List<Event>> {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("RoomsSerializer supports JSON only")
        val rooms = jsonDecoder.decodeJsonElement().jsonObject
        return rooms.mapValues { (_, events) ->
            events.jsonArray.mapNotNull { event ->
                runCatching { jsonDecoder.json.decodeFromJsonElement(Event.serializer(), event) }
                    .onFailure { logging.onDeserializeFailed(event.toString(), it) }
                    .getOrNull()
            }
        }
    }

    override fun serialize(encoder: Encoder, value: Map<String, List<Event>>) {
        encoder.encodeSerializableValue(delegateSerializer, value)
    }
}
