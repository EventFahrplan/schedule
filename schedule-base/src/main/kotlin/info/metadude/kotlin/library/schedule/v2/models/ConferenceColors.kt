package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.Serializable

@Serializable
data class ConferenceColors(
    val background: String? = null,
    val primary: String? = null,
)
