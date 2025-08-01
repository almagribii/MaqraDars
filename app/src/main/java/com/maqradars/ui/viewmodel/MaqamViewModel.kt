// app/src/main/java/com/maqradars/ui/viewmodel/MaqamViewModel.kt

package com.maqradars.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maqradars.data.entity.AyatExample
import com.maqradars.data.entity.GlosariumTerm
import com.maqradars.data.entity.Maqam
import com.maqradars.data.entity.MaqamVariant
import com.maqradars.data.repository.MaqamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MaqamViewModel(private val repository: MaqamRepository) : ViewModel() {

    // Metode untuk Maqam
    val allMaqamat: Flow<List<Maqam>> = repository.allMaqamat
    val allGlosariumTerms: Flow<List<GlosariumTerm>> = repository.allGlosariumTerms


    suspend fun getMaqamById(maqamId: Long): Maqam? {
        return repository.getMaqamById(maqamId)
    }

    fun insertMaqam(maqam: Maqam) {
        viewModelScope.launch {
            repository.insertMaqam(maqam)
        }
    }

    // Metode untuk MaqamVariant
    fun getVariantsByMaqamId(maqamId: Long): Flow<List<MaqamVariant>> {
        return repository.getVariantsByMaqamId(maqamId)
    }

    fun insertMaqamVariant(variant: MaqamVariant) {
        viewModelScope.launch {
            repository.insertMaqamVariant(variant)
        }
    }

    // Metode untuk AyatExample
    fun getAyatExamplesByMaqamVariantId(maqamVariantId: Long): Flow<List<AyatExample>> {
        return repository.getAyatExamplesByMaqamVariantId(maqamVariantId)
    }

    fun insertAyatExample(ayat: AyatExample) {
        viewModelScope.launch {
            repository.insertAyatExample(ayat)
        }
    }
}