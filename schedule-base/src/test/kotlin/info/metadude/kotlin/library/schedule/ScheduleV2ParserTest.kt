package info.metadude.kotlin.library.schedule

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.utils.ScheduleV2Parser
import info.metadude.kotlin.library.schedule.utils.loadJsonFile
import info.metadude.kotlin.library.schedule.v2.models.EventState
import info.metadude.kotlin.library.schedule.v2.models.Generator
import info.metadude.kotlin.library.schedule.v2.models.Image
import info.metadude.kotlin.library.schedule.v2.models.LocalizableString
import info.metadude.kotlin.library.schedule.v2.models.Location
import info.metadude.kotlin.library.schedule.v2.models.MetaEvent
import info.metadude.kotlin.library.schedule.v2.models.MetaEventType
import info.metadude.kotlin.library.schedule.v2.models.Organizer
import info.metadude.kotlin.library.schedule.v2.models.Participant
import info.metadude.kotlin.library.schedule.v2.models.ParticipantRole
import info.metadude.kotlin.library.schedule.v2.models.PlannedEvent
import info.metadude.kotlin.library.schedule.v2.models.RecordingMode
import info.metadude.kotlin.library.schedule.v2.models.ReferenceService
import info.metadude.kotlin.library.schedule.v2.models.ReferenceType
import info.metadude.kotlin.library.schedule.v2.models.RoomReference
import info.metadude.kotlin.library.schedule.v2.models.RoomStream
import info.metadude.kotlin.library.schedule.v2.models.RoomType
import info.metadude.kotlin.library.schedule.v2.models.ScheduleDuration
import info.metadude.kotlin.library.schedule.v2.models.ScheduleState
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEvent
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventType
import info.metadude.kotlin.library.schedule.v2.models.Theme
import info.metadude.kotlin.library.schedule.v2.models.TimeZone
import info.metadude.kotlin.library.schedule.v2.models.TrackColors
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import kotlin.uuid.Uuid

internal class ScheduleV2ParserTest {

    @Test
    fun `parse reads minor fixture with generator, scheduled and meta events`() {
        val json = loadJsonFile("schedule_v2_20261019_beta_1_minor.json")
        val scheduleV2 = ScheduleV2Parser.parse(json)

        assertThat(scheduleV2.schema).isEqualTo("https://c3voc.de/schedule/schema2.json#beta-1")
        assertThat(scheduleV2.schemaVersion).isEqualTo("beta-1")
        assertThat(scheduleV2.generator)
            .isEqualTo(Generator(name = "fixture-generator", url = "https://example.com/generator", version = "2.0.0"))

        val schedule = scheduleV2.schedule
        assertThat(schedule.state).isEqualTo(ScheduleState.PUBLISHED)
        assertThat(schedule.baseUrl).isEqualTo("https://events.ccc.de/congress/2024/hub/")

        val conference = schedule.conference
        assertThat(conference.acronym).isEqualTo("38c3")
        assertThat(conference.description).isEqualTo(LocalizableString.Translations(mapOf("en" to "Fixture conference")))
        assertThat(conference.timeZone).isEqualTo(TimeZone(ZoneId.of("Europe/Berlin")))
        assertThat(conference.defaults?.theme).isEqualTo(Theme.DARK)
        assertThat(conference.days.single().date).isEqualTo(LocalDate.parse("2024-12-27"))
        val room = conference.rooms.single()
        assertThat(room.type).isEqualTo(RoomType.LECTURE_HALL)
        assertThat(room.stream).isEqualTo(RoomStream(id = "s1"))
        assertThat(room.features.recording).isEqualTo(RecordingMode.RECORD_BY_DEFAULT)

        val scheduledEvent = schedule.events[0] as ScheduledEvent
        assertThat(scheduledEvent.guid).isEqualTo(Uuid.parse("270fa44c-821a-577f-8ca3-33e8d4a8c2a7"))
        assertThat(scheduledEvent.type).isEqualTo(ScheduledEventType.CEREMONY)
        assertThat(scheduledEvent.state).isEqualTo(EventState.CONFIRMED)
        assertThat(scheduledEvent.languages).containsExactly("en")
        assertThat(scheduledEvent.abstractText).isEqualTo(LocalizableString.Text("Glad you could make it!"))
        assertThat(scheduledEvent.description)
            .isEqualTo(LocalizableString.Translations(mapOf("en" to "This ceremony will prepare you.")))
        assertThat(scheduledEvent.participants.single().role).isEqualTo(ParticipantRole.SPEAKER)
        assertThat(scheduledEvent.links.map { it.type to it.service }).containsExactly(
            ReferenceType.UNKNOWN to ReferenceService.FORGEJO,
            ReferenceType.ORIGIN to ReferenceService.UNKNOWN,
        ).inOrder()
        assertThat(scheduledEvent.attachments.single().type).isEqualTo(ReferenceType.SLIDES)

        val metaEvent = schedule.events[1] as MetaEvent
        assertThat(metaEvent.type).isEqualTo(MetaEventType.BREAK)
        assertThat(metaEvent.title).isEqualTo("Lunch")
        assertThat(metaEvent.room).isEqualTo(RoomReference(guid = Uuid.parse("ba692ba3-421b-5371-8309-60acc34a3c05")))

        assertThat(schedule.persons.single().biography).isEqualTo("Host")
    }

