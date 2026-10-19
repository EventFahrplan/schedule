package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.LocalizableStringSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.threeten.bp.OffsetDateTime

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
    val location: Location? = null,
    val logo: Image? = null,
    val meta: JsonObject? = null,
    val organizer: Organizer? = null,
    val partOf: List<String> = emptyList(),
    val rooms: List<Room> = emptyList(),
    val sameAs: List<String> = emptyList(),
    @Serializable(with = OffsetDateTimeSerializer::class)
    val start: OffsetDateTime,
    val timeZone: TimeZone? = null,
    val title: String,
    val tracks: List<Track> = emptyList(),
    val url: String? = null,
)
