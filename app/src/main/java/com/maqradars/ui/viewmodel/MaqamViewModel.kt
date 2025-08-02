// app/src/main/java/com/maqradars/ui/viewmodel/MaqamViewModel.kt

package com.maqradars.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maqradars.data.entity.AyatExample
import com.maqradars.data.entity.GlosariumTerm
import com.maqradars.data.entity.Maqam
import com.maqradars.data.entity.MaqamVariant
import com.maqradars.data.entity.User
import com.maqradars.data.repository.MaqamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MaqamViewModel(private val repository: MaqamRepository) : ViewModel() {

    val allMaqamat: Flow<List<Maqam>> = repository.allMaqamat

    fun getVariantsByMaqamId(maqamId: Long): Flow<List<MaqamVariant>> {
        return repository.getVariantsByMaqamId(maqamId)
    }

    fun getAyatExamplesByMaqamVariantId(maqamVariantId: Long): Flow<List<AyatExample>> {
        return repository.getAyatExamplesByMaqamVariantId(maqamVariantId)
    }

    suspend fun getMaqamById(maqamId: Long): Maqam? {
        return repository.getMaqamById(maqamId)
    }

    suspend fun getMaqamVariantById(variantId: Long): MaqamVariant? {
        return repository.getMaqamVariantById(variantId)
    }

    suspend fun insertMaqam(maqam: Maqam): Long {
        return repository.insertMaqam(maqam)
    }

    val allGlosariumTerms: Flow<List<GlosariumTerm>> = repository.allGlosariumTerms

    val user: Flow<User?> = repository.getSingleUser()

    // --- FUNGSI UPDATE YANG SUDAH DIPERBAIKI ---
    fun updateIsDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            val user = repository.getSingleUser().firstOrNull() // Mengambil data sekali
            if (user != null) {
                val updatedUser = user.copy(isDarkMode = isDarkMode)
                repository.updateUser(updatedUser)
            }
        }
    }

    // --- AKHIR FUNGSI UPDATE YANG DIPERBAIKI ---

    suspend fun insertMaqamVariant(variant: MaqamVariant): Long {
        return repository.insertMaqamVariant(variant)
    }

    suspend fun insertAyatExample(ayat: AyatExample): Long {
        return repository.insertAyatExample(ayat)
    }

    suspend fun insertGlosariumTerm(term: GlosariumTerm) {
        // Implementasi opsional
    }

    fun toggleFavorite(maqamId: Long) {
        viewModelScope.launch {
            val maqam = repository.getMaqamById(maqamId)
            if (maqam != null) {
                val updatedMaqam = maqam.copy(isFavorite = !maqam.isFavorite)
                repository.updateMaqam(updatedMaqam)
            }
        }
    }
}