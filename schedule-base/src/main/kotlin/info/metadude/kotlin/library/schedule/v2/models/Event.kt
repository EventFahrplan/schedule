package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.EventSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.MetaEventTypeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.OffsetDateTimeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.UuidSerializer
import kotlinx.serialization.Serializable
import org.threeten.bp.OffsetDateTime
import kotlin.uuid.Uuid

@Serializable(with = EventSerializer::class)
sealed interface Event {
    val end: OffsetDateTime
    val guid: Uuid?
    val room: RoomReference?
    val start: OffsetDateTime
    val subtitle: String?
    val title: String
}
