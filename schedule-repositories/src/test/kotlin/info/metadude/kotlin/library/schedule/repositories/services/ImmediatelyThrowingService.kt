package info.metadude.kotlin.library.schedule.repositories.services

import info.metadude.kotlin.library.schedule.ScheduleService
import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1
import retrofit2.Response

internal class ImmediatelyThrowingService : ScheduleService {

    override suspend fun getScheduleV1(
        eTag: String,
        lastModifiedAt: String,
        path: String,
    ): Response<ScheduleV1> =
        throw RuntimeException()
}
