package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.ReferenceType

internal object ReferenceTypeSerializer : StringEnumSerializer<ReferenceType>("ReferenceType") {

    override fun fromWire(value: String) =
        ReferenceType.entries.firstOrNull { toWire(it) == value } ?: ReferenceType.UNKNOWN

    override fun toWire(value: ReferenceType) = when (value) {
        ReferenceType.ACTIVITY_PUB -> "ACTIVITYPUB"
        ReferenceType.AT_PROTO -> "ATPROTO"
        else -> value.name
    }
}
