package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.MetaEventTypeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.OffsetDateTimeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.UuidSerializer
import kotlinx.serialization.Serializable
import org.threeten.bp.OffsetDateTime
import kotlin.uuid.Uuid

@Serializable
data class MetaEvent(
    @Serializable(with = OffsetDateTimeSerializer::class) override val end: OffsetDateTime,
    @Serializable(with = UuidSerializer::class) override val guid: Uuid? = null,
    override val room: RoomReference? = null,
    @Serializable(with = OffsetDateTimeSerializer::class) override val start: OffsetDateTime,
    override val subtitle: String? = null,
    override val title: String,
    @Serializable(with = MetaEventTypeSerializer::class) val type: MetaEventType,
) : Event
