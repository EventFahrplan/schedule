package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.OffsetDateTime

internal class OffsetDateTimeSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(
        strings = [
            "2024-03-19T14:30:00+01:00",
            "2000-01-01T00:00:00Z",
            "2099-12-31T23:59:59.123456789-05:00",
        ],
    )
    fun `round trip preserves OffsetDateTime`(iso: String) {
        val value = OffsetDateTime.parse(iso)
        val encoded = json.encodeToJsonElement(OffsetDateTimeSerializer, value).jsonPrimitive.content
        assertThat(OffsetDateTime.parse(encoded)).isEqualTo(value)
        val decoded = json.decodeFromString(OffsetDateTimeSerializer, "\"${value}\"")
        assertThat(decoded).isEqualTo(value)
    }
}
