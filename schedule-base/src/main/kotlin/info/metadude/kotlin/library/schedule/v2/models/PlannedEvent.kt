package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.EventStateSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.LocalizableStringSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.OffsetDateTimeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.ScheduleDurationSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.ScheduledEventTypeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.threeten.bp.OffsetDateTime
import kotlin.uuid.Uuid

@Serializable
data class PlannedEvent(
    @SerialName("abstract")
    @Serializable(with = LocalizableStringSerializer::class)
    val abstractText: LocalizableString?,
    val attachments: List<Reference> = emptyList(),
    val code: String? = null,
    @Serializable(with = LocalizableStringSerializer::class)
    val description: LocalizableString? = null,
    @Serializable(with = ScheduleDurationSerializer::class)
    val duration: ScheduleDuration? = null,
    @Serializable(with = UuidSerializer::class)
    override val guid: Uuid,
    val image: Image? = null,
    val languages: List<String>? = null,
    val links: List<Reference> = emptyList(),
    val location: Location? = null,
    val participants: List<Participant>,
    override val room: RoomReference? = null,
    val slug: String? = null,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val start: OffsetDateTime? = null,
    @Serializable(with = EventStateSerializer::class)
    val state: EventState = EventState.CONFIRMED,
    override val subtitle: String?,
    override val title: String,
    val track: TrackReference? = null,
    @Serializable(with = ScheduledEventTypeSerializer::class)
    val type: ScheduledEventType,
    val url: String,
) : Event
