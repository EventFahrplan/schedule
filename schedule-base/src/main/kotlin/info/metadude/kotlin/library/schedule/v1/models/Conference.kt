package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.ConferenceDateSerializer
import info.metadude.kotlin.library.schedule.v1.serializers.DurationSerializer
import info.metadude.kotlin.library.schedule.v1.serializers.ZoneIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.threeten.bp.Duration
import org.threeten.bp.ZoneId

@Serializable
data class Conference(
    val acronym: String,
    val title: String,
    val description: String? = null,
    @Serializable(with = ConferenceDateSerializer::class)
    val start: ConferenceDate,
    @Serializable(with = ConferenceDateSerializer::class)
    val end: ConferenceDate,
    @SerialName("daysCount")
    val daysCount: Int,
    @SerialName("timeslot_duration")
    @Serializable(with = DurationSerializer::class)
    val timeslotDuration: Duration,
    @SerialName("time_zone_name")
    @Serializable(with = ZoneIdSerializer::class)
    val timeZoneName: ZoneId? = null,
    val logo: String? = null,
    val colors: ConferenceColors? = null,
    val keywords: List<String> = emptyList(),
    val url: String? = null,
    val tracks: List<Track> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val days: List<Day>,
)
