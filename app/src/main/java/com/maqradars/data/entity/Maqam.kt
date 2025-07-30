package com.maqradars.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maqamat")
data class Maqam (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name : String,

    @ColumnInfo(name = "description")
    val description : String,

    @ColumnInfo(name = "audio_path_pure_maqam")
    val audioPathPureMaqam: String,

    @ColumnInfo(name = "is_favorite")
    val isFavorite : Boolean
)
