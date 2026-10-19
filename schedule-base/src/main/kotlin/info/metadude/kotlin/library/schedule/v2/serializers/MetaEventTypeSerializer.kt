package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.MetaEventType

internal object MetaEventTypeSerializer : StringEnumSerializer<MetaEventType>("MetaEventType") {

    override fun fromWire(value: String) = runCatching { MetaEventType.valueOf(value) }
        .getOrDefault(MetaEventType.UNKNOWN)

    override fun toWire(value: MetaEventType) = value.name
}
