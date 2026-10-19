package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.ScheduleDurationSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ScheduleDurationSerializer::class)
data class ScheduleDuration(
    val value: String,
)
