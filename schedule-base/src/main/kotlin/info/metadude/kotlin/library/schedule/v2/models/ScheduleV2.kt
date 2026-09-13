package info.metadude.kotlin.library.schedule.v2.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleV2(
    val generator: Generator? = null,
    val schedule: Schedule,
    @SerialName($$"$schema")
    val schema: String? = null,
) {

    /**
     * Version taken from the fragment of the [schema] URL, e.g. `beta-1` for
     * `https://c3voc.de/schedule/schema2.json#beta-1`. `null` if there is no fragment.
     */
    val schemaVersion: String?
        get() = schema?.substringAfter('#', missingDelimiterValue = "")?.ifEmpty { null }

}
