package info.metadude.kotlin.library.schedule.v1.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val version: String,
    @SerialName("base_url")
    val baseUrl: String? = null,
    val conference: Conference,
)
