package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.RecordingModeSerializer
import kotlinx.serialization.Serializable

@Serializable(with = RecordingModeSerializer::class)
enum class RecordingMode {
    RECORD_BY_DEFAULT,
    NOT_RECORDED_BY_DEFAULT,
    RECORDING_FORBIDDEN,
    RECORDING_NOT_POSSIBLE,
    UNKNOWN,
}
