package info.metadude.kotlin.library.schedule.repositories.simple

import info.metadude.kotlin.library.schedule.Api
import info.metadude.kotlin.library.schedule.Logging
import info.metadude.kotlin.library.schedule.Logging.Companion.None
import info.metadude.kotlin.library.schedule.ScheduleApi
import info.metadude.kotlin.library.schedule.repositories.ScheduleRepository
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State.Error
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State.Failure
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State.Success
import info.metadude.kotlin.library.schedule.repositories.utils.UrlComponents.Companion.getUrlComponents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Call
import okhttp3.OkHttpClient

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
    ): Flow<GetScheduleV1State> {
        return flow {
            val emission = try {
                val (baseUrl, path) = url.getUrlComponents()
                val response = api
                    .provideScheduleService(baseUrl, callFactory, logging)
                    .getScheduleV1(
                        path = path,
                        eTag = requestETag.ifBlank { null },
                        lastModifiedAt = lastModifiedAt.ifBlank { null },
                    )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null) {
                        Error(
                            httpStatusCode = response.code(),
                            errorMessage = response.message().orEmpty(),
                        )
                    } else {
                        Success(
                            scheduleV1 = body,
                            responseETag = response.headers()[HEADER_NAME_ETAG].orEmpty(),
                            responseLastModifiedAt = response.headers()[HEADER_NAME_LAST_MODIFIED].orEmpty(),
                        )
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: response.message().orEmpty()
                    Error(
                        httpStatusCode = response.code(),
                        errorMessage = errorMessage,
                    )
                }
            } catch (t: Throwable) {
                Failure(throwable = t)
            }
            emit(emission)
        }
    }

}
