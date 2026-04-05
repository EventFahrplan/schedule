package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v1.models.RoomType
import info.metadude.kotlin.library.schedule.v1.models.RoomType.BBB
import info.metadude.kotlin.library.schedule.v1.models.RoomType.HANGAR
import info.metadude.kotlin.library.schedule.v1.models.RoomType.LECTURE_HALL
import info.metadude.kotlin.library.schedule.v1.models.RoomType.ONLINE
import info.metadude.kotlin.library.schedule.v1.models.RoomType.OTHER
import info.metadude.kotlin.library.schedule.v1.models.RoomType.OUTSIDE
import info.metadude.kotlin.library.schedule.v1.models.RoomType.PROJECT
import info.metadude.kotlin.library.schedule.v1.models.RoomType.STAGE
import info.metadude.kotlin.library.schedule.v1.models.RoomType.UNKNOWN
import info.metadude.kotlin.library.schedule.v1.models.RoomType.WORKSHOP
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class RoomTypeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(RoomType::class)
    fun `serialize returns corresponding string value`(type: RoomType) {
        val expected = when (type) {
            LECTURE_HALL -> "lecturehall"
            STAGE -> "stage"
            WORKSHOP -> "workshop"
            OUTSIDE -> "outside"
            ONLINE -> "online"
            PROJECT -> "project"
            BBB -> "bbb"
            HANGAR -> "hangar"
            OTHER -> "other"
            UNKNOWN -> "unknown"
        }
        val actual = json.encodeToJsonElement(RoomTypeSerializer, type).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(RoomType::class)
    fun `deserialize returns corresponding RoomType value`(expected: RoomType) {
        val wire = when (expected) {
            LECTURE_HALL -> "lecturehall"
            STAGE -> "stage"
            WORKSHOP -> "workshop"
            OUTSIDE -> "outside"
            ONLINE -> "online"
            PROJECT -> "project"
            BBB -> "bbb"
            HANGAR -> "hangar"
            OTHER -> "other"
            UNKNOWN -> "unknown"
        }
        val actual = json.decodeFromString(RoomTypeSerializer, "\"$wire\"")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `deserialize maps unknown string to UNKNOWN`() {
        val actual = json.decodeFromString(RoomTypeSerializer, "\"garbage\"")
        assertThat(actual).isEqualTo(UNKNOWN)
    }
}
