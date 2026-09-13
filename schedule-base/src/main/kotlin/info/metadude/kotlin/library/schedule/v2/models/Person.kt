package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.LocalizableStringSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.UuidSerializer
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class Person(
    val avatar: Image? = null,
    @Serializable(with = LocalizableStringSerializer::class)
    val biography: LocalizableString? = null,
    @Serializable(with = UuidSerializer::class)
    val guid: Uuid? = null,
    val links: List<Reference> = emptyList(),
    val name: String? = null,
    val url: String? = null,
)
