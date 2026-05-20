package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.LocalDateSerializer
import info.metadude.kotlin.library.schedule.v1.serializers.OffsetDateTimeSerializer
import info.metadude.kotlin.library.schedule.v1.serializers.RoomsSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

@Serializable
data class Day(
    val index: Int,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    @SerialName("day_start")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val dayStart: OffsetDateTime,
    @SerialName("day_end")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val dayEnd: OffsetDateTime,
    @Serializable(with = RoomsSerializer::class)
    val rooms: Map<String, List<Event>>,
)
