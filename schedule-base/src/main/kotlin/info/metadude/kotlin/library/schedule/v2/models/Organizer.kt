package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.Serializable

@Serializable
data class Organizer(
    val name: String? = null,
    val title: String? = null,
    val url: String? = null,
)
