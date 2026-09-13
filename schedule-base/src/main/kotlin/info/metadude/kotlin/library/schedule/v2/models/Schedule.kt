package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.ScheduleStateSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val baseUrl: String? = null,
    val conference: Conference,
    val events: List<Event>,
    val persons: List<Person> = emptyList(),
    @Serializable(with = ScheduleStateSerializer::class)
    val state: ScheduleState? = ScheduleState.PUBLISHED,
    val version: String,
)
