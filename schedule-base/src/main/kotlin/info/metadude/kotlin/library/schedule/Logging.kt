package info.metadude.kotlin.library.schedule

fun interface Logging {

    fun onDeserializeFailed(jsonElement: String, throwable: Throwable)

    companion object {
        val None = Logging { _, _ -> }
    }
}
