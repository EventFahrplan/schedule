package info.metadude.kotlin.library.schedule.repositories.models

import info.metadude.kotlin.library.schedule.v2.models.ScheduleV2

sealed interface GetScheduleV2State {

    data class Success(
        val scheduleV2: ScheduleV2,
        val responseETag: String,
        val responseLastModifiedAt: String,
    ) : GetScheduleV2State

    data class Error(
        val httpStatusCode: Int,
        val errorMessage: String,
    ) : GetScheduleV2State

    data class Failure(
        val throwable: Throwable,
    ) : GetScheduleV2State
}
