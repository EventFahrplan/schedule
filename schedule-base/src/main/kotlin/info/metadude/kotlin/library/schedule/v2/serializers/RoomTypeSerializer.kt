package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.RoomType

internal object RoomTypeSerializer : StringEnumSerializer<RoomType>("RoomType") {

    override fun fromWire(value: String) = when (value) {
        "LECTUREHALL" -> RoomType.LECTURE_HALL
        else -> runCatching { RoomType.valueOf(value) }.getOrDefault(RoomType.UNKNOWN)
    }

    override fun toWire(value: RoomType) = value.name
}
