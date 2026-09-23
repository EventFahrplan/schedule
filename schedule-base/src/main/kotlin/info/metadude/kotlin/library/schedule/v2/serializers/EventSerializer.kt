package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.Event
import info.metadude.kotlin.library.schedule.v2.models.MetaEvent
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEvent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal object EventSerializer : JsonContentPolymorphicSerializer<Event>(Event::class) {

    override fun selectDeserializer(element: JsonElement): KSerializer<out Event> =
        when (element.jsonObject["type"]?.jsonPrimitive?.content) {
            "BREAK", "META" -> MetaEvent.serializer()
            else -> ScheduledEvent.serializer()
        }

}
