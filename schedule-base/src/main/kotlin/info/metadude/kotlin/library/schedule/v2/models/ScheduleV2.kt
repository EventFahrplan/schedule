package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleV2(
    val generator: Generator? = null,
    val schedule: Schedule,
    @SerialName($$"$schema")
    val schema: String? = null,
)
