package info.metadude.kotlin.library.schedule

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.utils.loadJsonFile
import info.metadude.kotlin.library.schedule.v2.models.LocalizableString
import info.metadude.kotlin.library.schedule.v2.models.ReferenceService
import info.metadude.kotlin.library.schedule.v2.models.ReferenceType
import info.metadude.kotlin.library.schedule.v2.models.RoomType
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEvent
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventState
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventType
import info.metadude.kotlin.library.schedule.v2.models.Theme
import kotlinx.serialization.SerializationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import kotlin.uuid.Uuid

internal class ScheduleV2ParserTest {

    @Test
    fun `parse reads schedule fixture with scheduled and meta events`() {
        val json = loadJsonFile("schedule_v2_complete.json")
        val scheduleV2 = ScheduleV2Parser.parse(json)

        assertThat(scheduleV2.schema).isEqualTo("https://c3voc.de/schedule/schema2.json")

        val conference = scheduleV2.schedule.conference
        assertThat(conference.acronym).isEqualTo("39c3")
        assertThat(conference.start).isEqualTo(OffsetDateTime.parse("2025-12-27T10:00:00+00:00"))
        assertThat(conference.end).isEqualTo(OffsetDateTime.parse("2025-12-30T15:00:00+00:00"))
        assertThat(conference.timeZoneName).isEqualTo(ZoneId.of("Europe/Berlin"))
        assertThat(conference.rooms.first().type).isEqualTo(RoomType.LECTURE_HALL)
        assertThat(conference.days.first().date).isEqualTo(LocalDate.parse("2025-12-27"))
        assertThat(conference.description).isNull()

        val scheduledEvent = scheduleV2.schedule.events.first() as ScheduledEvent
        assertThat(scheduledEvent.guid).isEqualTo(Uuid.parse("0c8b0cb4-6cf9-5ff8-928a-0a0f49558c48"))
        assertThat(scheduledEvent.type).isEqualTo(ScheduledEventType.TALK)
        assertThat(scheduledEvent.state).isEqualTo(ScheduledEventState.CONFIRMED)
        assertThat(scheduledEvent.abstractText).isInstanceOf(LocalizableString.Text::class.java)
        assertThat(scheduledEvent.description)
            .isEqualTo(
                LocalizableString.Translations(
                    mapOf(
                        "en" to "Das Opening gibt euch die wichtigsten Infos für den Congress, stimmt euch ein und ... äh ... bis Späti!\n",
                        "de" to "Das Opening gibt euch die wichtigsten Infos für den Congress, stimmt euch ein und ... äh ... bis Späti!\n",
                    )
                )
            )
        assertThat(scheduledEvent.participants.first().role.name).isEqualTo("SPEAKER")
        assertThat(scheduledEvent.links).isEmpty()
        assertThat(scheduledEvent.attachments).isEmpty()
    }

    @Test
    fun `parse reads September 2026 fixture with nullable fields and event locations`() {
        val json = loadJsonFile("schedule_v2_complete.json")
        val schedule = ScheduleV2Parser.parse(json).schedule

        assertThat(schedule.conference.acronym).isEqualTo("39c3")
        assertThat(schedule.conference.days).hasSize(4)
        assertThat(schedule.conference.rooms).hasSize(76)
        assertThat(schedule.conference.rooms.count { it.type == RoomType.LECTURE_HALL }).isEqualTo(7)
        assertThat(schedule.events).hasSize(1347)

        val events = schedule.events.map { it as ScheduledEvent }
        assertThat(events.count { it.room == null }).isEqualTo(337)
        assertThat(events.count { it.track == null }).isEqualTo(1111)
        assertThat(events.count { it.language == null }).isEqualTo(83)
        assertThat(events.count { it.type == ScheduledEventType.SELF_ORGANIZED }).isEqualTo(398)
        assertThat(events.count { it.image != null }).isEqualTo(303)
        assertThat(events.first { it.image != null }.image!!.url)
            .isEqualTo("/media/e/e74a44b5bf66/e/e74a44b5bf66/flyerservice_Hahn_Feuerwehr_B8CjkR9_2dA_EDO16Ys.webp")
        assertThat(events.count { it.location != null }).isEqualTo(529)
        val location = events.first { it.location != null }.location!!
        assertThat(location.name).isEqualTo("foo")
        assertThat(location.type).isEqualTo("Place")
    }

    @Test
    fun `parse maps unknown enum values to UNKNOWN`() {
        val json = loadJsonFile("schedule_v2_unknown_enum_values.json")
        val scheduleV2 = ScheduleV2Parser.parse(json)

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
        val json = loadJsonFile("schedule_v2_missing_required_event_field.json")
        assertThrows<SerializationException> {
            ScheduleV2Parser.parse(json)
        }
    }
}
