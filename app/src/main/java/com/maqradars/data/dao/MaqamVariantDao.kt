// app/src/main/java/com/maqradars/data/dao/MaqamVariantDao.kt

package com.maqradars.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maqradars.data.entity.MaqamVariant
import kotlinx.coroutines.flow.Flow

@Dao
interface MaqamVariantDao {
    @Insert
    suspend fun insertMaqamVariant(variant: MaqamVariant): Long

    @Update
    suspend fun updateMaqamVariant(variant: MaqamVariant)

    @Query("SELECT * FROM maqam_variants WHERE maqam_id = :maqamId ORDER BY variant_name ASC")
    fun getVariantsByMaqamId(maqamId: Long): Flow<List<MaqamVariant>>

    @Query("SELECT * FROM maqam_variants WHERE id = :variantId LIMIT 1")
    fun getMaqamVariantById(variantId: Long): Flow<MaqamVariant?>
}