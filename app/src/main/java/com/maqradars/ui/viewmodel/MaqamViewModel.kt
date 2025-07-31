package com.maqradars.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maqradars.data.entity.Maqam
import com.maqradars.data.repository.MaqamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MaqamViewModel(private val repository: MaqamRepository) : ViewModel() {
    val allMaqamat: Flow<List<Maqam>> = repository.allMaqamat
    val favoriteMaqamat: Flow<List<Maqam>> = repository.favoritMaqamat

    fun insertMaqam(maqam: Maqam) {
        viewModelScope.launch {
            repository.insert(maqam)
        }
    }

    fun updateMaqam(maqam: Maqam) {
        viewModelScope.launch {
            repository.update(maqam)
        }
    }

    fun deleteMaqam(maqam: Maqam) {
        viewModelScope.launch {
            repository.delete(maqam)
        }
    }
}