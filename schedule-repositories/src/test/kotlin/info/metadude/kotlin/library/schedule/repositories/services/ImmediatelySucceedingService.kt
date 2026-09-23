package info.metadude.kotlin.library.schedule.repositories.services

import info.metadude.kotlin.library.schedule.ScheduleService
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import retrofit2.Response
import kotlin.uuid.Uuid
import info.metadude.kotlin.library.schedule.v1.models.Assembly as AssemblyV1
import info.metadude.kotlin.library.schedule.v1.models.Conference as ConferenceV1
import info.metadude.kotlin.library.schedule.v1.models.ConferenceColors as ConferenceColorsV1
import info.metadude.kotlin.library.schedule.v1.models.ConferenceDate as ConferenceDateV1
import info.metadude.kotlin.library.schedule.v1.models.Day as DayV1
import info.metadude.kotlin.library.schedule.v1.models.Generator as GeneratorV1
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode as RecordingModeV1
import info.metadude.kotlin.library.schedule.v1.models.Room as RoomV1
import info.metadude.kotlin.library.schedule.v1.models.RoomDescription as RoomDescriptionV1
import info.metadude.kotlin.library.schedule.v1.models.RoomFeatures as RoomFeaturesV1
import info.metadude.kotlin.library.schedule.v1.models.RoomType as RoomTypeV1
import info.metadude.kotlin.library.schedule.v1.models.Schedule as ScheduleV1
import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1 as ScheduleV1Model
import info.metadude.kotlin.library.schedule.v1.models.Track as TrackV1

internal class ImmediatelySucceedingService : ScheduleService {

    override suspend fun getScheduleV1(
        path: String,
        eTag: String?,
        lastModifiedAt: String?,
    ): Response<ScheduleV1Model> = Response.success(
        ScheduleV1Model(
            schema = "https://c3voc.de/schedule/schema.json",
            generator = GeneratorV1(
                name = "test-generator",
                version = "1.0.0",
                url = "https://example.com/generator",
            ),
            schedule = ScheduleV1(
                version = "test",
                baseUrl = "https://example.com/",
                conference = ConferenceV1(
                    acronym = "test",
                    title = "Test Conference",
                    description = "Test description",
                    start = ConferenceDateV1.DateTime(OffsetDateTime.parse("2025-01-01T00:00:00+00:00")),
                    end = ConferenceDateV1.DateTime(OffsetDateTime.parse("2025-01-02T00:00:00+00:00")),
                    daysCount = 1,
                    timeslotDuration = Duration.ofMinutes(10),
                    timeZoneName = ZoneId.of("UTC"),
                    logo = "https://example.com/logo.png",
                    colors = ConferenceColorsV1(primary = "#ffffff", background = "#000000"),
                    keywords = listOf("test"),
                    url = "https://example.com/",
                    tracks = listOf(TrackV1("Test", "#000000", "test")),
                    rooms = listOf(
                        RoomV1(
                            name = "Room 1",
                            slug = "room-1",
                            guid = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                            type = RoomTypeV1.LECTURE_HALL,
                            description = RoomDescriptionV1.Text("Room 1"),
                            features = RoomFeaturesV1(RecordingModeV1.RECORD_BY_DEFAULT),
                            assembly = AssemblyV1(
                                name = "Test",
                                slug = "test",
                                guid = Uuid.parse("22222222-2222-2222-2222-222222222222"),
                            ),
                        )
                    ),
                    days = listOf(
                        DayV1(
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
