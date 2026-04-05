package info.metadude.kotlin.library.schedule

import com.google.common.truth.Subject
import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v1.models.Conference
import info.metadude.kotlin.library.schedule.v1.models.Day
import info.metadude.kotlin.library.schedule.v1.models.Event
import info.metadude.kotlin.library.schedule.v1.models.Room
import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import kotlin.uuid.Uuid

internal class ProductionApiTest {

    companion object {
        const val BASE_URL = "https://fahrplan.events.ccc.de/congress/2025/fahrplan/schedules/"
        const val SCHEDULE_PATH = "schedule.json"
    }

    @Test
    fun `getScheduleV1 responds successfully from production API`() = runTest {
        try {
            val response = service.getScheduleV1("", "", SCHEDULE_PATH)
            when (response.isSuccessful) {
                true -> {
                    val schedule = response.body()
                    assertThat(schedule).isNotNull()
                    schedule?.let { assertSchedule(it) }
                }

                false -> {
                    fail("Request failed with code ${response.code()}: ${response.message()}")
                }
            }
        } catch (t: Throwable) {
            fail("Should not throw $t")
        }
    }

    private fun assertSchedule(wrapper: ScheduleV1) {
        assertThat(wrapper.schema).isNotEmpty()
        assertThat(wrapper.schedule).isNotNull()
        val schedule = wrapper.schedule
        assertThat(schedule.version).isNotEmpty()
        assertScheduleConference(schedule.conference)
    }

    private fun assertScheduleConference(conference: Conference) {
        assertThat(conference.acronym).isNotEmpty()
        assertThat(conference.title).isNotEmpty()
        assertThat(conference.daysCount).isAtLeast(0)
        assertThat(conference.days).isNotNull()
        conference.days.forEach { assertDay(it) }
        conference.rooms.forEach { assertRoom(it) }
    }

    private fun assertDay(day: Day) {
        assertThat(day.index).isAtLeast(1)
        assertThat(day.date).isLocalDate()
        assertThat(day.dayStart).isOffsetDateTime()
        assertThat(day.dayEnd).isOffsetDateTime()
        assertThat(day.rooms).isNotNull()
        day.rooms.values.forEach { events ->
            events.forEach { assertEvent(it) }
        }
    }

    private fun assertRoom(room: Room) {
        assertThat(room.name).isNotEmpty()
        assertThat(room.guid).isUuid()
    }

    private fun assertEvent(event: Event) {
        assertThat(event.guid).isUuid()
        assertThat(event.id).isAtLeast(0)
        assertThat(event.date).isOffsetDateTime()
        assertThat(event.start).isLocalTime()
        assertThat(event.duration).isDuration()
        assertThat(event.room).isNotEmpty()
        assertThat(event.slug).isNotEmpty()
        assertThat(event.title).isNotEmpty()
        assertThat(event.type).isNotEmpty()
        assertThat(event.url).isNotEmpty()
    }

    private val httpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val service: ScheduleService by lazy {
        Api.provideScheduleService(BASE_URL, httpClient)
    }
}

private fun Subject.isUuid() = isInstanceOf(Uuid::class.java)
private fun Subject.isLocalDate() = isInstanceOf(LocalDate::class.java)
private fun Subject.isOffsetDateTime() = isInstanceOf(OffsetDateTime::class.java)
private fun Subject.isLocalTime() = isInstanceOf(LocalTime::class.java)
private fun Subject.isDuration() = isInstanceOf(Duration::class.java)