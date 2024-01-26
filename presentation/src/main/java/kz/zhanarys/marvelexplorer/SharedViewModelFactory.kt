package kz.zhanarys.marvelexplorer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.zhanarys.data.HeroesDatabase

class SharedViewModelFactory(private val heroesDatabase: HeroesDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel(heroesDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}