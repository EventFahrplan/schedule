package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.ReferenceType

internal object ReferenceTypeSerializer : StringEnumSerializer<ReferenceType>("ReferenceType") {

    override fun fromWire(value: String) = when (value) {
        "ACTIVITYPUB" -> ReferenceType.ACTIVITY_PUB
        else -> runCatching { ReferenceType.valueOf(value) }.getOrDefault(ReferenceType.UNKNOWN)
    }

    override fun toWire(value: ReferenceType) = value.name
}
