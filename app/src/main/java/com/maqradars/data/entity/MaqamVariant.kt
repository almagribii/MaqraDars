
package com.maqradars.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "maqam_variants",
    foreignKeys = [
        ForeignKey(
            entity = Maqam::class,
            parentColumns = ["id"],
            childColumns = ["maqam_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MaqamVariant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "maqam_id")
    val maqamId: Long,
    @ColumnInfo(name = "variant_name")
    val variantName: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "audio_path")
    val audioPath: String
)