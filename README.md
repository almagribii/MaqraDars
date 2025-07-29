# Dokumentasi Aplikasi MaqraDars

## 1. Pendahuluan

**MaqraDars** adalah aplikasi edukasi berbasis Android yang didedikasikan untuk membantu pengguna mempelajari dan menguasai **Maqamat** (tangga nada/irama) dalam pembacaan Al-Quran. Aplikasi ini bertujuan untuk menyediakan sumber belajar yang komprehensif dan interaktif secara *offline*, memungkinkan pengguna memahami nuansa melodi Al-Quran dan menerapkannya dalam bacaan mereka.

## 2. Tujuan Aplikasi

* **Mengenalkan Maqamat:** Menyajikan berbagai jenis Maqamat Al-Quran beserta karakteristik dasarnya.
* **Pembelajaran Interaktif:** Menyediakan contoh audio Maqam murni dan contoh pembacaan ayat Al-Quran dalam Maqam tertentu.
* **Akses Offline:** Memungkinkan pengguna belajar kapan saja dan di mana saja tanpa memerlukan koneksi internet aktif.
* **Melacak Progres:** Membantu pengguna memantau Maqam yang sedang dipelajari atau yang telah dikuasai (fitur favorit).
* **Meningkatkan Kualitas Bacaan:** Memberdayakan pengguna untuk melantunkan Al-Quran dengan irama yang lebih indah dan sesuai.

## 3. Fitur Utama

* **Daftar Maqamat:** Menampilkan daftar lengkap jenis-jenis Maqamat (Bayati, Hijaz, Nahawand, Rast, Sika, Saba, Jiharkah, dll.) dengan deskripsi singkat masing-masing.
* **Detail Maqam:** Halaman detail untuk setiap Maqam yang meliputi:
    * Nama dan penjelasan karakteristik Maqam.
    * **Audio Contoh Maqam Murni:** Melodi Maqam tanpa teks ayat.
    * **Audio Contoh Ayat Al-Quran:** Ayat-ayat pilihan yang dibacakan menggunakan Maqam tersebut, disertai teks Arab.
* **Fitur Favorit:** Pengguna dapat menandai Maqam atau contoh ayat tertentu sebagai favorit untuk akses cepat dan revisi.
* **Glosarium (Opsional):** Daftar istilah penting terkait Tajwid dan Maqamat dengan definisinya.

## 4. Tech Stack

Aplikasi MaqraDars akan dibangun dengan menggunakan teknologi modern dan direkomendasikan untuk pengembangan aplikasi Android:

* **Bahasa Pemrograman:** **Kotlin**
    * Pilihan utama Google untuk Android, menawarkan keamanan null, sintaksis yang ringkas, dan interoperabilitas penuh dengan Java.
* **Lingkungan Pengembangan (IDE):** **Android Studio**
    * IDE resmi dan terlengkap untuk pengembangan Android, menyediakan fitur debugging, profiling, dan layout editing yang canggih.
* **Database Lokal:** **Room Persistence Library (di atas SQLite)**
    * **Room** adalah *library* abstraksi yang direkomendasikan Google di atas SQLite. Ini menyederhanakan interaksi database, menyediakan keamanan tipe saat kompilasi, dan mengurangi kode boilerplate.
* **UI Framework:** **Android Jetpack Compose**
    * Toolkit UI deklaratif modern yang memungkinkan pembangunan antarmuka pengguna yang lebih cepat, lebih intuitif, dan reaktif.
* **Komponen Arsitektur:** **Android Jetpack Components**
    * **ViewModel:** Untuk mengelola data terkait UI dan bertahan melalui perubahan konfigurasi.
    * **LiveData/Flow:** Untuk observasi data reaktif yang *lifecycle-aware*.
    * **Navigation Component:** Untuk mengelola navigasi antar layar aplikasi.
* **Manajemen Data Asinkron:** **Kotlin Coroutines**
    * Digunakan untuk menjalankan operasi I/O (seperti interaksi database) di *background thread* untuk menjaga UI tetap responsif.
* **Sistem Build:** **Gradle**
    * Sistem *build* standar Android untuk mengelola dependensi dan otomatisasi tugas.
