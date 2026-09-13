package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.LocalizableStringSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.OffsetDateTimeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.ScheduleDurationSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.ScheduledEventStateSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.ScheduledEventTypeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.threeten.bp.OffsetDateTime
import kotlin.uuid.Uuid

@Serializable
data class ScheduledEvent(
    @SerialName("abstract")
    @Serializable(with = LocalizableStringSerializer::class)
    val abstractText: LocalizableString?,
    val attachments: List<Reference> = emptyList(),
    val code: String? = null,
    @Serializable(with = LocalizableStringSerializer::class)
    val description: LocalizableString? = null,
    @Serializable(with = ScheduleDurationSerializer::class) val duration: ScheduleDuration? = null,
    @Serializable(with = OffsetDateTimeSerializer::class) override val end: OffsetDateTime,
    @Serializable(with = UuidSerializer::class) override val guid: Uuid,
    val image: Image? = null,
    val language: String? = null,
    val links: List<Reference>,
    val location: Location? = null,
    val logo: Image? = null,
    val participants: List<EventParticipant>,
    override val room: RoomReference? = null,
    val slug: String,
    @Serializable(with = OffsetDateTimeSerializer::class) override val start: OffsetDateTime,
    @Serializable(with = ScheduledEventStateSerializer::class)
    val state: ScheduledEventState = ScheduledEventState.CONFIRMED,
    override val subtitle: String?,
    override val title: String,
    val track: TrackReference? = null,
    @Serializable(with = ScheduledEventTypeSerializer::class) val type: ScheduledEventType,
    val url: String,
) : Event
