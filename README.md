# CatatanUang

CatatanUang is an offline Android application for tracking income and expenses. The project is built with Kotlin and Jetpack Compose, using Clean Architecture, MVVM, Room, and Koin.

> This repository was created through **full vibe coding with GitHub Copilot**.

## ✨ Current Features

- **Transaction recording** for both income and expenses
- **Transaction editing and deletion**
- **Detailed transaction view**
- **Balance summary** on the home screen
- **Recent transactions** displayed on the home screen
- **Monthly pie chart** with period filtering
- **Daily, weekly, and monthly reports**
- **Language preferences** (Indonesian / English)
- **Theme preferences** (System / Light / Dark)
- **Offline-first** data storage on the device

## 🧱 Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Koin
- **Local Database**: Room
- **Preferences**: DataStore
- **Navigation**: Navigation Compose
- **Charts**: Donut Compose
- **Async**: Kotlin Coroutines & Flow
- **Build System**: Gradle Kotlin DSL

## 📁 Project Structure

```text
app/src/main/java/com/oratakashi/catatanuang/
├── data/        # Local database, DAO, preferences, and repository implementation
├── di/          # Koin modules
├── domain/      # Models, repository contracts, mappers, and use cases
├── helpers/     # Shared helpers and formatters
├── navigation/  # Routes and navigation graph
└── ui/          # Screens, components, ViewModels, and theme
```

## 📱 Available Screens

- Home
- Add transaction
- Transaction detail
- Edit transaction
- Report
- Settings

## 🚀 Running the Project

### Prerequisites

- Android Studio (latest stable version recommended)
- JDK 17

### Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/oratakashi/CatatanUang.git
   ```

2. Open the project in Android Studio
3. Wait for Gradle sync to complete
4. Run the app on an emulator or Android device

## 📬 Contact

- GitHub: [oratakashi](https://github.com/oratakashi)
- Repository: [oratakashi/CatatanUang](https://github.com/oratakashi/CatatanUang)
