package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Schedule(
    @JsonNames("base_url")
    val baseUrl: String? = null,
    val conference: Conference,
    val events: List<Event> = emptyList(),
    val persons: List<Person> = emptyList(),
    val version: String,
)
