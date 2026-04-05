[![Build](https://github.com/EventFahrplan/schedule/actions/workflows/build.yaml/badge.svg)](https://github.com/EventFahrplan/schedule/actions/workflows/build.yaml) [![Maven Central](https://img.shields.io/maven-central/v/info.metadude.kotlin.library.schedule/schedule-repositories.svg?label=Maven%20Central)](https://central.sonatype.com/search?q=info.metadude.kotlin.library.schedule) [![Apache License](http://img.shields.io/badge/license-Apache%20License%202.0-lightgrey.svg)](http://choosealicense.com/licenses/apache-2.0/)

# Schedule library

A Kotlin library containing a parser and models for Frab-compatible conference schedule JSON (version 1), as used by apps such as:

* https://github.com/EventFahrplan/EventFahrplan


## Usage

The library is published as two separate artifacts: `schedule-base` and `schedule-repositories`.
You can use either of them depending on your needs.

### Usage of `schedule-base`

The `schedule-base` artifact returns a `Response<ScheduleV1>` type
from the suspending `ScheduleService#getScheduleV1` function.

```kotlin
suspend fun loadSchedule(okHttpClient: Call.Factory) {
    val api: ScheduleApi = Api
    val service: ScheduleService = api.provideScheduleService(
        baseUrl = "https://some.event.com/schedules/",
        callFactory = okHttpClient,
    )

    val requestETag = "" // Pass an empty string or a previous ETag value for caching
    val requestLastModifiedAt = "" // Pass an empty string or a previous Last-Modified value for caching
    val response = service.getScheduleV1(
        eTag = requestETag,
        lastModifiedAt = requestLastModifiedAt,
        path = "schedule.json",
    )

    if (response.isSuccessful) {
        val scheduleV1 = response.body()
        val responseETag = response.headers()["ETag"]
        val responseLastModifiedAt = response.headers()["Last-Modified"]
    } else {
        val errorCode = response.code()
        val errorMessage = response.message()
    }
}
```

### Usage of `schedule-repositories`

The `schedule-repositories` artifact returns a `Flow<GetScheduleV1State>` type
from the suspending `ScheduleRepository#getScheduleV1State` function.

```kotlin
suspend fun loadSchedule(okHttpClient: Call.Factory) {
    val repository: ScheduleRepository = SimpleScheduleRepository(
        callFactory = okHttpClient,
        api = Api,
    )

    val requestETag = "" // Pass an empty string or a previous ETag value for caching
    val requestLastModifiedAt = "" // Pass an empty string or a previous Last-Modified value for caching
    repository.getScheduleV1State(
        url = "https://some.event.com/schedules/schedule.json",
        requestETag = requestETag,
        lastModifiedAt = requestLastModifiedAt,
    ).collectLatest { state ->
        when (state) {
            is Success -> {
                val scheduleV1 = state.scheduleV1
                val responseETag = state.responseETag
                val responseLastModifiedAt = state.responseLastModifiedAt
            }
            is Error -> {
                val httpStatusCode = state.httpStatusCode
                val errorMessage = state.errorMessage
            }
            is Failure -> {
                val throwable = state.throwable
            }
        }
    }
}
```


## Gradle build

To deploy the library to your local Maven repository run the following task:

```bash
$ ./gradlew publishToMavenLocal
```

Then, to use the library in your project add the following to
your top level `build.gradle`:

```groovy
allprojects {
    repositories {
        mavenLocal()
    }
}
```

and one of the following dependencies to your application module `build.gradle`:


```groovy
dependencies {
    implementation "info.metadude.kotlin.library.schedule:schedule-base:$version"
    implementation "info.metadude.kotlin.library.schedule:schedule-repositories:$version"
}
```


## Tests

Run the following command to execute all tests:

```bash
$ ./gradlew clean test
```

## Author

* [Tobias Preuss][tobias-preuss]

## License

    Copyright 2026 Tobias Preuss

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[tobias-preuss]: https://github.com/johnjohndoe
