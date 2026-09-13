package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.LocalDateSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

@Serializable
data class Day(
    @Serializable(with = LocalDateSerializer::class) val date: LocalDate,
    @Serializable(with = OffsetDateTimeSerializer::class) val end: OffsetDateTime,
    val index: Int,
    @Serializable(with = OffsetDateTimeSerializer::class) val start: OffsetDateTime,
)
