package info.metadude.kotlin.library.schedule.v2.models

import info.metadude.kotlin.library.schedule.v2.serializers.ReferenceServiceSerializer
import info.metadude.kotlin.library.schedule.v2.serializers.ReferenceTypeSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Reference(
    @Serializable(with = ReferenceServiceSerializer::class) val service: ReferenceService = ReferenceService.UNKNOWN,
    val title: String? = null,
    @Serializable(with = ReferenceTypeSerializer::class) val type: ReferenceType = ReferenceType.UNKNOWN,
    val url: String,
)
