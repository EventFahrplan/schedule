package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.UuidSerializer
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class Track(
    val color: String? = null,
    val colors: TrackColors? = null,
    @Serializable(with = UuidSerializer::class)
    val guid: Uuid? = null,
    val name: String? = null,
    val slug: String? = null,
    val type: String? = null,
)
