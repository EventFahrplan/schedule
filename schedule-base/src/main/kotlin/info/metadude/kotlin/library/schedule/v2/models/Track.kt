package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val colors: TrackColors? = null,
    val name: String,
    val sameAs: List<String> = emptyList(),
    val slug: String,
    val type: String? = null,
)
