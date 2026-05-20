package info.metadude.kotlin.library.schedule.repositories.simple

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.ScheduleApi
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State.Error
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State.Failure
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State.Success
import info.metadude.kotlin.library.schedule.repositories.services.ImmediatelyFailingService
import info.metadude.kotlin.library.schedule.repositories.services.ImmediatelySucceedingService
import info.metadude.kotlin.library.schedule.repositories.services.ImmediatelyThrowingService
import kotlinx.coroutines.test.runTest
import okhttp3.Call
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

internal class SimpleScheduleRepositoryTest {

    private companion object {
        const val VALID_URL = "https://example.com/schedule.json"
        const val INVALID_URL = "https:/"
    }

    private lateinit var api: ScheduleApi

    @Test
    fun `getScheduleV1State returns success with schedule wrapper`() = runTest {
        api = mock {
            on { provideScheduleService(any(), any(), any()) }
                .doReturn(ImmediatelySucceedingService())
        }
        val repository = createRepository(api)
        repository.getScheduleV1State(VALID_URL, requestETag = "", lastModifiedAt = "").test {
            val state = awaitItem()
            assertThat(state).isInstanceOf(Success::class.java)
            val success = state as Success
            assertThat(success.scheduleV1.schedule.conference.acronym).isEqualTo("test")
            assertThat(success.responseETag).isEqualTo("")
            awaitComplete()
        }
    }

    @Test
    fun `getScheduleV1State returns error with http error`() = runTest {
        api = mock {
            on { provideScheduleService(any(), any(), any()) }
                .doReturn(ImmediatelyFailingService())
        }
        val repository = createRepository(api)
        repository.getScheduleV1State(VALID_URL, requestETag = "", lastModifiedAt = "").test {
            val state = awaitItem()
            assertThat(state).isInstanceOf(Error::class.java)
            val error = state as Error
            assertThat(error.httpStatusCode).isEqualTo(503)
            assertThat(error.errorMessage).isEqualTo("Service Unavailable.")
            awaitComplete()
        }
    }

    @Test
    fun `getScheduleV1State returns failure with runtime exception`() = runTest {
        api = mock {
            on { provideScheduleService(any(), any(), any()) }
                .doReturn(ImmediatelyThrowingService())
        }
        val repository = createRepository(api)
        repository.getScheduleV1State(VALID_URL, requestETag = "", lastModifiedAt = "").test {
            val state = awaitItem()
            assertThat(state).isInstanceOf(Failure::class.java)
            assertThat((state as Failure).throwable).isInstanceOf(RuntimeException::class.java)
            awaitComplete()
        }
    }

    @Test
    fun `getScheduleV1State returns failure with IllegalArgumentException`() = runTest {
        api = mock {
            on { provideScheduleService(any(), any(), any()) }
                .doReturn(ImmediatelySucceedingService())
        }
        val repository = createRepository(api)
        repository.getScheduleV1State(INVALID_URL, requestETag = "", lastModifiedAt = "").test {
            val state = awaitItem()
            assertThat(state).isInstanceOf(Failure::class.java)
            assertThat((state as Failure).throwable).isInstanceOf(IllegalArgumentException::class.java)
            awaitComplete()
        }
    }

    private fun createRepository(api: ScheduleApi) = SimpleScheduleRepository(
        callFactory = mock<Call.Factory>(),
        api = api,
    )

}
