package com.maqradars.data.repository

import com.maqradars.data.dao.MaqamDao
import com.maqradars.data.entity.Maqam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MaqamRepository (private val maqamDao: MaqamDao){
    val allMaqamat: Flow<List<Maqam>> = maqamDao.getAllMaqamat()
    val favoritMaqamat: Flow<List<Maqam>> = maqamDao.getFavoriteMaqamat()

    suspend fun insert(maqam: Maqam){
        maqamDao.insertMaqam(maqam)
    }

    suspend fun update(maqam: Maqam) {
        maqamDao.updateMaqam(maqam)
    }

    suspend fun delete(maqam: Maqam) {
        maqamDao.deleteMaqam(maqam)
    }

    suspend fun getMaqamById(maqamId: Long): Maqam? {
        return maqamDao.getMaqamById(maqamId).firstOrNull() // Menggunakan firstOrNull()
    }
}