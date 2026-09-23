package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.Serializable

@Serializable
data class TrackColors(
    val dark: String? = null,
    val light: String? = null,
)
