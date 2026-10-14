package info.metadude.kotlin.library.schedule.v2.serializers

import info.metadude.kotlin.library.schedule.v2.models.Theme

internal object ThemeSerializer : StringEnumSerializer<Theme>("Theme") {

    override fun fromWire(value: String) = when (value) {
        "light" -> Theme.LIGHT
        "dark" -> Theme.DARK
        else -> Theme.UNKNOWN
    }

    override fun toWire(value: Theme) = when (value) {
        Theme.LIGHT -> "light"
        Theme.DARK -> "dark"
        Theme.UNKNOWN -> "UNKNOWN"
    }
}
