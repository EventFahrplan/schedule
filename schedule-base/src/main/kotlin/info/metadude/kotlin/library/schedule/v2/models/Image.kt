package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val type: String? = null,
    val url: String,
)
