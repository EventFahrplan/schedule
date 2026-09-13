package info.metadude.kotlin.library.schedule

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.utils.loadJsonFile
import info.metadude.kotlin.library.schedule.v2.models.MetaEvent
import info.metadude.kotlin.library.schedule.v2.models.ScheduledEvent
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ScheduleV2ServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var service: ScheduleService

    @BeforeEach
    fun setup() {
        server = MockWebServer().apply { start() }
        service = Api
            .createRetrofit(
                baseUrl = server.url("/").toString(),
                callFactory = OkHttpClient.Builder().build(),
            )
            .create(ScheduleService::class.java)
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getScheduleV2 parses fixture`() = runTest {
        val json = loadJsonFile("schedule_v2_minor.json")
        server.enqueue(createResponse(json))
        val response = service.getScheduleV2("schedule2.json")

        assertThat(response.isSuccessful).isTrue()
        val schedule = requireNotNull(response.body()) { "Missing body" }.schedule
        assertThat(schedule.events[0]).isInstanceOf(ScheduledEvent::class.java)
        assertThat(schedule.events[1]).isInstanceOf(MetaEvent::class.java)
    }

    @Test
    fun `getScheduleV2 fails when JSON is malformed`() = runTest {
        server.enqueue(createResponse("not json"))
        assertThrows<Exception> {
            service.getScheduleV2("")
        }
    }

    private fun createResponse(json: String) = MockResponse()
        .setBody(json)
        .addHeader("Content-Type", "application/json; charset=UTF-8")
}
