// app/src/main/java/com/maqradars/data/repository/MaqamRepository.kt

package com.maqradars.data.repository

import com.maqradars.data.dao.AyatExampleDao
import com.maqradars.data.dao.GlosariumTermDao
import com.maqradars.data.dao.MaqamDao
import com.maqradars.data.dao.MaqamVariantDao
import com.maqradars.data.dao.UserDao
import com.maqradars.data.entity.AyatExample
import com.maqradars.data.entity.GlosariumTerm
import com.maqradars.data.entity.Maqam
import com.maqradars.data.entity.MaqamVariant
import com.maqradars.data.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MaqamRepository(
    private val maqamDao: MaqamDao,
    private val maqamVariantDao: MaqamVariantDao,
    private val ayatExampleDao: AyatExampleDao,
    private val glosariumTermDao: GlosariumTermDao,
    private val userDao: UserDao
) {
    val allMaqamat: Flow<List<Maqam>> = maqamDao.getAllMaqamat()
    val allGlosariumTerms: Flow<List<GlosariumTerm>> = glosariumTermDao.getAllGlosariumTerms()

    suspend fun insertMaqam(maqam: Maqam): Long {
        return maqamDao.insertMaqam(maqam)
    }

    suspend fun getMaqamById(maqamId: Long): Maqam? {
        return maqamDao.getMaqamById(maqamId).firstOrNull()
    }

    fun getVariantsByMaqamId(maqamId: Long): Flow<List<MaqamVariant>> {
        return maqamVariantDao.getVariantsByMaqamId(maqamId)
    }
    suspend fun getMaqamVariantById(variantId: Long): MaqamVariant? {
        return maqamVariantDao.getMaqamVariantById(variantId).firstOrNull()
    }


    suspend fun insertMaqamVariant(variant: MaqamVariant): Long {
        return maqamVariantDao.insertMaqamVariant(variant)
    }

    fun getAyatExamplesByMaqamVariantId(maqamVariantId: Long): Flow<List<AyatExample>> {
        return ayatExampleDao.getAyatExamplesByMaqamVariantId(maqamVariantId)
    }

    suspend fun getAyatExampleById(ayatId: Long): AyatExample? {
        return ayatExampleDao.getAyatExampleById(ayatId).firstOrNull()
    }

    suspend fun insertAyatExample(ayat: AyatExample): Long {
        return ayatExampleDao.insertAyatExample(ayat)
    }

    suspend fun insertGlosariumTerm(term: GlosariumTerm) {
        glosariumTermDao.insertGlosariumTerm(term)
    }

    fun getSingleUser(): Flow<User?> {
        return userDao.getSingleUser()
    }

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updateMaqam(maqam: Maqam){
        maqamDao.updateMaqam(maqam)
    }
}