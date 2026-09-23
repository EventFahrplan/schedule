package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val name: String,
    @SerialName("@type")
    val type: String? = null,
)
