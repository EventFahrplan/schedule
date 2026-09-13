package info.metadude.kotlin.library.schedule

import com.google.common.truth.Subject
import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.Conference
import info.metadude.kotlin.library.schedule.v2.models.ConferenceColors
import info.metadude.kotlin.library.schedule.v2.models.ConferenceDefaults
import info.metadude.kotlin.library.schedule.v2.models.Day
import info.metadude.kotlin.library.schedule.v2.models.Event
import info.metadude.kotlin.library.schedule.v2.models.EventParticipant
import info.metadude.kotlin.library.schedule.v2.models.Generator
import info.metadude.kotlin.library.schedule.v2.models.Image
import info.metadude.kotlin.library.schedule.v2.models.LocalizableString
import info.metadude.kotlin.library.schedule.v2.models.LocalizableString.Text
import info.metadude.kotlin.library.schedule.v2.models.LocalizableString.Translations
import info.metadude.kotlin.library.schedule.v2.models.Location
import info.metadude.kotlin.library.schedule.v2.models.MetaEvent
import info.metadude.kotlin.library.schedule.v2.models.MetaEventType
import info.metadude.kotlin.library.schedule.v2.models.ParticipantRole
import info.metadude.kotlin.library.schedule.v2.models.Person
import info.metadude.kotlin.library.schedule.v2.models.RecordingMode
import info.metadude.kotlin.library.schedule.v2.models.Reference
import info.metadude.kotlin.library.schedule.v2.models.ReferenceService
import info.metadude.kotlin.library.schedule.v2.models.ReferenceType
import info.metadude.kotlin.library.schedule.v2.models.Room
import info.metadude.kotlin.library.schedule.v2.models.RoomFeatures
import info.metadude.kotlin.library.schedule.v2.models.RoomParent
import info.metadude.kotlin.library.schedule.v2.models.RoomReference
import info.metadude.kotlin.library.schedule.v2.models.RoomType
import info.metadude.kotlin.library.schedule.v2.models.Schedule
import info.metadude.kotlin.library.schedule.v2.models.ScheduleDuration
import info.metadude.kotlin.library.schedule.v2.models.ScheduleV2
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEvent
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventState
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventType
import info.metadude.kotlin.library.schedule.v2.models.Theme
import info.metadude.kotlin.library.schedule.v2.models.Track
import info.metadude.kotlin.library.schedule.v2.models.TrackColors
import info.metadude.kotlin.library.schedule.v2.models.TrackReference
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import retrofit2.Response
import kotlin.uuid.Uuid

internal class ProductionApiV2Test {

    companion object {
        // https://gist.github.com/johnjohndoe/c84a5bd30d5be2777cb583ee5769959c
        const val BASE_URL =
            "https://gist.githubusercontent.com/johnjohndoe/c84a5bd30d5be2777cb583ee5769959c/raw/169d1f47a19901d0d079ff00f0b2e34d78edd329/"
        const val SCHEDULE_PATH = "39c3-schedule2.json"
    }

    @Test
    fun `getScheduleV2 responds successfully from production API`() = runTest {
        assertSchedule(
            scheduleService = serviceV2,
            onGetSchedule = { getScheduleV2(SCHEDULE_PATH) },
            onAssertSchedule = { assertWrapper(it) },
        )
    }

    private fun <T> assertSchedule(
        scheduleService: ScheduleService,
        onGetSchedule: suspend ScheduleService.() -> Response<T>,
        onAssertSchedule: (T) -> Unit,
    ) = runTest {
        try {
            val response = scheduleService.onGetSchedule()
            when (response.isSuccessful) {
                true -> {
                    val schedule = response.body()
                    assertThat(schedule).isNotNull()
                    schedule?.let(onAssertSchedule)
                }

                false -> {
                    fail("Request failed with code ${response.code()}: ${response.message()}")
                }
            }
        } catch (t: Throwable) {
            fail("Should not throw", t)
        }
    }

