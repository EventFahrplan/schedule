package info.metadude.kotlin.library.schedule.v1.models

import kotlinx.serialization.Serializable

@Serializable
data class Generator(
    val name: String? = null,
    val version: String? = null,
    val url: String? = null,
)