    @Test
    fun `parse reads October 2026 beta-1 fixture without conference type and with optional room features`() {
        val json = loadJsonFile("schedule_v2_20261019_beta_1.json")
        val scheduleV2 = ScheduleV2Parser.parse(json)
        val schedule = scheduleV2.schedule

        assertThat(scheduleV2.schema).isEqualTo("https://c3voc.de/schedule/schema2.json#beta-1")
        assertThat(scheduleV2.schemaVersion).isEqualTo("beta-1")
        assertThat(schedule.baseUrl).isEqualTo("https://hub.test.c3voc.de")
        assertThat(schedule.state).isEqualTo(ScheduleState.PUBLISHED)

        val conference = schedule.conference
        assertThat(conference.acronym).isEqualTo("39c3")
        assertThat(conference.timeZone).isEqualTo(TimeZone(ZoneId.of("Europe/Berlin")))
        assertThat(conference.days).hasSize(4)
        val firstDay = conference.days.first()
        assertThat(firstDay.start).isEqualTo(OffsetDateTime.parse("2025-12-27T10:00:00+00:00"))
        assertThat(firstDay.end).isEqualTo(OffsetDateTime.parse("2025-12-28T06:00:00+01:00"))
        assertThat(conference.rooms).hasSize(76)
        assertThat(conference.rooms.count { it.type == RoomType.LECTURE_HALL }).isEqualTo(7)
        assertThat(conference.rooms.count { it.features.recording == RecordingMode.UNKNOWN }).isEqualTo(63)
        assertThat(conference.rooms.count { it.features.recording == RecordingMode.RECORD_BY_DEFAULT }).isEqualTo(7)
        assertThat(conference.rooms.count { it.stream != null }).isEqualTo(0)
        assertThat(conference.organizer).isNull()
        assertThat(conference.location).isNull()
        assertThat(conference.tracks).hasSize(9)
        assertThat(conference.tracks.first().colors).isEqualTo(TrackColors(dark = "#0347b4", light = "#0347b4"))
        assertThat(schedule.events).hasSize(1347)

        assertThat(schedule.events.filterIsInstance<PlannedEvent>()).isEmpty()
        val events = schedule.events.map { it as ScheduledEvent }
        assertThat(events.count { it.room == null }).isEqualTo(337)
        assertThat(events.count { it.track == null }).isEqualTo(1111)
        assertThat(events.count { it.languages == null }).isEqualTo(0)
        assertThat(events.count { it.languages.isNullOrEmpty() }).isEqualTo(83)
        assertThat(events.count { it.languages == listOf("de") }).isEqualTo(443)
        assertThat(events.count { it.languages == listOf("en") }).isEqualTo(433)
        assertThat(events.count { it.languages?.size == 2 }).isEqualTo(388)
        assertThat(events.count { it.type == ScheduledEventType.OTHER }).isEqualTo(398)
        assertThat(events.count { it.type == ScheduledEventType.UNKNOWN }).isEqualTo(0)
        assertThat(events.count { it.image != null }).isEqualTo(303)
        assertThat(events.count { it.location != null }).isEqualTo(316)

        assertThat(schedule.persons).hasSize(939)
        assertThat(schedule.persons.count { it.avatar != null }).isEqualTo(290)
        assertThat(schedule.persons.count { it.biography != null }).isEqualTo(939)
        val person = schedule.persons.first()
        assertThat(person.guid).isEqualTo(Uuid.parse("4f09b480-eebf-54b5-b703-781907c5e048"))
        assertThat(person.name).isEqualTo("pajowu")
        assertThat(person.biography).isEqualTo(
            "pajowu macht hacktivism bei zerforschung, tech-stuff für fragdenstaat und versucht die welt ein bisschen bunter zu machen.\n"
        )
    }

