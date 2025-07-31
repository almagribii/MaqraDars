package com.maqradars.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maqradars.data.entity.Maqam
import kotlinx.coroutines.flow.Flow

@Dao
interface MaqamDao {
    @Insert
    suspend fun insertMaqam(maqam: Maqam) : Long

    @Update
    suspend fun updateMaqam(maqam: Maqam)

    @Delete
    suspend fun deleteMaqam(maqam: Maqam)

    @Query("SELECT * FROM maqamat WHERE id = :maqamId")
    fun getMaqamById(maqamId: Long): Flow<List<Maqam>>

    @Query("SELECT * FROM maqamat ORDER BY name ASC")
    fun getAllMaqamat() : Flow<List<Maqam>>

    @Query("SELECT * FROM maqamat WHERE is_favorite = 1 ORDER BY name ASC")
    fun getFavoriteMaqamat() : Flow<List<Maqam>>
}