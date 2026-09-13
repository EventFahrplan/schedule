package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.LocalizableStringSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.RoomTypeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.UuidSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlin.uuid.Uuid

@Serializable
data class Room(
    val capacity: Double? = null,
    @Serializable(with = LocalizableStringSerializer::class)
    val description: LocalizableString? = null,
    val features: RoomFeatures = RoomFeatures(),
    val geolocation: JsonObject? = null,
    @Serializable(with = UuidSerializer::class)
    val guid: Uuid,
    val name: String,
    val parent: RoomParent? = null,
    val sameAs: List<String> = emptyList(),
    val slug: String? = null,
    val stream: RoomStream? = null,
    @Serializable(with = RoomTypeSerializer::class)
    val type: RoomType = RoomType.UNKNOWN,
    val url: String? = null,
)
