package info.metadude.kotlin.library.schedule.repositories.models

import info.metadude.kotlin.library.schedule.v1.models.ScheduleV1

sealed interface GetScheduleV1State {

    data class Success(
        val scheduleV1: ScheduleV1,
        val responseETag: String,
        val responseLastModifiedAt: String,
    ) : GetScheduleV1State

    data class Error(
        val httpStatusCode: Int,
        val errorMessage: String,
    ) : GetScheduleV1State

    data class Failure(
        val throwable: Throwable,
    ) : GetScheduleV1State
}