    @Test
    fun `parse reads beta-1 features fixture with organizer, locations, room streams and planned events`() {
        val json = loadJsonFile("schedule_v2_20261019_beta_1_features.json")
        val schedule = ScheduleV2Parser.parse(json).schedule

        assertThat(schedule.state).isEqualTo(ScheduleState.DRAFT)

        val conference = schedule.conference
        assertThat(conference.organizer)
            .isEqualTo(Organizer(name = "CCC", title = "Chaos Computer Club e.V.", url = "https://ccc.de/"))
        assertThat(conference.sameAs).containsExactly("https://www.wikidata.org/wiki/Q141455450")
        assertThat(conference.partOf).containsExactly("https://www.wikidata.org/wiki/Q1068013")
        val conferenceLocation = conference.location!!
        assertThat(conferenceLocation.name).isEqualTo("CCH")
        assertThat(conferenceLocation.address).isEqualTo("Messeplatz 1, 20357 Hamburg, Germany")
        assertThat(conferenceLocation.url).isEqualTo("https://cch.de")
        assertThat(conferenceLocation.geolocation!!["type"]?.jsonPrimitive?.content).isEqualTo("Point")
        assertThat(conferenceLocation.sameAs).containsExactly("https://www.wikidata.org/wiki/Q896114")

        val track = conference.tracks.single()
        assertThat(track.name).isEqualTo("Security")
        assertThat(track.slug).isEqualTo("security")
        assertThat(track.type).isEqualTo("MAIN")
        assertThat(track.sameAs).containsExactly("https://www.wikidata.org/wiki/Q141455293")

        assertThat(conference.rooms).hasSize(2)
        val room = conference.rooms[0]
        assertThat(room.stream).isEqualTo(RoomStream(id = "s1"))
        assertThat(room.geolocation!!["type"]?.jsonPrimitive?.content).isEqualTo("Point")
        assertThat(room.sameAs).containsExactly("https://www.openstreetmap.org/node/1")
        val roomWithoutStream = conference.rooms[1]
        assertThat(roomWithoutStream.type).isEqualTo(RoomType.STAGE)
        assertThat(roomWithoutStream.stream).isNull()

        val scheduled = schedule.events[0] as ScheduledEvent
        assertThat(scheduled.start).isEqualTo(OffsetDateTime.parse("2025-01-01T10:00:00+01:00"))
        assertThat(scheduled.end).isEqualTo(OffsetDateTime.parse("2025-01-01T10:30:00+01:00"))
        assertThat(scheduled.image).isEqualTo(Image(url = "https://example.com/scheduled.png"))
        assertThat(scheduled.languages).containsExactly("en", "de").inOrder()
        assertThat(scheduled.participants).containsExactly(
            Participant(guid = Uuid.parse("dddddddd-dddd-dddd-dddd-dddddddddddd"), name = "Speaker")
        )
        assertThat(scheduled.links.map { it.type to it.service }).containsExactly(
            ReferenceType.ACTIVITY_PUB to ReferenceService.MASTODON,
            ReferenceType.MEDIA to ReferenceService.VOCTOWEB,
        ).inOrder()

        val planned = schedule.events[1] as PlannedEvent
        assertThat(planned.start).isNull()
        assertThat(planned.duration).isEqualTo(ScheduleDuration("PT1H"))
        assertThat(planned.state).isEqualTo(EventState.SUBMITTED)
        assertThat(planned.slug).isNull()
        assertThat(planned.track).isNull()
        assertThat(planned.room).isNull()
        assertThat(planned.languages).isNull()
        assertThat(planned.links).isEmpty()
        assertThat(planned.participants)
            .containsExactly(Participant(guid = null, name = "Submitter", role = ParticipantRole.HOST))
        assertThat(planned.location)
            .isEqualTo(Location(name = "Bar", address = "Somewhere 1, 20357 Hamburg, Germany"))

        val plannedWithStart = schedule.events[2] as PlannedEvent
        assertThat(plannedWithStart.start).isEqualTo(OffsetDateTime.parse("2025-01-01T14:00:00+01:00"))
        assertThat(plannedWithStart.duration).isNull()
        assertThat(plannedWithStart.state).isEqualTo(EventState.ACCEPTED)
        assertThat(plannedWithStart.room).isEqualTo(RoomReference(guid = Uuid.parse("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")))
        assertThat(plannedWithStart.languages).containsExactly("en")

        val person = schedule.persons.single()
        assertThat(person.links.single().type).isEqualTo(ReferenceType.AT_PROTO)
        assertThat(person.links.single().service).isEqualTo(ReferenceService.UNKNOWN)
    }

