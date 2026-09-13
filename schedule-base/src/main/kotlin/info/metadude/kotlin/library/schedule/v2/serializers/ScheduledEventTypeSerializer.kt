package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventType

internal object ScheduledEventTypeSerializer : StringEnumSerializer<ScheduledEventType>("ScheduledEventType") {

    override fun fromWire(value: String) =
        ScheduledEventType.entries.firstOrNull { toWire(it) == value } ?: ScheduledEventType.UNKNOWN

    override fun toWire(value: ScheduledEventType) = when (value) {
        ScheduledEventType.HANDS_ON -> "HANDS-ON"
        else -> value.name
    }
}
