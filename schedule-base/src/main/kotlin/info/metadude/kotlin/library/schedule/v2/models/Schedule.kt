package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    @SerialName("base_url")
    val baseUrl: String? = null,
    val conference: Conference,
    val events: List<Event> = emptyList(),
    val persons: List<Person> = emptyList(),
    val version: String,
)
