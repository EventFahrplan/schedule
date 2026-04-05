package info.metadude.kotlin.library.schedule.v1.models

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val url: String,
    val type: ResourceType = ResourceType.UNKNOWN,
    val title: String = "",
)
