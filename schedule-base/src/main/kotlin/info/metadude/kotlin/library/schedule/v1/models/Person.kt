package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class Person(
    @Serializable(with = UuidSerializer::class)
    val guid: Uuid,
    val id: Int? = null,
    val code: String? = null,
    val name: String,
    @SerialName("public_name")
    val publicName: String? = null,
    val url: String? = null,
    val links: List<Link> = emptyList(),
    val avatar: String? = null,
    val biography: String? = null,
)
