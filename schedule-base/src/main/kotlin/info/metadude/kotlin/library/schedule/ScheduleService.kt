package info.metadude.kotlin.library.schedule

import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ScheduleService {

    @GET("{path}")
    suspend fun getScheduleV1(
        @Path("path", encoded = true) path: String,
        @Header("If-None-Match") eTag: String? = null,
        @Header("If-Modified-Since") lastModifiedAt: String? = null,
    ): Response<ScheduleV1>
}
