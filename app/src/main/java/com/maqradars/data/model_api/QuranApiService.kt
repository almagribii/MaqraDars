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

data class Surat(
    val nomor: Int,
    val nama: String,
    val namaLatin: String,
    val jumlah_ayat: Int,
    val tempat_turun: String,
    val arti: String
)

data class SuratResponse(
    val data: List<Surat>
)

data class Ayat(
    val nomorAyat: Int,
    val teksArab: String,
    val teksLatin: String,
    val teksIndonesia: String,
    val audio: Map<String, String>
)

data class DetailSurat(
    val nomor: Int,
    val nama: String,
    val namaLatin: String,
    val tempatTurun: String,
    val arti: String,
    val deskripsi: String,
    val audioFull: Map<String, String>,
    val ayat: List<Ayat>,
    val suratSelanjutnya: Surat? = null,
    val suratSebelumnya: Any
)

data class DetailSuratResponse(
    val data: DetailSurat
)