package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.LocalDateSerializer
import info.metadude.kotlin.library.schedule.v1.serializers.OffsetDateTimeSerializer
import kotlinx.serialization.Serializable
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

sealed interface ConferenceDate {

    @Serializable
    data class Date(
        @Serializable(with = LocalDateSerializer::class)
        val value: LocalDate,
    ) : ConferenceDate

    @Serializable
    data class DateTime(
        @Serializable(with = OffsetDateTimeSerializer::class)
        val value: OffsetDateTime,
    ) : ConferenceDate

}