    private fun assertWrapper(wrapper: ScheduleV2) {
        wrapper.generator?.let {
            assertGenerator(it)
        }
        assertThat(wrapper.schedule).isNotNull()
        assertThat(wrapper.schema).isNotNull()

        assertSchedule(wrapper.schedule)
        assertConference(wrapper.schedule.conference)
        assertDays(wrapper.schedule.conference.days)
        assertEvents(wrapper.schedule.events)
        assertPersons(wrapper.schedule.persons)
        assertRooms(wrapper.schedule.conference.rooms)
        assertTracks(wrapper.schedule.conference.tracks)
    }

    private fun assertGenerator(generator: Generator) {
        assertThat(generator.name).isNotEmpty()
        assertThat(generator.url).isNotEmpty()
        assertThat(generator.version).isNotEmpty()
    }

    private fun assertSchedule(schedule: Schedule) {
        assertThat(schedule.baseUrl).isNotNull()
        assertThat(schedule.conference).isNotNull()
        assertThat(schedule.events).isNotNull()
        assertThat(schedule.persons).isNotNull()
        assertThat(schedule.version).isNotNull()
    }

    private fun assertConference(conference: Conference) {
        assertThat(conference.acronym).isNotEmpty()
        conference.colors?.let {
            assertConferenceColors(it)
        }
        assertThat(conference.days).isNotNull()
        conference.defaults?.let {
            assertConferenceDefaults(it)
        }
        conference.description?.let {
            assertLocalizableString(it)
        }
        assertThat(conference.end).isNotNull()
        assertThat(conference.end).isOffsetDateTime()
        assertThat(conference.keywords).isNotNull()
        assertThat(conference.languages).isNotNull()
        conference.logo?.let {
            assertImage(it)
        }
        assertThat(conference.meta).isNull()
        assertThat(conference.rooms).isNotNull()
        assertThat(conference.start).isNotNull()
        assertThat(conference.start).isOffsetDateTime()
        conference.timeslotDuration?.let {
            assertScheduleDuration(it)
        }
        assertThat(conference.timeZoneName).isNotNull()
        assertThat(conference.timeZoneName).isZoneId()
        assertThat(conference.title).isNotEmpty()
        assertThat(conference.tracks).isNotNull()
        assertThat(conference.url).isNotNull()
    }

    private fun assertConferenceColors(conferenceColors: ConferenceColors) {
        conferenceColors.background?.let {
            assertThat(it).isNotEmpty()
        }
        conferenceColors.primary?.let {
            assertThat(it).isNotEmpty()
        }
    }

    private fun assertConferenceDefaults(conferenceDefaults: ConferenceDefaults) {
        conferenceDefaults.language?.let {
            assertThat(it).isNotEmpty()
        }
        assertThat(conferenceDefaults.theme).isNotNull()
        assertThat(conferenceDefaults.theme).isTheme()
    }

    private fun assertScheduleDuration(duration: ScheduleDuration) {
        assertThat(duration.value).isNotEmpty()
    }

    private fun assertDays(days: List<Day>) {
        days.forEachIndexed { index, day -> assertDay(day, index + 1) }
    }

    private fun assertDay(day: Day, index: Int) {
        assertThat(day.date).isLocalDate()
        assertThat(day.end).isOffsetDateTime()
        assertThat(day.index).isEqualTo(index)
        assertThat(day.start).isOffsetDateTime()
    }

    private fun assertRooms(rooms: List<Room>) {
        rooms.forEach { assertRoom(it) }
    }

    private fun assertRoom(room: Room) {
        if (room.capacity != null) {
            assertThat(room.capacity).isAtLeast(0)
        }
        room.description?.let {
            assertLocalizableString(it)
        }
        assertThat(room.features).isNotNull()
        assertRoomFeatures(room.features)
        assertThat(room.guid).isNotNull()
        assertThat(room.guid).isUuid()
        assertThat(room.name).isNotEmpty()
        assertThat(room.parent).isNotNull()
        room.parent?.let {
            assertRoomParent(it)
        }
        assertThat(room.slug).isNotNull()
        assertThat(room.streamId).isNull()
        assertThat(room.type).isNotNull()
        assertThat(room.type).isRoomType()
        assertThat(room.url).isNull()
    }

