package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.RecordingMode

internal object RecordingModeSerializer : StringEnumSerializer<RecordingMode>("RecordingMode") {

    override fun fromWire(value: String) = runCatching { RecordingMode.valueOf(value) }
        .getOrDefault(RecordingMode.UNKNOWN)

    override fun toWire(value: RecordingMode) = value.name
}
