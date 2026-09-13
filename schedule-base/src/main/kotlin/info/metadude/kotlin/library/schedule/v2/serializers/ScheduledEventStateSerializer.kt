package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventState

internal object ScheduledEventStateSerializer : StringEnumSerializer<ScheduledEventState>("ScheduledEventState") {

    override fun fromWire(value: String) = runCatching { ScheduledEventState.valueOf(value) }
        .getOrDefault(ScheduledEventState.UNKNOWN)

    override fun toWire(value: ScheduledEventState) = value.name
}
