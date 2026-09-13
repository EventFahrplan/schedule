package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.LocalizableStringSerializer
import kotlinx.serialization.Serializable

@Serializable(with = LocalizableStringSerializer::class)
sealed interface LocalizableString {

    @Serializable
    data class Text(
        val value: String,
    ) : LocalizableString

    @Serializable
    data class Translations(
        val values: Map<String, String>,
    ) : LocalizableString

}
