package com.maqradars.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maqradars.data.entity.GlosariumTerm
import kotlinx.coroutines.flow.Flow

@Dao
interface GlosariumTermDao {
    @Insert
    suspend fun insertGlosariumTerm(term: GlosariumTerm): Long

    @Update
    suspend fun updateGlosariumTermI(term: GlosariumTerm)

    @Delete
    suspend fun deleteGlosariumTerm(term: GlosariumTerm)

    @Query("SELECT * FROM glosarium_terms WHERE id = :termId")
    fun getGlosariumTermById(termId: Long): Flow<GlosariumTerm?>

    @Query("SELECT * FROM glosarium_terms ORDER BY term ASC")
    fun getAllGlosariumTerms(): Flow<List<GlosariumTerm>>

    @Query("SELECT * FROM glosarium_terms WHERE term LIKE '%' || :query || '%' ORDER BY term ASC")
    fun searchGlosariumTerms(query: String): Flow<List<GlosariumTerm>>
}