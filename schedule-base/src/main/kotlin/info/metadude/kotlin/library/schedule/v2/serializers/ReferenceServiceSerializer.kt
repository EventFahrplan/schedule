package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.ReferenceService

internal object ReferenceServiceSerializer : StringEnumSerializer<ReferenceService>("ReferenceService") {

    override fun fromWire(value: String) = when (value) {
        "MEDIACCCDE" -> ReferenceService.MEDIA_CCC_DE
        else -> runCatching { ReferenceService.valueOf(value) }.getOrDefault(ReferenceService.UNKNOWN)
    }

    override fun toWire(value: ReferenceService) = value.name
}
