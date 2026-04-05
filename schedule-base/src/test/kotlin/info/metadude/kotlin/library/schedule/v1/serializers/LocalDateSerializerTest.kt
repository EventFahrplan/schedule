package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.LocalDate

internal class LocalDateSerializerTest {

    private val json = Json

    @ParameterizedTest
    @ValueSource(strings = ["2024-03-19", "2000-01-01", "2099-12-31"])
    fun `round trip preserves LocalDate`(iso: String) {
        val date = LocalDate.parse(iso)
        val encoded = json.encodeToJsonElement(LocalDateSerializer, date).jsonPrimitive.content
        assertThat(encoded).isEqualTo(iso)
        val decoded = json.decodeFromString(LocalDateSerializer, "\"$iso\"")
        assertThat(decoded).isEqualTo(date)
    }
}
