package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Location(
    val address: String? = null,
    val geolocation: JsonObject? = null,
    val name: String,
    val sameAs: List<String> = emptyList(),
    val url: String? = null,
)
