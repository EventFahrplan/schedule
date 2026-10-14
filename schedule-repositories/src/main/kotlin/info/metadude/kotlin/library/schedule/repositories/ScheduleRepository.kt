package info.metadude.kotlin.library.schedule.repositories

import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV1State
import info.metadude.kotlin.library.schedule.repositories.models.GetScheduleV2State
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun getScheduleV1State(
        url: String,
        requestETag: String,
        lastModifiedAt: String,
    ): Flow<GetScheduleV1State>

    suspend fun getScheduleV2State(
        url: String,
        requestETag: String,
        lastModifiedAt: String,
    ): Flow<GetScheduleV2State>
}
