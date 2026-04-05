package info.metadude.kotlin.library.schedule.v1.models

import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val name: String,
    val color: String,
    val slug: String,
)
