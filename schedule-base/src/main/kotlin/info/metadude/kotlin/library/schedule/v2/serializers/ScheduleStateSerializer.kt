package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.ScheduleState

internal object ScheduleStateSerializer : StringEnumSerializer<ScheduleState>("ScheduleState") {

    override fun fromWire(value: String) = runCatching { ScheduleState.valueOf(value) }
        .getOrDefault(ScheduleState.UNKNOWN)

    override fun toWire(value: ScheduleState) = value.name
}
