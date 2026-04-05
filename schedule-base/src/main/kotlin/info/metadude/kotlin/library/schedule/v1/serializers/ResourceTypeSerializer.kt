package info.metadude.kotlin.library.schedule.v1.serializers

import info.metadude.kotlin.library.schedule.v1.models.ResourceType
import info.metadude.kotlin.library.schedule.v1.models.ResourceType.ACTIVITY_PUB
import info.metadude.kotlin.library.schedule.v1.models.ResourceType.ARTICLE
import info.metadude.kotlin.library.schedule.v1.models.ResourceType.BLOG
import info.metadude.kotlin.library.schedule.v1.models.ResourceType.MEDIA
import info.metadude.kotlin.library.schedule.v1.models.ResourceType.PAPER
import info.metadude.kotlin.library.schedule.v1.models.ResourceType.RELATED
import info.metadude.kotlin.library.schedule.v1.models.ResourceType.SLIDES
import info.metadude.kotlin.library.schedule.v1.models.ResourceType.UNKNOWN
import info.metadude.kotlin.library.schedule.v1.models.ResourceType.WEB
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ResourceTypeSerializer : KSerializer<ResourceType> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ResourceType", STRING)

    override fun deserialize(decoder: Decoder): ResourceType = when (decoder.decodeString()) {
        "slides" -> SLIDES
        "paper" -> PAPER
        "web" -> WEB
        "blog" -> BLOG
        "article" -> ARTICLE
        "media" -> MEDIA
        "related" -> RELATED
        "activitypub" -> ACTIVITY_PUB
        else -> UNKNOWN
    }

    override fun serialize(encoder: Encoder, value: ResourceType) {
        val serialized = when (value) {
            SLIDES -> "slides"
            PAPER -> "paper"
            WEB -> "web"
            BLOG -> "blog"
            ARTICLE -> "article"
            MEDIA -> "media"
            RELATED -> "related"
            ACTIVITY_PUB -> "activitypub"
            UNKNOWN -> "unknown"
        }
        encoder.encodeString(serialized)
    }
}
