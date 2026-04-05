package info.metadude.kotlin.library.schedule.v1.models

import kotlinx.serialization.Serializable

@Serializable
data class RoomFeatures(
    val recording: RecordingMode? = null,
)
