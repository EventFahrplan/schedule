package info.metadude.kotlin.library.schedule

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v1.models.ConferenceDate
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode
import info.metadude.kotlin.library.schedule.v1.models.ResourceType
import info.metadude.kotlin.library.schedule.v1.models.RoomDescription
import info.metadude.kotlin.library.schedule.v1.models.RoomType
import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import kotlin.uuid.Uuid

internal class ScheduleServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var service: ScheduleService

    @BeforeEach
    fun setup() {
        server = MockWebServer().apply { start() }
        service = createService()
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getScheduleV1 parses JSON with conference days rooms and events`() = runTest {
        val json = javaClass.classLoader!!
            .getResourceAsStream("schedule_fixture.json")!!
            .bufferedReader()
            .readText()

        server.enqueue(createResponse(json))
        val response = service.getScheduleV1("schedule.json")
        assertThat(response.isSuccessful).isTrue()
        assertScheduleV1(requireNotNull(response.body()) { "Missing body" })
    }

    private fun assertScheduleV1(scheduleV1: ScheduleV1) {
        assertThat(scheduleV1.schema).isEqualTo("https://c3voc.de/schedule/schema.json")
        assertThat(scheduleV1.generator?.name).isEqualTo("fixture-generator")
        assertThat(scheduleV1.generator?.version).isEqualTo("1.0.0")
        assertThat(scheduleV1.generator?.url).isEqualTo("https://example.com/generator")

        val schedule = scheduleV1.schedule
        assertThat(schedule.version).isNotEmpty()
        assertThat(schedule.baseUrl).isEqualTo("https://events.ccc.de/congress/2025/hub/")

        val conference = schedule.conference
        assertThat(conference.acronym).isEqualTo("39c3")
        assertThat(conference.title).isEqualTo("39th Chaos Communications Congress")
        assertThat(conference.description).isEqualTo("Congress fixture description")
        assertThat(conference.start).isEqualTo(
            ConferenceDate.DateTime(OffsetDateTime.parse("2025-12-27T10:00:00+00:00"))
        )
        assertThat(conference.end).isEqualTo(
            ConferenceDate.DateTime(OffsetDateTime.parse("2025-12-30T15:00:00+00:00"))
        )
        assertThat(conference.timeslotDuration).isEqualTo(Duration.ofMinutes(10))
        assertThat(conference.timeZoneName).isEqualTo(ZoneId.of("Europe/Berlin"))
        assertThat(conference.logo).isEqualTo("https://events.ccc.de/congress/2025/hub/logo.png")
        assertThat(conference.colors?.primary).isEqualTo("#ffffff")
        assertThat(conference.colors?.background).isEqualTo("#000000")
        assertThat(conference.keywords).containsExactly("chaos", "congress")
        assertThat(conference.tracks).hasSize(1)
        assertThat(conference.tracks[0].name).isEqualTo("CCC & Community")
        assertThat(conference.rooms).hasSize(2)

        val oneRoom = conference.rooms[0]
        assertThat(oneRoom.name).isEqualTo("One")
        assertThat(oneRoom.guid).isEqualTo(Uuid.parse("ba692ba3-421b-5371-8309-60acc34a3c05"))
        assertThat(oneRoom.type).isEqualTo(RoomType.LECTURE_HALL)
        assertThat(oneRoom.description).isEqualTo(RoomDescription.Text("Main lecture hall"))
        assertThat(oneRoom.capacity).isEqualTo(3025.0)
        assertThat(oneRoom.url).isEqualTo("https://events.ccc.de/congress/2025/hub/room/one")
        assertThat(oneRoom.features.recording).isEqualTo(RecordingMode.RECORD_BY_DEFAULT)
        assertThat(oneRoom.assembly?.guid).isEqualTo(Uuid.parse("2465345f-f0e4-4a72-96d8-346d703a59e3"))
        assertThat(oneRoom.assembly?.url).isEqualTo("https://events.ccc.de/congress/2025/hub/assembly/ccc")

        val groundRoom = conference.rooms[1]
        assertThat(groundRoom.name).isEqualTo("Ground")
        assertThat(groundRoom.guid).isEqualTo(Uuid.parse("33333333-3333-3333-3333-333333333333"))
        assertThat(groundRoom.type).isEqualTo(RoomType.STAGE)
        assertThat(groundRoom.description).isEqualTo(
            RoomDescription.Translations(
                mapOf(
                    "en" to "Ground stage",
                    "de" to "Bodenbuehne",
                )
            )
        )
        assertThat(groundRoom.capacity).isEqualTo(750.5)
        assertThat(groundRoom.features.recording).isEqualTo(RecordingMode.RECORDING_NOT_POSSIBLE)
        assertThat(conference.days).hasSize(1)

        val day = conference.days[0]
        assertThat(day.index).isEqualTo(1)
        assertThat(day.date).isEqualTo(LocalDate.parse("2025-12-27"))
        assertThat(day.dayStart).isEqualTo(OffsetDateTime.parse("2025-12-27T11:00:00+01:00"))
        assertThat(day.dayEnd).isEqualTo(OffsetDateTime.parse("2025-12-28T06:00:00+01:00"))
        assertThat(day.rooms).containsKey("One")
        assertThat(day.rooms).containsKey("Ground")

        val oneEvents = day.rooms["One"]!!
        assertThat(oneEvents).isNotEmpty()

        val firstEvent = oneEvents[0]
        assertThat(firstEvent.guid).isEqualTo(Uuid.parse("0c8b0cb4-6cf9-5ff8-928a-0a0f49558c48"))
        assertThat(firstEvent.date).isEqualTo(OffsetDateTime.parse("2025-12-27T10:30:00+01:00"))
        assertThat(firstEvent.start).isEqualTo(LocalTime.of(10, 30))
        assertThat(firstEvent.duration).isEqualTo(Duration.ofMinutes(30))
        assertThat(firstEvent.title).isEqualTo("Opening Ceremony")
        assertThat(firstEvent.abstractText).startsWith("Power On!")
        assertThat(firstEvent.recordingLicense).isEqualTo("CC-BY-4.0")
        assertThat(firstEvent.originUrl).isEqualTo("https://cfp.cccv.de/39c3/talk/POWER1/")
        assertThat(firstEvent.feedbackUrl).isEqualTo("https://cfp.cccv.de/39c3/talk/POWER1/feedback/")
        assertThat(firstEvent.doNotRecord).isFalse()
        assertThat(firstEvent.doNotStream).isNull()
        assertThat(firstEvent.persons).hasSize(2)
        assertThat(firstEvent.persons[0].guid).isEqualTo(Uuid.parse("4f09b480-eebf-54b5-b703-781907c5e048"))
        assertThat(firstEvent.persons[0].id).isEqualTo(1)
        assertThat(firstEvent.persons[0].publicName).isEqualTo("pajowu")
        assertThat(firstEvent.persons[0].links.single().type).isEqualTo(ResourceType.WEB)
        assertThat(firstEvent.persons[1].code).isEqualTo("STELLA1")
        assertThat(firstEvent.links).isEmpty()
        assertThat(firstEvent.attachments).isEmpty()

        val groundEvents = day.rooms["Ground"]!!
        assertThat(groundEvents).hasSize(1)

        val secondEvent = groundEvents[0]
        assertThat(secondEvent.guid).isEqualTo(Uuid.parse("7c12c5be-5414-5673-a856-697a3889f824"))
        assertThat(secondEvent.date).isEqualTo(OffsetDateTime.parse("2025-12-27T11:00:00+01:00"))
        assertThat(secondEvent.start).isEqualTo(LocalTime.of(11, 0))
        assertThat(secondEvent.duration).isEqualTo(Duration.ofMinutes(40))
        assertThat(secondEvent.logo).isNotNull()
        assertThat(secondEvent.links).hasSize(1)
        assertThat(secondEvent.links[0].type).isEqualTo(ResourceType.RELATED)
        assertThat(secondEvent.links[0].title).isEqualTo("Presentation slides")
        assertThat(secondEvent.attachments).hasSize(1)
        assertThat(secondEvent.attachments[0].type).isEqualTo(ResourceType.SLIDES)
    }

    @Test
    fun `getSchedule parses conference start and end as date-only values`() = runTest {
        val json = """
            {
              "schedule": {
                "version": "test",
                "conference": {
                  "acronym": "demo",
                  "title": "Demo",
                  "start": "2025-01-01",
                  "end": "2025-01-02",
                  "daysCount": 1,
                  "timeslot_duration": "00:10",
                  "days": [
                    {
                      "index": 1,
                      "date": "2025-01-01",
                      "day_start": "2025-01-01T09:00:00+00:00",
                      "day_end": "2025-01-01T18:00:00+00:00",
                      "rooms": {}
                    }
                  ]
                }
              }
            }
        """.trimIndent()

        server.enqueue(createResponse(json))
        val response = service.getScheduleV1("")
        assertThat(response.isSuccessful).isTrue()
        val body = requireNotNull(response.body()) { "Missing body" }
        assertThat(body.schedule.conference.start).isEqualTo(
            ConferenceDate.Date(LocalDate.parse("2025-01-01"))
        )
        assertThat(body.schedule.conference.end).isEqualTo(
            ConferenceDate.Date(LocalDate.parse("2025-01-02"))
        )
    }

    @Test
    fun `getSchedule maps unknown enum values to UNKNOWN`() = runTest {
        val json = """
            {
              "schedule": {
                "version": "test",
                "conference": {
                  "acronym": "demo",
                  "title": "Demo",
                  "start": "2025-01-01T00:00:00+00:00",
                  "end": "2025-01-02T00:00:00+00:00",
                  "daysCount": 1,
                  "timeslot_duration": "00:10",
                  "rooms": [
                    {
                      "name": "Mystery",
                      "guid": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
                      "type": "spaceship",
                      "features": {
                        "recording": "recording_magic"
                      }
                    }
                  ],
                  "days": [
                    {
                      "index": 1,
                      "date": "2025-01-01",
                      "day_start": "2025-01-01T09:00:00+00:00",
                      "day_end": "2025-01-01T18:00:00+00:00",
                      "rooms": {
                        "Mystery": [
                          {
                            "guid": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
                            "id": 1,
                            "date": "2025-01-01T10:00:00+00:00",
                            "start": "10:00",
                            "duration": "00:30",
                            "room": "Mystery",
                            "slug": "mystery",
                            "title": "Mystery",
                            "subtitle": null,
                            "track": null,
                            "type": "other",
                            "language": null,
                            "abstract": null,
                            "persons": [],
                            "links": [
                              {
                                "url": "https://example.com/ref",
                                "type": "mystery"
                              }
                            ],
                            "url": "https://example.com/event"
                          }
                        ]
                      }
                    }
                  ]
                }
              }
            }
        """.trimIndent()

        server.enqueue(createResponse(json))
        val response = service.getScheduleV1("")
        assertThat(response.isSuccessful).isTrue()
        val conference = requireNotNull(response.body()) { "Missing body" }.schedule.conference
        assertThat(conference.rooms.single().type).isEqualTo(RoomType.UNKNOWN)
        assertThat(conference.rooms.single().features.recording).isEqualTo(RecordingMode.UNKNOWN)
        val event = conference.days.single().rooms.getValue("Mystery").single()
        assertThat(event.links.single().type).isEqualTo(ResourceType.UNKNOWN)
    }

    @Test
    fun `getSchedule skips events missing mandatory fields`() = runTest {
        val json = """
            {
              "schedule": {
                "version": "test",
                "conference": {
                  "acronym": "demo",
                  "title": "Demo",
                  "start": "2025-01-01T00:00:00+00:00",
                  "end": "2025-01-02T00:00:00+00:00",
                  "daysCount": 1,
                  "timeslot_duration": "00:10",
                  "days": [
                    {
                      "index": 1,
                      "date": "2025-01-01",
                      "day_start": "2025-01-01T09:00:00+00:00",
                      "day_end": "2025-01-01T18:00:00+00:00",
                      "rooms": {
                        "One": [
                          {
                            "id": 1,
                            "date": "2025-01-01T10:00:00+00:00",
                            "start": "10:00",
                            "duration": "00:30",
                            "room": "One",
                            "slug": "missing-guid",
                            "title": "Missing guid",
                            "type": "talk",
                            "url": "https://example.com/missing-guid"
                          },
                          {
                            "guid": "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb",
                            "id": 2,
                            "date": "2025-01-01T11:00:00+00:00",
                            "start": "11:00",
                            "duration": "00:30",
                            "room": "One",
                            "slug": "valid",
                            "title": "Valid",
                            "type": "talk",
                            "url": "https://example.com/valid"
                          }
                        ]
                      }
                    }
                  ]
                }
              }
            }
        """.trimIndent()

        server.enqueue(createResponse(json))
        val response = service.getScheduleV1("")
        assertThat(response.isSuccessful).isTrue()
        val events = requireNotNull(response.body()) { "Missing body" }
            .schedule.conference.days.single().rooms.getValue("One")
        assertThat(events).hasSize(1)
        assertThat(events.single().guid).isEqualTo(Uuid.parse("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
    }

    @Test
    fun `getSchedule fails when JSON is malformed`() = runTest {
        server.enqueue(createResponse("not json"))
        assertThrows<Exception> {
            service.getScheduleV1("")
        }
    }

    private fun createResponse(json: String) = MockResponse()
        .setBody(json)
        .addHeader("Content-Type", "application/json; charset=UTF-8")

    private fun createService(): ScheduleService = Api
        .createRetrofit(
            baseUrl = server.url("/").toString(),
            callFactory = OkHttpClient.Builder().build()
        )
        .create(ScheduleService::class.java)
}
