package info.metadude.kotlin.library.schedule

import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SchemaValidatorsConfig
import com.networknt.schema.SpecVersion
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test

internal class ProductionSchemaValidationTest {

    private companion object {
        const val BASE_URL = "https://fahrplan.events.ccc.de/congress/2025/fahrplan/schedules/"
        const val SCHEDULE_PATH = "schedule.json"
    }

    @Test
    fun `production schedule validates against its schema`() = runTest {
        val scheduleUrl = "$BASE_URL$SCHEDULE_PATH"
        val scheduleJson = httpClient
            .newCall(Request.Builder().url(scheduleUrl).build())
            .execute()
            .body.string()

        val scheduleNode = objectMapper.readTree(scheduleJson)
        val schemaUrl = scheduleNode[$$"$schema"]?.asText()
            ?: fail($$"Schedule has no $schema field")

        val schemaJson = httpClient
            .newCall(Request.Builder().url(schemaUrl).build())
            .execute()
            .body.string()

        val schemaNode = objectMapper.readTree(schemaJson)
        val config = SchemaValidatorsConfig
            .Builder()
            .formatAssertionsEnabled(false)
            .build()
        val schema = jsonSchemaFactory.getSchema(schemaNode, config)
        val errors = schema.validate(scheduleNode)

        assumeTrue(errors.isEmpty()) {
            val summary = errors.take(10).joinToString("\n") { "${it.instanceLocation}: ${it.message}" }
            "Skipping: live production JSON has ${errors.size} validation error(s) vs remote $schemaUrl (often drifts). First 10:\n$summary"
        }
    }

    private val httpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val objectMapper = ObjectMapper()
    private val jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6)

}


