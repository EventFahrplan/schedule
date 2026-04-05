package info.metadude.kotlin.library.schedule.v1.models

import kotlinx.serialization.Serializable

sealed interface RoomDescription {

    @Serializable
    data class Text(
        val value: String,
    ) : RoomDescription

    @Serializable
    data class Translations(
        val values: Map<String, String>,
    ) : RoomDescription

}
