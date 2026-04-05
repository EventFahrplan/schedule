package info.metadude.kotlin.library.schedule

import okhttp3.Call
import okhttp3.OkHttpClient

interface ScheduleApi {

    fun provideScheduleService(
        baseUrl: String,
        callFactory: Call.Factory = OkHttpClient.Builder().build(),
    ): ScheduleService
}
