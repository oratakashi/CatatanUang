# CatatanUang

CatatanUang adalah aplikasi Android untuk mencatat pemasukan dan pengeluaran secara offline. Project ini dibangun dengan Kotlin + Jetpack Compose, memakai pendekatan Clean Architecture, MVVM, Room, dan Koin.

> Repo ini dibuat dengan **full vibe coding menggunakan GitHub Copilot**.

## ✨ Fitur yang Sudah Ada

- **Catat transaksi** pemasukan dan pengeluaran
- **Edit dan hapus transaksi**
- **Detail transaksi** lengkap
- **Ringkasan saldo** di halaman utama
- **Transaksi terbaru** di home screen
- **Pie chart bulanan** dengan filter periode
- **Laporan harian, mingguan, dan bulanan**
- **Pengaturan bahasa** (Indonesia / English)
- **Pengaturan tema** (System / Light / Dark)
- **Offline-first**, karena data disimpan lokal di perangkat

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

## 📁 Struktur Project

```text
app/src/main/java/com/oratakashi/catatanuang/
├── data/        # Local database, DAO, preferences, repository implementation
├── di/          # Koin module
├── domain/      # Model, repository contract, mapper, use case
├── helpers/     # Helper dan formatter
├── navigation/  # Route dan navigation graph
└── ui/          # Screen, component, ViewModel, theme
```

## 📱 Layar yang Tersedia

- Home
- Tambah transaksi
- Detail transaksi
- Edit transaksi
- Laporan
- Pengaturan

## 🚀 Menjalankan Project

### Prasyarat

- Android Studio versi terbaru
- JDK 17

### Langkah

1. Clone repository:

   ```bash
   git clone https://github.com/oratakashi/CatatanUang.git
   ```

2. Buka project di Android Studio
3. Tunggu Gradle sync selesai
4. Jalankan app di emulator atau device Android

## 📬 Contact

- GitHub: [oratakashi](https://github.com/oratakashi)
- Repository: [oratakashi/CatatanUang](https://github.com/oratakashi/CatatanUang)
