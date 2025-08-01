// app/src/main/java/com/maqradars/data/repository/MaqamRepository.kt

package com.maqradars.data.repository

import com.maqradars.data.dao.AyatExampleDao
import com.maqradars.data.dao.MaqamDao
import com.maqradars.data.dao.MaqamVariantDao
import com.maqradars.data.entity.AyatExample
import com.maqradars.data.entity.Maqam
import com.maqradars.data.entity.MaqamVariant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

// Konstruktor sekarang menerima TIGA DAO
class MaqamRepository(
    private val maqamDao: MaqamDao,
    private val maqamVariantDao: MaqamVariantDao,
    private val ayatExampleDao: AyatExampleDao // <-- Tambahkan DAO ini
) {
    val allMaqamat: Flow<List<Maqam>> = maqamDao.getAllMaqamat()

    suspend fun insertMaqam(maqam: Maqam): Long {
        return maqamDao.insertMaqam(maqam)
    }

    suspend fun getMaqamById(maqamId: Long): Maqam? {
        return maqamDao.getMaqamById(maqamId).firstOrNull()
    }

    // Metode untuk MaqamVariant
    fun getVariantsByMaqamId(maqamId: Long): Flow<List<MaqamVariant>> {
        return maqamVariantDao.getVariantsByMaqamId(maqamId)
    }

    suspend fun insertMaqamVariant(variant: MaqamVariant): Long {
        return maqamVariantDao.insertMaqamVariant(variant)
    }

    // Metode baru untuk AyatExample
    fun getAyatExamplesByMaqamVariantId(maqamVariantId: Long): Flow<List<AyatExample>> {
        return ayatExampleDao.getAyatExamplesByMaqamVariantId(maqamVariantId)
    }

    suspend fun insertAyatExample(ayat: AyatExample): Long {
        return ayatExampleDao.insertAyatExample(ayat)
    }
}