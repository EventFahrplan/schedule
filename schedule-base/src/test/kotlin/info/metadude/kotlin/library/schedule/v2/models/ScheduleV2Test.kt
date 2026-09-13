package info.metadude.kotlin.library.schedule.v2.models

import com.google.common.truth.Truth.assertThat
import info.metadude.kotlin.library.schedule.ScheduleJson
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

internal class ScheduleV2Test {

    @ParameterizedTest
    @CsvSource(
        "https://c3voc.de/schedule/schema2.json#beta-1, beta-1",
        "https://c3voc.de/schedule/schema2.json#1.0, 1.0",
    )
    fun `schemaVersion returns the fragment of the schema URL`(schema: String, expected: String) {
        assertThat(scheduleV2(schema).schemaVersion).isEqualTo(expected)
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = ["", "https://c3voc.de/schedule/schema2.json", "https://c3voc.de/schedule/schema2.json#"])
    fun `schemaVersion returns null if the schema URL has no fragment`(schema: String?) {
        assertThat(scheduleV2(schema).schemaVersion).isNull()
    }

    @Test
    fun `schemaVersion is not serialized`() {
        val element = ScheduleJson.create().encodeToJsonElement(scheduleV2("https://c3voc.de/schedule/schema2.json#beta-1"))
        assertThat(element.jsonObject.keys).doesNotContain("schemaVersion")
    }

    private fun scheduleV2(schema: String?) = ScheduleV2(
        schedule = Schedule(
            version = "1.0",
            conference = Conference(
                acronym = "test",
                title = "Test",
                start = OffsetDateTime.parse("2026-01-01T10:00:00Z"),
                end = OffsetDateTime.parse("2026-01-02T10:00:00Z"),
                timeZone = TimeZone(ZoneId.of("UTC")),
                days = emptyList(),
            ),
            events = emptyList(),
            persons = emptyList(),
        ),
        schema = schema,
    )
}
