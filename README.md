# APOD Explorer App (apod-android)
Android App for exploring NASA's "Astronomy Picture of the Day" API. A learning exercise and fun project for me.

# Architecture
  - Following [Android Architecture Recommendations](https://developer.android.com/topic/architecture/recommendations)

### 🖼️ UI
      
The app contains a simple Compose UI that shows:
 - A list of random images returned from [NASA's APOD API](https://github.com/nasa/apod-api)
with title and dates that they were the APOD.
   - The list is sortable by Title or Date, changed via Floating Action Button.
   - The list can be refreshed using pull-to-refresh to get a new random list from the API.

      
### 🧱 Build

* [KTS gradle files](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
* [Version catalog](https://docs.gradle.org/current/userguide/platforms.html)

### 🏠 Architecture

* [Room Database](https://developer.android.com/training/data-storage/room)
* Dependency injection with [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [Jetpack ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
* UI using [Jetpack Compose](https://developer.android.com/jetpack/compose) and
[Material3](https://developer.android.com/jetpack/androidx/releases/compose-material3)
* [Jetpack Navigation](https://developer.android.com/jetpack/compose/navigation)
* [Reactive data layer](https://developer.android.com/topic/architecture/data-layer)
* [Kotlin Coroutines and Flow](https://developer.android.com/kotlin/coroutines)
* [Unit tests](https://developer.android.com/training/testing/local-tests)
* [UI tests](https://developer.android.com/jetpack/compose/testing) using fake data with
[Hilt](https://developer.android.com/training/dependency-injection/hilt-testing)

# Author <a name="author"></a>
- Trevor Crawford 

# License

- This code is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.
- Based on template code from [Android Architecture Templates](https://github.com/android/architecture-templates)
- See [notice](NOTICE.md) for attribution of any Libraries used in this project.
