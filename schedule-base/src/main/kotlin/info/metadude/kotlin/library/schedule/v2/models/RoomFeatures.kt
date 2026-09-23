package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.RecordingModeSerializer
import kotlinx.serialization.Serializable

@Serializable
data class RoomFeatures(
    @Serializable(with = RecordingModeSerializer::class)
    val recording: RecordingMode = RecordingMode.UNKNOWN,
)
