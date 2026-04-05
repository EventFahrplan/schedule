package info.metadude.kotlin.library.schedule.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleV1(
    @SerialName($$"$schema")
    val schema: String = "",
    val generator: Generator? = null,
    val schedule: Schedule,
)
