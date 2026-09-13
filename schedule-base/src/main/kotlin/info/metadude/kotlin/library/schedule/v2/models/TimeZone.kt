package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.ZoneIdSerializer
import kotlinx.serialization.Serializable
import org.threeten.bp.ZoneId

@Serializable
data class TimeZone(
    @Serializable(with = ZoneIdSerializer::class)
    val name: ZoneId,
)
