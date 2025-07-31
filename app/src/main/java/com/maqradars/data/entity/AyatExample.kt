package com.maqradars.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ayat_examples",
    foreignKeys = [
        ForeignKey(
            entity = Maqam::class,
            parentColumns = ["id"],
            childColumns = ["maqam_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class AyatExample (
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "maqam_id")
    val maqamId: Long,

    @ColumnInfo(name = "surah_number")
    val surahNumber: Int,

    @ColumnInfo(name = "ayat_number")
    val ayatNumber: Int,

    @ColumnInfo(name = "arabic_text")
    val arabicText: String,

    @ColumnInfo(name = "translation_text")
    val translationText: String?,

    @ColumnInfo(name = "audio_path_ayat_example")
    val audioPathAyatExample: String
)