    @Test
    fun `parse maps unknown enum values to UNKNOWN`() {
        val json = loadJsonFile("schedule_v2_20261019_beta_1_unknown_enum_values.json")
        val scheduleV2 = ScheduleV2Parser.parse(json)

        assertThat(scheduleV2.schedule.state).isEqualTo(ScheduleState.UNKNOWN)
        assertThat(scheduleV2.schedule.conference.defaults?.theme).isEqualTo(Theme.UNKNOWN)
        assertThat(scheduleV2.schedule.conference.rooms.single().type).isEqualTo(RoomType.UNKNOWN)
        assertThat(scheduleV2.schedule.conference.rooms.single().features.recording.name).isEqualTo("UNKNOWN")
        val event = scheduleV2.schedule.events.single() as ScheduledEvent
        assertThat(event.type).isEqualTo(ScheduledEventType.UNKNOWN)
        assertThat(event.participants.single().role.name).isEqualTo("UNKNOWN")
        assertThat(event.links.single().type).isEqualTo(ReferenceType.UNKNOWN)
        assertThat(event.links.single().service).isEqualTo(ReferenceService.UNKNOWN)
    }

    @Test
    fun `parse fails when required event field is missing`() {
        val json = loadJsonFile("schedule_v2_20261019_beta_1_missing_required_event_field.json")
        assertThrows<SerializationException> {
            ScheduleV2Parser.parse(json)
        }
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(
        strings = [
            "schedule.events",
            "schedule.persons[0].guid",
            "schedule.persons[0].name",
            "schedule.conference.tracks[0].name",
            "schedule.conference.tracks[0].slug",
        ]
    )
    fun `parse fails when required field is missing`(path: String) {
        val json = loadJsonFile("schedule_v2_20261019_beta_1_features.json").withoutField(path)
        val exception = assertThrows<SerializationException> {
            ScheduleV2Parser.parse(json)
        }
        assertThat(exception).hasMessageThat().contains("'${path.substringAfterLast('.')}'")
    }

    @Test
    fun `parse defaults missing persons to an empty list`() {
        val json = loadJsonFile("schedule_v2_20261019_beta_1_features.json").withoutField("schedule.persons")
        val schedule = ScheduleV2Parser.parse(json).schedule
        assertThat(schedule.persons).isEmpty()
    }

    @Test
    fun `parse defaults missing schedule state to PUBLISHED`() {
        val json = loadJsonFile("schedule_v2_20261019_beta_1_features.json").withoutField("schedule.state")
        val schedule = ScheduleV2Parser.parse(json).schedule
        assertThat(schedule.state).isEqualTo(ScheduleState.PUBLISHED)
    }

    /**
     * Removes the field at the given dot separated [path] from this JSON document.
     * Path segments may index into arrays, e.g. `schedule.persons[0].name`.
     */
    private fun String.withoutField(path: String): String =
        ScheduleJson.create().parseToJsonElement(this).withoutField(path.split('.')).toString()

    private fun JsonElement.withoutField(segments: List<String>): JsonElement {
        val segment = segments.first()
        val key = segment.substringBefore('[')
        val fields = jsonObject
        if (segments.size == 1) {
            require(key in fields) { "Missing field '$key' in $fields" }
            return JsonObject(fields - key)
        }
        val child = fields.getValue(key)
        val index = segment.substringAfter('[', missingDelimiterValue = "").removeSuffix("]").toIntOrNull()
        val updated = when (index) {
            null -> child.withoutField(segments.drop(1))
            else -> JsonArray(child.jsonArray.mapIndexed { i, item ->
                if (i == index) item.withoutField(segments.drop(1)) else item
            })
        }
        return JsonObject(fields + (key to updated))
    }
}
