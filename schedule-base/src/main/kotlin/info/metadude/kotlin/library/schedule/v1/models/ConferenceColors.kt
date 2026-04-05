package info.metadude.kotlin.library.schedule.v1.models

import kotlinx.serialization.Serializable

@Serializable
data class ConferenceColors(
    val primary: String? = null,
    val background: String? = null,
)
