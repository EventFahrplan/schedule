package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.ScheduleJson
import info.metadude.kotlin.library.schedule.v2.models.EventState
import info.metadude.kotlin.library.schedule.v2.models.MetaEvent
import info.metadude.kotlin.library.schedule.v2.models.MetaEventType
import info.metadude.kotlin.library.schedule.v2.models.PlannedEvent
import info.metadude.kotlin.library.schedule.v2.models.ScheduleDuration
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEvent
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventType
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.OffsetDateTime

internal class EventSerializerTest {

    private val json = ScheduleJson.create()

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
        val wire = json.encodeToJsonElement(ScheduledEventTypeSerializer, type).jsonPrimitive.content
        val event = json.decodeFromString(EventSerializer, scheduledEventJson(wire))
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
    fun `round trip preserves planned event with duration and without start, end and slug`() {
        val event = json.decodeFromString(EventSerializer, plannedEventJson(""""duration": "PT1H","""))
        assertThat(event).isInstanceOf(PlannedEvent::class.java)
        event as PlannedEvent
        assertThat(event.start).isNull()
        assertThat(event.duration).isEqualTo(ScheduleDuration("PT1H"))
        assertThat(event.state).isEqualTo(EventState.DRAFT)
        assertThat(event.slug).isNull()
        val encoded = json.encodeToString(EventSerializer, event)
        assertThat(json.decodeFromString(EventSerializer, encoded)).isEqualTo(event)
    }

    @Test
    fun `deserialize start without end selects planned event`() {
        val event = json.decodeFromString(EventSerializer, plannedEventJson(""""start": "2025-12-27T10:00:00+01:00","""))
        assertThat(event).isInstanceOf(PlannedEvent::class.java)
        event as PlannedEvent
        assertThat(event.start).isEqualTo(OffsetDateTime.parse("2025-12-27T10:00:00+01:00"))
        assertThat(event.duration).isNull()
    }

    @Test
    fun `deserialize null start and end selects planned event`() {
        val event = json.decodeFromString(EventSerializer, plannedEventJson(""""start": null, "end": null,"""))
        assertThat(event).isInstanceOf(PlannedEvent::class.java)
        assertThat((event as PlannedEvent).start).isNull()
    }

    @Test
    fun `deserialize end without start selects planned event and drops end`() {
        val event = json.decodeFromString(EventSerializer, plannedEventJson(""""end": "2025-12-27T10:30:00+01:00","""))
        assertThat(event).isInstanceOf(PlannedEvent::class.java)
        assertThat((event as PlannedEvent).start).isNull()
    }

    @Test
    fun `deserialize unknown type without times selects planned event with UNKNOWN type`() {
        val event = json.decodeFromString(EventSerializer, plannedEventJson("", type = "future-type"))
        assertThat(event).isInstanceOf(PlannedEvent::class.java)
        assertThat((event as PlannedEvent).type).isEqualTo(ScheduledEventType.UNKNOWN)
    }

    @Test
    fun `deserialize rejects missing type`() {
        val wire = json.parseToJsonElement(scheduledEventJson("TALK")).jsonObject
        assertThrows<SerializationException> {
            json.decodeFromJsonElement(EventSerializer, JsonObject(wire - "type"))
        }
    }

    private fun plannedEventJson(timing: String, type: String = "WORKSHOP") = """
        {
            "type": "$type",
            "guid": "550e8400-e29b-41d4-a716-446655440000",
            "title": "Workshop",
            "subtitle": null,
            "abstract": null,
            "languages": null,
            $timing
            "state": "DRAFT",
            "url": "https://example.com/workshop",
            "participants": []
        }
    """.trimIndent()

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
