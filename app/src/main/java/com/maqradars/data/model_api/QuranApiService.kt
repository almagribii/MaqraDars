package com.maqradars.data.model_api


import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApiService {

    @GET("surat")
    suspend fun getDaftarSurat(): SuratResponse

    @GET("surat/{nomor}")
    suspend fun getDetailSurat(
        @Path("nomor") nomorSurat: Int
    ): DetailSuratResponse
}

// Data yang mewakili satu surat
data class Surat(
    val nomor: Int,
    val nama: String,
    val namaLatin: String,
    val jumlah_ayat: Int,
    val tempat_turun: String,
    val arti: String
)

// Data yang mewakili respons dari API
data class SuratResponse(
    val data: List<Surat>
)

// Data yang mewakili satu ayat
data class Ayat(
    val nomorAyat: Int, // Nama properti diubah dari 'nomor' menjadi 'nomorAyat'
    val teksArab: String, // Nama properti diubah dari 'arab' menjadi 'teksArab'
    val teksLatin: String, // Nama properti diubah dari 'tr' menjadi 'teksLatin'
    val teksIndonesia: String, // Nama properti diubah dari 'id' menjadi 'teksIndonesia'
    val audio: Map<String, String>
)

// Data yang mewakili detail surat
data class DetailSurat(
    val nomor: Int,
    val nama: String,
    val namaLatin: String, // Nama properti diubah dari 'nama_latin' menjadi 'namaLatin'
    val tempatTurun: String, // Nama properti diubah dari 'tempat_turun' menjadi 'tempatTurun'
    val arti: String,
    val deskripsi: String,
    val audioFull: Map<String, String>,
    val ayat: List<Ayat>,
    val suratSelanjutnya: Surat? = null,
    val suratSebelumnya: Any // Tipe data diubah karena bisa false atau objek
)

// Data yang mewakili respons dari API
data class DetailSuratResponse(
    val data: DetailSurat
)