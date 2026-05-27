package info.metadude.kotlin.library.schedule.repositories.services

import info.metadude.kotlin.library.schedule.ScheduleService
import info.metadude.kotlin.library.schedule.v1.models.Assembly
import info.metadude.kotlin.library.schedule.v1.models.Conference
import info.metadude.kotlin.library.schedule.v1.models.ConferenceColors
import info.metadude.kotlin.library.schedule.v1.models.ConferenceDate
import info.metadude.kotlin.library.schedule.v1.models.Day
import info.metadude.kotlin.library.schedule.v1.models.Generator
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode
import info.metadude.kotlin.library.schedule.v1.models.Room
import info.metadude.kotlin.library.schedule.v1.models.RoomDescription
import info.metadude.kotlin.library.schedule.v1.models.RoomFeatures
import info.metadude.kotlin.library.schedule.v1.models.RoomType
import info.metadude.kotlin.library.schedule.v1.models.Schedule
import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1
import info.metadude.kotlin.library.schedule.v1.models.Track
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import retrofit2.Response
import kotlin.uuid.Uuid

internal class ImmediatelySucceedingService : ScheduleService {

    override suspend fun getScheduleV1(
        path: String,
        eTag: String?,
        lastModifiedAt: String?,
    ): Response<ScheduleV1> = Response.success(
        ScheduleV1(
            schema = "https://c3voc.de/schedule/schema.json",
            generator = Generator(
                name = "test-generator",
                version = "1.0.0",
                url = "https://example.com/generator",
            ),
            schedule = Schedule(
                version = "test",
                baseUrl = "https://example.com/",
                conference = Conference(
                    acronym = "test",
                    title = "Test Conference",
                    description = "Test description",
                    start = ConferenceDate.DateTime(OffsetDateTime.parse("2025-01-01T00:00:00+00:00")),
                    end = ConferenceDate.DateTime(OffsetDateTime.parse("2025-01-02T00:00:00+00:00")),
                    daysCount = 1,
                    timeslotDuration = Duration.ofMinutes(10),
                    timeZoneName = ZoneId.of("UTC"),
                    logo = "https://example.com/logo.png",
                    colors = ConferenceColors(primary = "#ffffff", background = "#000000"),
                    keywords = listOf("test"),
                    url = "https://example.com/",
                    tracks = listOf(Track("Test", "#000000", "test")),
                    rooms = listOf(
                        Room(
                            name = "Room 1",
                            slug = "room-1",
                            guid = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                            type = RoomType.LECTURE_HALL,
                            description = RoomDescription.Text("Room 1"),
                            features = RoomFeatures(RecordingMode.RECORD_BY_DEFAULT),
                            assembly = Assembly(
                                name = "Test",
                                slug = "test",
                                guid = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                            ),
                        )
                    ),
                    days = listOf(
                        Day(
                            index = 1,
                            date = LocalDate.parse("2025-01-01"),
                            dayStart = OffsetDateTime.parse("2025-01-01T09:00:00+00:00"),
                            dayEnd = OffsetDateTime.parse("2025-01-02T02:00:00+00:00"),
                            rooms = emptyMap(),
                        )
                    ),
                ),
            )
        )
    )
}
