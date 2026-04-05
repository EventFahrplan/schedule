package info.metadude.kotlin.library.schedule.v1.serializers

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.v1.models.ConferenceDate.Date
import info.metadude.kotlin.library.schedule.v1.models.ConferenceDate.DateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

internal class ConferenceDateSerializerTest {

    private val json = Json

    @Test
    fun `deserialize date only string to Date`() {
        val decoded = json.decodeFromString(ConferenceDateSerializer, "\"2024-07-01\"")
        assertThat(decoded).isEqualTo(Date(LocalDate.of(2024, 7, 1)))
    }

    @Test
    fun `deserialize offset datetime string to DateTime`() {
        val decoded = json.decodeFromString(ConferenceDateSerializer, "\"2024-07-01T12:00:00+02:00\"")
        val expected = DateTime(OffsetDateTime.parse("2024-07-01T12:00:00+02:00"))
        assertThat(decoded).isEqualTo(expected)
    }

    @Test
    fun `serialize Date as local date string`() {
        val value = Date(LocalDate.of(2024, 7, 1))
        val encoded = json.encodeToJsonElement(ConferenceDateSerializer, value).jsonPrimitive.content
        assertThat(encoded).isEqualTo("2024-07-01")
    }

    @Test
    fun `serialize DateTime as offset datetime string`() {
        val odt = OffsetDateTime.parse("2024-07-01T12:00:00+02:00")
        val value = DateTime(odt)
        val encoded = json.encodeToJsonElement(ConferenceDateSerializer, value).jsonPrimitive.content
        assertThat(OffsetDateTime.parse(encoded)).isEqualTo(odt)
    }
}
