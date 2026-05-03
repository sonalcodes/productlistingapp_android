# Product Listing — Android

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Compose BOM 2024.02.00), Material 3, Navigation Compose 2.7.7, Activity Compose
- **Architecture:** MVVM, Repository pattern, `StateFlow`, `viewModelScope`
- **DI:** Hilt 2.50, `hilt-navigation-compose`
- **Networking:** Retrofit 2.9.0, Gson converter, OkHttp 4.12.0 (logging interceptor in debug)
- **Images:** Coil Compose 2.6.0
- **Async:** Kotlinx Coroutines 1.7.3
- **Lifecycle:** AndroidX Lifecycle 2.7.0 (ViewModel Compose, Runtime Compose)
- **Pull to refresh:** Accompanist SwipeRefresh 0.34.0
- **Core:** AndroidX Core KTX

## Architecture

MVVM with a single source of truth in ViewModels, a repository abstraction over the Retrofit API, and Hilt for constructor injection of network and data dependencies.

## Setup

1. Clone this repository.
2. Open the project in **Android Studio** (Ladybug or newer recommended).
3. Let Gradle sync finish, then run the **app** configuration on an emulator or physical device (API 24+).

## Screens

- **List:** Fetches products from FakeStore, search filter, pull-to-refresh, loading and error states with retry.
- **Detail:** Product image, price, title, description, rating summary, and a stub “Add to Favorites” action with snackbar feedback.

## Bonus Features
- **Search** — real-time filter by title and category via `combine` on `StateFlow`
- **Pull to Refresh** — native M3 `PullToRefreshBox` (Material3 1.4.0, zero deprecated APIs)
- **Favorites (stub)** — "Add to Favorites" button on detail screen shows a Snackbar confirmation

## Approach

The app uses a `Flow<Result<T>>` from the repository so all network failures are caught before they reach ViewModels; ViewModels map failures to UI states and log with the `ProductApp` tag. Hilt modules provide a single `Retrofit`/`ApiService` graph while `RetrofitInstance` centralizes base URL, timeouts, and Gson configuration. The UI is dark-only with a warm neutral palette and DM Sans via downloadable Google Fonts for a consistent, readable hierarchy.
