package kz.zhanarys.marvelexplorer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.zhanarys.domain.features.MarvelRepoController
import javax.inject.Inject

class SharedViewModelFactory @Inject constructor(private val marvelRepoController: MarvelRepoController): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel(marvelRepoController) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}