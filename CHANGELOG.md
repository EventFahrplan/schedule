# Schedule changelog

## NEXT

* Not published yet.

### Changes

* `com.squareup.okhttp3:okhttp:5.4.0` - compileSdk 37 is required as of 5.5.0
* `org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.20`
* `org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0`
* `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0`
* `org.threeten:threetenbp:1.7.4`
* Move inline JSON fixtures for schedule v1 into files.
* Restructure schedule v1 related files and classes to add schedule version 2.
* Fix showing exact line of failure in `ProductionApiTest`.


## [v.2.0.0](https://github.com/EventFahrplan/schedule/releases/tag/v.2.0.0)

* Published: 2026-05-27

### Changes

* **Breaking changes:**
  * Fix sending empty header fields to which the webserver responds with HTTP 400.
  * Support logging to be injected from app.
  * Skip events missing mandatory fields.


## [v.1.0.0](https://github.com/EventFahrplan/schedule/releases/tag/v.1.0.0)

* Published: 2026-04-05

### Changes

* This is the initial release. Have fun!
