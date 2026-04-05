package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.RoomTypeSerializer
import kotlinx.serialization.Serializable

@Serializable(with = RoomTypeSerializer::class)
enum class RoomType {
    LECTURE_HALL,
    STAGE,
    WORKSHOP,
    OUTSIDE,
    ONLINE,
    PROJECT,
    BBB,
    HANGAR,
    OTHER,
    UNKNOWN,
}
