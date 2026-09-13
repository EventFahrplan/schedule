package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.Serializable

@Serializable
data class Generator(
    val name: String? = null,
    val url: String? = null,
    val version: String? = null,
)
