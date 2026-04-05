package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.DurationSerializer
import info.metadude.kotlin.library.schedule.v1.serializers.LocalTimeSerializer
import info.metadude.kotlin.library.schedule.v1.serializers.OffsetDateTimeSerializer
import info.metadude.kotlin.library.schedule.v1.serializers.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.threeten.bp.Duration
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import kotlin.uuid.Uuid

@Serializable
data class Event(
    @Serializable(with = UuidSerializer::class)
    val guid: Uuid,
    val code: String? = null,
    val id: Int,
    val logo: String? = null,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val date: OffsetDateTime,
    @Serializable(with = LocalTimeSerializer::class)
    val start: LocalTime,
    @Serializable(with = DurationSerializer::class)
    val duration: Duration,
    val room: String,
    val slug: String,
    val title: String,
    val subtitle: String? = null,
    val language: String? = null,
    val track: String? = null,
    val type: String,
    @SerialName("abstract")
    val abstractText: String? = null,
    val description: String? = null,
    @SerialName("recording_license")
    val recordingLicense: String? = null,
    val persons: List<Person> = emptyList(),
    val url: String,
    val links: List<Link> = emptyList(),
    @SerialName("origin_url")
    val originUrl: String? = null,
    @SerialName("feedback_url")
    val feedbackUrl: String? = null,
    @SerialName("do_not_record")
    val doNotRecord: Boolean? = null,
    @SerialName("do_not_stream")
    val doNotStream: Boolean? = null,
    val attachments: List<Attachment> = emptyList(),
)
