package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.UuidSerializer
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class Person(
    val avatar: Image? = null,
    val biography: String? = null,
    @Serializable(with = UuidSerializer::class)
    val guid: Uuid,
    val links: List<Reference> = emptyList(),
    val name: String,
    val url: String? = null,
)
