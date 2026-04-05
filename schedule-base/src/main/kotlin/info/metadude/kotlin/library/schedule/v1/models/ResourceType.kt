package info.metadude.kotlin.library.schedule.v1.models

import info.metadude.kotlin.library.schedule.v1.serializers.ResourceTypeSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ResourceTypeSerializer::class)
enum class ResourceType {
    SLIDES,
    PAPER,
    WEB,
    BLOG,
    ARTICLE,
    MEDIA,
    RELATED,
    ACTIVITY_PUB,
    UNKNOWN,
}
