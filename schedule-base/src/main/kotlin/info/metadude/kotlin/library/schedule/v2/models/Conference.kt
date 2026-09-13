package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.LocalizableStringSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.OffsetDateTimeSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.ScheduleDurationSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.ZoneIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

@Serializable
data class Conference(
    val acronym: String,
    val colors: ConferenceColors? = null,
    val days: List<Day>,
    val defaults: ConferenceDefaults? = null,
    @Serializable(with = LocalizableStringSerializer::class)
    val description: LocalizableString? = null,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val end: OffsetDateTime,
    val keywords: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val logo: Image? = null,
    val meta: JsonObject? = null,
    val rooms: List<Room> = emptyList(),
    @Serializable(with = OffsetDateTimeSerializer::class)
    val start: OffsetDateTime,
    @SerialName("timeslot_duration")
    @Serializable(with = ScheduleDurationSerializer::class)
    val timeslotDuration: ScheduleDuration? = null,
    @SerialName("time_zone_name")
    @Serializable(with = ZoneIdSerializer::class)
    val timeZoneName: ZoneId? = null,
    val title: String,
    val tracks: List<Track> = emptyList(),
    val url: String? = null,
)
