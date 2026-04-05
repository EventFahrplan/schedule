package info.metadude.kotlin.library.schedule.v1.serializers

import info.metadude.kotlin.library.schedule.v1.models.RecordingMode
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.NOT_RECORDED_BY_DEFAULT
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.RECORDING_FORBIDDEN
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.RECORDING_NOT_POSSIBLE
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.RECORD_BY_DEFAULT
import info.metadude.kotlin.library.schedule.v1.models.RecordingMode.UNKNOWN
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object RecordingModeSerializer : KSerializer<RecordingMode> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("RecordingMode", STRING)

    override fun deserialize(decoder: Decoder): RecordingMode = when (decoder.decodeString()) {
        "record_by_default" -> RECORD_BY_DEFAULT
        "not_recorded_by_default" -> NOT_RECORDED_BY_DEFAULT
        "recording_forbidden" -> RECORDING_FORBIDDEN
        "recording_not_possible" -> RECORDING_NOT_POSSIBLE
        "unknown" -> UNKNOWN
        else -> UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: RecordingMode) {
        val serialized = when (value) {
            RECORD_BY_DEFAULT -> "record_by_default"
            NOT_RECORDED_BY_DEFAULT -> "not_recorded_by_default"
            RECORDING_FORBIDDEN -> "recording_forbidden"
            RECORDING_NOT_POSSIBLE -> "recording_not_possible"
            UNKNOWN -> "unknown"
        }
        encoder.encodeString(serialized)
    }
}
