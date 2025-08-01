// app/src/main/java/com/maqradars/data/dao/AyatExampleDao.kt

package com.maqradars.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maqradars.data.entity.AyatExample
import kotlinx.coroutines.flow.Flow

@Dao
interface AyatExampleDao {
    @Insert
    suspend fun insertAyatExample(ayatExample: AyatExample): Long

    @Update
    suspend fun updateAyatExample(ayatExample: AyatExample)

    @Query("SELECT * FROM ayat_examples WHERE maqam_variant_id = :maqamVariantId ORDER BY surah_number ASC, ayat_number ASC")
    fun getAyatExamplesByMaqamVariantId(maqamVariantId: Long): Flow<List<AyatExample>>

    @Query("SELECT * FROM ayat_examples WHERE id = :ayatExampleId LIMIT 1")
    fun getAyatExampleById(ayatExampleId: Long): Flow<AyatExample?>
}