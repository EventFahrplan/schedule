package info.metadude.kotlin.library.schedule.repositories.simple

import info.metadude.kotlin.library.schedule.Api
import info.metadude.kotlin.library.schedule.Logging
import info.metadude.kotlin.library.schedule.Logging.Companion.None
import info.metadude.kotlin.library.schedule.ScheduleApi
import info.metadude.kotlin.library.schedule.ScheduleService
import info.metadude.kotlin.library.schedule.repositories.ScheduleRepository
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State
import info.metadude.kotlin.library.schedule.repositories.utils.UrlComponents.Companion.getUrlComponents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Call
import okhttp3.OkHttpClient
import retrofit2.Response
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State.Error as ErrorV1
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State.Failure as FailureV1
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State.Success as SuccessV1

class SimpleScheduleRepository(
    private val callFactory: Call.Factory = OkHttpClient.Builder().build(),
    private val logging: Logging = None,
    private val api: ScheduleApi = Api,
) : ScheduleRepository {

    private companion object {
        const val HEADER_NAME_ETAG = "ETag"
        const val HEADER_NAME_LAST_MODIFIED = "Last-Modified"
    }

    override suspend fun getScheduleV1State(
        url: String,
        requestETag: String,
        lastModifiedAt: String,
    ): Flow<GetScheduleV1State> = getScheduleState(
        url = url,
        requestETag = requestETag,
        lastModifiedAt = lastModifiedAt,
        request = ScheduleService::getScheduleV1,
        success = ::SuccessV1,
        error = ::ErrorV1,
        failure = ::FailureV1,
    )

    private fun <Schedule, State> getScheduleState(
        url: String,
        requestETag: String,
        lastModifiedAt: String,
        request: suspend (ScheduleService, String, String?, String?) -> Response<Schedule>,
        success: (Schedule, String, String) -> State,
        error: (Int, String) -> State,
        failure: (Throwable) -> State,
    ): Flow<State> = flow {
        val emission = try {
            val (baseUrl, path) = url.getUrlComponents()
            val response = request(
                api.provideScheduleService(baseUrl, callFactory, logging),
                path,
                requestETag.ifBlank { null },
                lastModifiedAt.ifBlank { null },
            )
            response.toState(success, error)
        } catch (t: Throwable) {
            failure(t)
        }
        emit(emission)
    }

    private fun <Schedule, State> Response<Schedule>.toState(
        success: (Schedule, String, String) -> State,
        error: (Int, String) -> State,
    ): State {
        if (!isSuccessful) {
            return error(code(), errorBody()?.string() ?: message().orEmpty())
        }
        val body = body() ?: return error(code(), message().orEmpty())
        return success(
            body,
            headers()[HEADER_NAME_ETAG].orEmpty(),
            headers()[HEADER_NAME_LAST_MODIFIED].orEmpty(),
        )
    }

}
