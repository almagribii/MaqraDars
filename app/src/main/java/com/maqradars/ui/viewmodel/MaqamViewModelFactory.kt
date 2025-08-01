// app/src/main/java/com/maqradars/ui/viewmodel/MaqamViewModelFactory.kt

package com.maqradars.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maqradars.data.repository.MaqamRepository

class MaqamViewModelFactory(private val repository: MaqamRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MaqamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MaqamViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}