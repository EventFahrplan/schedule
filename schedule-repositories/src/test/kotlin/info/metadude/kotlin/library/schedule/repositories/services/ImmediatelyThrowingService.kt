package info.metadude.kotlin.library.schedule.repositories.services

import info.metadude.kotlin.library.schedule.ScheduleService
import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1
import info.metadude.kotlin.library.schedule.v2.models.ScheduleV2
import retrofit2.Response

internal class ImmediatelyThrowingService : ScheduleService {

    override suspend fun getScheduleV1(
        path: String,
        eTag: String?,
        lastModifiedAt: String?,
    ): Response<ScheduleV1> =
        throw RuntimeException()

    override suspend fun getScheduleV2(
        path: String,
        eTag: String?,
        lastModifiedAt: String?,
    ): Response<ScheduleV2> =
        throw RuntimeException()
}
