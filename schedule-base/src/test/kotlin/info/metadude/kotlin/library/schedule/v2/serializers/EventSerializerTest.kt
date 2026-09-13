package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.MetaEvent
import info.metadude.kotlin.library.schedule.v2.models.MetaEventType
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEvent
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventType
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class EventSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(strings = ["BREAK", "META"])
    fun `round trip preserves meta event subtype`(type: String) {
        val wire = """
            {
                "type": "$type",
                "title": "Break",
                "start": "2025-12-27T10:00:00+01:00",
                "end": "2025-12-27T10:30:00+01:00"
            }
        """.trimIndent()
        val event = json.decodeFromString(EventSerializer, wire)
        assertThat(event).isInstanceOf(MetaEvent::class.java)
        assertThat((event as MetaEvent).type).isEqualTo(MetaEventType.valueOf(type))
        assertThat(event.title).isEqualTo("Break")
        val encoded = json.encodeToString(EventSerializer, event)
        assertThat(json.decodeFromString(EventSerializer, encoded)).isEqualTo(event)
    }

    @ParameterizedTest
    @EnumSource(ScheduledEventType::class)
    fun `round trip preserves scheduled event subtype`(type: ScheduledEventType) {
        val event = json.decodeFromString(EventSerializer, scheduledEventJson(type.name))
        assertThat(event).isInstanceOf(ScheduledEvent::class.java)
        assertThat((event as ScheduledEvent).type).isEqualTo(type)
        assertThat(event.title).isEqualTo("Talk")
        val encoded = json.encodeToString(EventSerializer, event)
        assertThat(json.decodeFromString(EventSerializer, encoded)).isEqualTo(event)
    }

    @Test
    fun `deserialize unknown type selects scheduled event with UNKNOWN type`() {
        val event = json.decodeFromString(EventSerializer, scheduledEventJson("future-type"))
        assertThat(event).isInstanceOf(ScheduledEvent::class.java)
        assertThat((event as ScheduledEvent).type).isEqualTo(ScheduledEventType.UNKNOWN)
    }

    @Test
    fun `deserialize rejects missing type`() {
        val wire = json.parseToJsonElement(scheduledEventJson("TALK")).jsonObject
        assertThrows<SerializationException> {
            json.decodeFromJsonElement(EventSerializer, JsonObject(wire - "type"))
        }
    }

    private fun scheduledEventJson(type: String) = """
        {
            "type": "$type",
            "guid": "550e8400-e29b-41d4-a716-446655440000",
            "title": "Talk",
            "subtitle": null,
            "abstract": "Summary",
            "start": "2025-12-27T10:00:00+01:00",
            "end": "2025-12-27T10:30:00+01:00",
            "slug": "talk",
            "url": "https://example.com/talk",
            "links": [],
            "participants": []
        }
    """.trimIndent()
}
