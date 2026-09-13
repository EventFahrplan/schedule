package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.ThemeSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ConferenceDefaults(
    val language: String? = null,
    @Serializable(with = ThemeSerializer::class) val theme: Theme = Theme.UNKNOWN,
)