    private fun assertRoomFeatures(roomFeatures: RoomFeatures) {
        assertThat(roomFeatures.recording).isNotNull()
        assertThat(roomFeatures.recording).isRecordingMode()
    }

    private fun assertLocalizableString(string: LocalizableString) {
        when (string) {
            is Text -> assertThat(string.value).isNotNull()
            is Translations -> string.values.forEach { translation ->
                assertThat(translation.value).isNotEmpty()
            }
        }
    }

    private fun assertRoomParent(parent: RoomParent) {
        assertThat(parent.guid).isNotNull()
        assertThat(parent.guid).isUuid()
        assertThat(parent.name).isNotEmpty()
        assertThat(parent.slug).isNotEmpty()
        assertThat(parent.type).isNotEmpty()
        assertThat(parent.url).isNull()
    }

    private fun assertTracks(tracks: List<Track>) {
        tracks.forEach { assertTrack(it) }
    }

    private fun assertTrack(track: Track) {
        assertThat(track).isNotNull()
        assertThat(track.color).isNotNull()
        track.colors?.let {
            assertTrackColors(it)
        }
        assertThat(track.guid).isNull()
        assertThat(track.name).isNotEmpty()
        assertThat(track.slug).isNotEmpty()
        assertThat(track.type).isNull()
    }

    private fun assertTrackColors(trackColors: TrackColors) {
        assertThat(trackColors.dark).isNotEmpty()
        assertThat(trackColors.light).isNotEmpty()
    }

    private fun assertEvents(events: List<Event>) {
        events.forEach { assertEvent(it) }
    }

    private fun assertEvent(event: Event) {
        when (event) {
            is MetaEvent -> assertMetaEvent(event)
            is ScheduledEvent -> assertScheduledEvent(event)
        }
    }

    private fun assertMetaEvent(event: MetaEvent) {
        assertThat(event.end).isNotNull()
        assertThat(event.end).isOffsetDateTime()
        assertThat(event.guid).isNotNull()
        assertThat(event.guid).isUuid()
        assertThat(event.room).isNotNull()
        event.room?.let {
            assertRoomReference(it)
        }
        assertThat(event.start).isNotNull()
        assertThat(event.start).isOffsetDateTime()
        assertThat(event.subtitle).isNotEmpty()
        assertThat(event.title).isNotEmpty()
        assertThat(event.type).isNotNull()
        assertThat(event.type).isMetaEventType()
    }

    fun assertScheduledEvent(event: ScheduledEvent) {
        assertThat(event.abstractText).isNotNull()
        event.abstractText?.let {
            assertLocalizableString(it)
        }
        assertThat(event.attachments).isNotNull()
        assertReferences(event.attachments)
        assertThat(event.code).isNull()
        event.description?.let {
            assertLocalizableString(it)
        }
        event.duration?.let {
            assertScheduleDuration(it)
        }
        assertThat(event.end).isNotNull()
        assertThat(event.end).isOffsetDateTime()
        assertThat(event.guid).isNotNull()
        assertThat(event.guid).isUuid()
        event.image?.let {
            assertImage(it)
        }
        event.language?.let {
            assertThat(it).isNotEmpty()
        }
        assertThat(event.links).isNotNull()
        assertReferences(event.links)
        event.location?.let {
            assertLocation(it)
        }
        event.logo?.let {
            assertImage(it)
        }
        assertThat(event.participants).isNotNull()
        assertEventParticipants(event.participants)
        event.room?.let {
            assertRoomReference(it)
        }
        assertThat(event.slug).isNotEmpty()
        assertThat(event.start).isNotNull()
        assertThat(event.start).isOffsetDateTime()
        assertThat(event.state).isNotNull()
        assertThat(event.state).isScheduledEventState()
        event.subtitle?.let {
            assertThat(it).isNotEmpty()
        }
        assertThat(event.title).isNotEmpty()
        event.track?.let {
            assertTrackReference(it)
        }
        assertThat(event.type).isNotNull()
        assertThat(event.type).isScheduleEventType()
        assertThat(event.url).isNotEmpty()
    }

