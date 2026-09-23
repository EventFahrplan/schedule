package info.metadude.kotlin.library.schedule.utils

internal fun loadJsonFile(fileName: String) =
    requireNotNull(
        value = JsonFileLoader::class.java.classLoader.getResourceAsStream(fileName),
        lazyMessage = { "JSON file not found: $fileName" }
    )
        .bufferedReader()
        .use { it.readText() }

private object JsonFileLoader
