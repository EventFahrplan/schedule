package info.metadude.kotlin.library.schedule

import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import org.jetbrains.annotations.VisibleForTesting
import retrofit2.Converter.Factory
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object Api : ScheduleApi {

    private val CONTENT_TYPE = "application/json; charset=UTF-8".toMediaType()

    override fun provideScheduleService(baseUrl: String, callFactory: Call.Factory): ScheduleService {
        require(baseUrl.isNotEmpty()) { "baseUrl is empty." }
        val retrofit = createRetrofit(baseUrl, callFactory)
        return retrofit.create(ScheduleService::class.java)
    }

    @VisibleForTesting
    fun createRetrofit(baseUrl: String, callFactory: Call.Factory): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(createJsonConverterFactory())
        .callFactory(callFactory)
        .build()

    private fun createJsonConverterFactory(): Factory {
        val json = Json { ignoreUnknownKeys = true }
        return json.asConverterFactory(CONTENT_TYPE)
    }
}