    private fun assertLocation(location: Location) {
        assertThat(location.name).isNotEmpty()
        location.type?.let {
            assertThat(it).isNotEmpty()
        }
    }

    private fun assertReferences(references: List<Reference>) {
        assertThat(references).isNotNull()
        references.forEach {
            assertThat(it).isNotNull()
            assertReference(it)
        }
    }

    private fun assertReference(reference: Reference) {
        assertThat(reference.service).isNotNull()
        assertThat(reference.service).isReferenceService()
        assertThat(reference.title).isNotEmpty()
        assertThat(reference.type).isNotNull()
        assertThat(reference.type).isReferenceType()
        assertThat(reference.url).isNotEmpty()
    }

    private fun assertEventParticipants(participants: List<EventParticipant>) {
        participants.forEach {
            assertThat(it).isNotNull()
            assertEventParticipant(it)
        }
    }

    private fun assertEventParticipant(participant: EventParticipant) {
        assertThat(participant.guid).isNotNull()
        assertThat(participant.guid).isUuid()
        assertThat(participant.name).isNotEmpty()
        assertThat(participant.role).isNotNull()
        assertThat(participant.role).isParticipantRole()
    }

    private fun assertTrackReference(trackReference: TrackReference) {
        assertThat(trackReference.slug).isNotEmpty()
    }

    private fun assertRoomReference(roomReference: RoomReference) {
        assertThat(roomReference.guid).isNotNull()
        assertThat(roomReference.guid).isUuid()
        assertThat(roomReference.name).isNotEmpty()
        assertThat(roomReference.slug).isNotEmpty()
    }

    private fun assertPersons(persons: List<Person>) {
        persons.forEach {
            assertThat(it).isNotNull()
            assertPerson(it)
        }
    }

    private fun assertPerson(person: Person) {
        person.avatar?.let {
            assertImage(it)
        }
        person.biography?.let {
            assertLocalizableString(it)
        }
        assertThat(person.guid).isNotNull()
        assertThat(person.guid).isUuid()
        assertThat(person.links).isNotNull()
        assertReferences(person.links)
        assertThat(person.name).isNotEmpty()
        assertThat(person.url).isNotEmpty()
    }

    private fun assertImage(image: Image) {
        image.type?.let {
            assertThat(it).isNotEmpty()
        }
        assertThat(image.url).isNotNull()
        assertThat(image.url).isNotEmpty()
    }

    private val httpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val serviceV2: ScheduleService by lazy {
        Api.provideScheduleService(BASE_URL, httpClient)
    }
}

private fun Subject.isLocalDate() = isInstanceOf(LocalDate::class.java)
private fun Subject.isMetaEventType() = isInstanceOf(MetaEventType::class.java)
private fun Subject.isOffsetDateTime() = isInstanceOf(OffsetDateTime::class.java)
private fun Subject.isParticipantRole() = isInstanceOf(ParticipantRole::class.java)
private fun Subject.isReferenceService() = isInstanceOf(ReferenceService::class.java)
private fun Subject.isReferenceType() = isInstanceOf(ReferenceType::class.java)
private fun Subject.isRecordingMode() = isInstanceOf(RecordingMode::class.java)
private fun Subject.isScheduleEventType() = isInstanceOf(ScheduledEventType::class.java)
private fun Subject.isRoomType() = isInstanceOf(RoomType::class.java)
private fun Subject.isScheduledEventState() = isInstanceOf(ScheduledEventState::class.java)
private fun Subject.isTheme() = isInstanceOf(Theme::class.java)
private fun Subject.isUuid() = isInstanceOf(Uuid::class.java)
private fun Subject.isZoneId() = isInstanceOf(ZoneId::class.java)
