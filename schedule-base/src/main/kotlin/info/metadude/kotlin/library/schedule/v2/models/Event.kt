package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.EventSerializer
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable(with = EventSerializer::class)
sealed interface Event {
    val guid: Uuid?
    val room: RoomReference?
    val subtitle: String?
    val title: String
}
