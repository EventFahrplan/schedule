package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.UuidSerializer
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class Assembly(
    val name: String,
    val slug: String,
    @Serializable(with = UuidSerializer::class)
    val guid: Uuid,
    val url: String? = null,
)
