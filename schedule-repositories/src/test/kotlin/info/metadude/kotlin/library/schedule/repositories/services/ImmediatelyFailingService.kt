package info.metadude.kotlin.library.schedule.repositories.services

import info.metadude.kotlin.library.schedule.ScheduleService
import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import retrofit2.Response.error

internal class ImmediatelyFailingService : ScheduleService {

    override suspend fun getScheduleV1(
        path: String,
        eTag: String?,
        lastModifiedAt: String?,
    ): Response<ScheduleV1> = errorResponse()

    private fun <Schedule> errorResponse(): Response<Schedule> {
        val responseBody = "Service Unavailable.".toResponseBody("plain/text".toMediaType())
        return error(503, responseBody)
    }
}
