package info.metadude.kotlin.library.schedule.repositories.services

import info.metadude.kotlin.library.schedule.ScheduleService
import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

internal class ImmediatelyFailingService : ScheduleService {

    override suspend fun getScheduleV1(
        eTag: String,
        lastModifiedAt: String,
        path: String,
    ): Response<ScheduleV1> {
        val responseBody = "Service Unavailable.".toResponseBody("plain/text".toMediaType())
        return Response.error(503, responseBody)
    }
}
