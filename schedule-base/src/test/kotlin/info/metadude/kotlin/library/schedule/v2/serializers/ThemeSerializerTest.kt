package info.metadude.kotlin.library.schedule.v2.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v2.models.Theme
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

internal class ThemeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @EnumSource(Theme::class)
    fun `serialize returns corresponding string value`(value: Theme) {
        val expected = when (value) {
            Theme.LIGHT -> "light"
            Theme.DARK -> "dark"
            Theme.UNKNOWN -> "UNKNOWN"
        }
        val actual = json.encodeToJsonElement(ThemeSerializer, value).jsonPrimitive.content
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @EnumSource(Theme::class)
    fun `deserialize returns corresponding Theme value`(expected: Theme) {
        val wire = when (expected) {
            Theme.LIGHT -> "light"
            Theme.DARK -> "dark"
            Theme.UNKNOWN -> "UNKNOWN"
        }
        val actual = json.decodeFromJsonElement(ThemeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid", "", "LIGHT"])
    fun `deserialize maps unknown string to UNKNOWN`(wire: String) {
        val actual = json.decodeFromJsonElement(ThemeSerializer, JsonPrimitive(wire))
        assertThat(actual).isEqualTo(Theme.UNKNOWN)
    }
}
