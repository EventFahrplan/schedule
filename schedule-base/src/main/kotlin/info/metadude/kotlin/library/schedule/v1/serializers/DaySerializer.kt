package info.metadude.kotlin.library.schedule.v1.serializers

import info.metadude.kotlin.library.schedule.Logging
import info.metadude.kotlin.library.schedule.Logging.Companion.None
import info.metadude.kotlin.library.schedule.v1.models.Day
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

class DaySerializer(
    private val logging: Logging = None,
) : KSerializer<Day> {

    override val descriptor: SerialDescriptor = Day.serializer().descriptor

    override fun deserialize(decoder: Decoder): Day {
        val jsonDecoder = decoder as? JsonDecoder
            ?: error("DaySerializer supports JSON only")
        val json = jsonDecoder.json
        val day = jsonDecoder.decodeJsonElement().jsonObject
        return Day(
            index = json.decodeFromJsonElement(Int.serializer(), day.getValue("index")),
            date = json.decodeFromJsonElement(LocalDateSerializer, day.getValue("date")),
            dayStart = json.decodeFromJsonElement(OffsetDateTimeSerializer, day.getValue("day_start")),
            dayEnd = json.decodeFromJsonElement(OffsetDateTimeSerializer, day.getValue("day_end")),
            rooms = json.decodeFromJsonElement(RoomsSerializer(logging), day.getValue("rooms")),
        )
    }

    override fun serialize(encoder: Encoder, value: Day) {
        encoder.encodeSerializableValue(Day.serializer(), value)
    }
}
