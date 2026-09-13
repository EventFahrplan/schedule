package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.Event
import info.metadude.kotlin.library.schedule.v2.models.MetaEvent
import info.metadude.kotlin.library.schedule.v2.models.PlannedEvent
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEvent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal object EventSerializer : JsonContentPolymorphicSerializer<Event>(Event::class) {

    override fun selectDeserializer(element: JsonElement): KSerializer<out Event> {
        val event = element.jsonObject
        return when (event["type"]?.jsonPrimitive?.content) {
            "BREAK", "META" -> MetaEvent.serializer()
            else -> if (event.hasValue("start") && event.hasValue("end")) {
                ScheduledEvent.serializer()
            } else {
                PlannedEvent.serializer()
            }
        }
    }

    private fun JsonObject.hasValue(key: String) = this[key].let { it != null && it != JsonNull }

}
