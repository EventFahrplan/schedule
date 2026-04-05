package info.metadude.kotlin.library.schedule.v1.serializers

import info.metadude.kotlin.library.schedule.v1.models.RoomType
import info.metadude.kotlin.library.schedule.v1.models.RoomType.BBB
import info.metadude.kotlin.library.schedule.v1.models.RoomType.HANGAR
import info.metadude.kotlin.library.schedule.v1.models.RoomType.LECTURE_HALL
import info.metadude.kotlin.library.schedule.v1.models.RoomType.ONLINE
import info.metadude.kotlin.library.schedule.v1.models.RoomType.OTHER
import info.metadude.kotlin.library.schedule.v1.models.RoomType.OUTSIDE
import info.metadude.kotlin.library.schedule.v1.models.RoomType.PROJECT
import info.metadude.kotlin.library.schedule.v1.models.RoomType.STAGE
import info.metadude.kotlin.library.schedule.v1.models.RoomType.UNKNOWN
import info.metadude.kotlin.library.schedule.v1.models.RoomType.WORKSHOP
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object RoomTypeSerializer : KSerializer<RoomType> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("RoomType", STRING)

    override fun deserialize(decoder: Decoder): RoomType = when (decoder.decodeString()) {
        "lecturehall" -> LECTURE_HALL
        "stage" -> STAGE
        "workshop" -> WORKSHOP
        "outside" -> OUTSIDE
        "online" -> ONLINE
        "project" -> PROJECT
        "bbb" -> BBB
        "hangar" -> HANGAR
        "other" -> OTHER
        else -> UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: RoomType) {
        val serialized = when (value) {
            LECTURE_HALL -> "lecturehall"
            STAGE -> "stage"
            WORKSHOP -> "workshop"
            OUTSIDE -> "outside"
            ONLINE -> "online"
            PROJECT -> "project"
            BBB -> "bbb"
            HANGAR -> "hangar"
            OTHER -> "other"
            UNKNOWN -> "unknown"
        }
        encoder.encodeString(serialized)
    }
}
