package com.maqradars.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maqradars.data.entity.AyatExample
import kotlinx.coroutines.flow.Flow

@Dao
interface AyatExampleDao {
    @Insert
    suspend fun insertAyatExample(ayatExample: AyatExample) : Long

    @Update
    suspend fun updateAyatExample(ayatExample: AyatExample)

    @Delete
    suspend fun deleteAyatExample(ayatExample: AyatExample)

    @Query("SELECT * FROM ayat_examples WHERE id = :ayatExampleId")
    fun getAyatById(ayatExampleId: Long) : Flow <AyatExample?>

    @Query("SELECT * FROM ayat_examples WHERE maqam_id = :maqamId ORDER BY surah_number")
    fun getAyatExamplesByMaqamId(maqamId : Long) : Flow<List<AyatExample>>

    @Query("DELETE FROM ayat_examples WHERE id = :ayatExampleId")
    suspend fun deleteAyatExampleById(ayatExampleId: Long)
}