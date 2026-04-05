package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.RoomDescriptionSerializer
import info.metadude.kotlin.library.schedule.v1.serializers.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class Room(
    val name: String,
    val slug: String? = null,
    @Serializable(with = UuidSerializer::class)
    val guid: Uuid,
    val type: RoomType = RoomType.UNKNOWN,
    @SerialName("stream_id")
    val streamId: String? = null,
    @Serializable(with = RoomDescriptionSerializer::class)
    val description: RoomDescription? = null,
    val capacity: Double? = null,
    val url: String? = null,
    @SerialName("description_en")
    val descriptionEn: String? = null,
    @SerialName("description_de")
    val descriptionDe: String? = null,
    val features: RoomFeatures = RoomFeatures(),
    val assembly: Assembly? = null,
)
