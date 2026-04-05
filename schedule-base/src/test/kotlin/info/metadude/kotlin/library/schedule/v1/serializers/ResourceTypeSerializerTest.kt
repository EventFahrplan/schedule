package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class ResourceTypeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(ResourceType::class)
    fun `serialize returns corresponding string value`(type: ResourceType) {
        val expected = when (type) {
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
        val actual = json.encodeToJsonElement(ResourceTypeSerializer, type).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(ResourceType::class)
    fun `deserialize returns corresponding ResourceType value`(expected: ResourceType) {
        val wire = when (expected) {
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
        val actual = json.decodeFromString(ResourceTypeSerializer, "\"$wire\"")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `deserialize maps unknown string to UNKNOWN`() {
        val actual = json.decodeFromString(ResourceTypeSerializer, "\"nope\"")
        assertThat(actual).isEqualTo(UNKNOWN)
    }
}
