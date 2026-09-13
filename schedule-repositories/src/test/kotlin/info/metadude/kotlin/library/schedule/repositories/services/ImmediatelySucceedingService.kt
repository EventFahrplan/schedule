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
import info.metadude.kotlin.library.schedule.v2.models.Conference as ConferenceV2
import info.metadude.kotlin.library.schedule.v2.models.ConferenceDefaults as ConferenceDefaultsV2
import info.metadude.kotlin.library.schedule.v2.models.Day as DayV2
import info.metadude.kotlin.library.schedule.v2.models.EventParticipant as EventParticipantV2
import info.metadude.kotlin.library.schedule.v2.models.Generator as GeneratorV2
import info.metadude.kotlin.library.schedule.v2.models.LocalizableString as LocalizableStringV2
import info.metadude.kotlin.library.schedule.v2.models.Reference as ReferenceV2
import info.metadude.kotlin.library.schedule.v2.models.ReferenceType as ReferenceTypeV2
import info.metadude.kotlin.library.schedule.v2.models.Room as RoomV2
import info.metadude.kotlin.library.schedule.v2.models.RoomFeatures as RoomFeaturesV2
import info.metadude.kotlin.library.schedule.v2.models.RoomReference as RoomReferenceV2
import info.metadude.kotlin.library.schedule.v2.models.RoomType as RoomTypeV2
import info.metadude.kotlin.library.schedule.v2.models.Schedule as ScheduleV2Schedule
import info.metadude.kotlin.library.schedule.v2.models.ScheduleDuration as ScheduleDurationV2
import info.metadude.kotlin.library.schedule.v2.models.ScheduleV2 as ScheduleV2Model
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEvent as ScheduledEventV2
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventType as ScheduledEventTypeV2
import info.metadude.kotlin.library.schedule.v2.models.Theme as ThemeV2
import info.metadude.kotlin.library.schedule.v2.models.Track as TrackV2
import info.metadude.kotlin.library.schedule.v2.models.TrackReference as TrackReferenceV2

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

    override suspend fun getScheduleV2(
        path: String,
        eTag: String?,
        lastModifiedAt: String?,
    ): Response<ScheduleV2Model> = Response.success(
        ScheduleV2Model(
            schema = "https://c3voc.de/schema/schedule2",
            generator = GeneratorV2(
                name = "test-generator",
                version = "2.0.0",
                url = "https://example.com/generator",
            ),
            schedule = ScheduleV2Schedule(
                version = "test-v2",
                baseUrl = "https://example.com/",
                conference = ConferenceV2(
                    acronym = "test",
                    title = "Test Conference",
                    description = LocalizableStringV2.Text("Test description"),
                    start = OffsetDateTime.parse("2025-01-01T00:00:00+00:00"),
                    end = OffsetDateTime.parse("2025-01-02T00:00:00+00:00"),
                    timeslotDuration = ScheduleDurationV2("PT10M"),
                    timeZoneName = ZoneId.of("UTC"),
                    url = "https://example.com/",
                    defaults = ConferenceDefaultsV2(theme = ThemeV2.DARK, language = "en"),
                    tracks = listOf(
                        TrackV2(
                            guid = Uuid.parse("33333333-3333-3333-3333-333333333333"),
                            name = "Test",
                            slug = "test",
                        )
                    ),
                    rooms = listOf(
                        RoomV2(
                            name = "Room 1",
                            slug = "room-1",
                            type = RoomTypeV2.LECTURE_HALL,
                            guid = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                            features = RoomFeaturesV2(),
                        )
                    ),
                    days = listOf(
                        DayV2(
                            index = 1,
                            date = LocalDate.parse("2025-01-01"),
                            start = OffsetDateTime.parse("2025-01-01T09:00:00+00:00"),
                            end = OffsetDateTime.parse("2025-01-02T02:00:00+00:00"),
                        )
                    ),
                ),
                events = listOf(
                    ScheduledEventV2(
                        guid = Uuid.parse("44444444-4444-4444-4444-444444444444"),
                        code = "TESTV2",
                        start = OffsetDateTime.parse("2025-01-01T10:00:00+00:00"),
                        end = OffsetDateTime.parse("2025-01-01T10:30:00+00:00"),
                        duration = ScheduleDurationV2("PT30M"),
                        room = RoomReferenceV2(
                            guid = Uuid.parse("11111111-1111-1111-1111-111111111111"),
                            name = "Room 1",
                            slug = "room-1",
                        ),
                        slug = "test-v2-event",
                        url = "https://example.com/event",
                        title = "Test Event",
                        subtitle = null,
                        track = TrackReferenceV2("test"),
                        type = ScheduledEventTypeV2.TALK,
                        language = "en",
                        abstractText = LocalizableStringV2.Text("Abstract"),
                        description = LocalizableStringV2.Text("Description"),
                        participants = listOf(
                            EventParticipantV2(
                                guid = Uuid.parse("55555555-5555-5555-5555-555555555555"),
                                name = "Speaker",
                            )
                        ),
                        links = listOf(
                            ReferenceV2(
                                type = ReferenceTypeV2.WEB,
                                url = "https://example.com/link",
                            )
                        ),
                    )
                ),
            ),
        )
    )
}
