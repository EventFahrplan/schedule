package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.EventState

internal object EventStateSerializer : StringEnumSerializer<EventState>("EventState") {

    override fun fromWire(value: String) = runCatching { EventState.valueOf(value) }
        .getOrDefault(EventState.UNKNOWN)

    override fun toWire(value: EventState) = value.name
}
