// app/src/main/java/com/maqradars/data/repository/MaqamRepository.kt

package com.maqradars.data.repository

import com.maqradars.data.dao.MaqamDao
import com.maqradars.data.dao.MaqamVariantDao
import com.maqradars.data.entity.Maqam
import com.maqradars.data.entity.MaqamVariant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

// Konstruktor sekarang menerima dua DAO
class MaqamRepository(
    private val maqamDao: MaqamDao,
    private val maqamVariantDao: MaqamVariantDao
) {
    val allMaqamat: Flow<List<Maqam>> = maqamDao.getAllMaqamat()

    suspend fun insertMaqam(maqam: Maqam): Long {
        return maqamDao.insertMaqam(maqam)
    }

    suspend fun getMaqamById(maqamId: Long): Maqam? {
        return maqamDao.getMaqamById(maqamId).firstOrNull()
    }

    // Metode baru untuk MaqamVariant
    fun getVariantsByMaqamId(maqamId: Long): Flow<List<MaqamVariant>> {
        return maqamVariantDao.getVariantsByMaqamId(maqamId)
    }

    suspend fun getMaqamVariantById(variantId: Long): MaqamVariant? {
        return maqamVariantDao.getMaqamVariantById(variantId).firstOrNull()
    }

    suspend fun insertMaqamVariant(variant: MaqamVariant): Long {
        return maqamVariantDao.insertMaqamVariant(variant)
    }
}