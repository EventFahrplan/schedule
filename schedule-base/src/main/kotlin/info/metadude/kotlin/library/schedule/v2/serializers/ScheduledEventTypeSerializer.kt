package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.ScheduledEventType

internal object ScheduledEventTypeSerializer : StringEnumSerializer<ScheduledEventType>("ScheduledEventType") {

    override fun fromWire(value: String) =
        runCatching { ScheduledEventType.valueOf(value) }
            .getOrDefault(ScheduledEventType.UNKNOWN)


    override fun toWire(value: ScheduledEventType) =
        value.name
}
