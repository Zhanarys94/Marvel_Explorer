package kz.zhanarys.marvelexplorer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.zhanarys.domain.useCases.AddDeleteFavoritesUseCase
import kz.zhanarys.domain.useCases.FavoritesListUseCase
import kz.zhanarys.domain.useCases.SearchForCharacterUseCase
import kz.zhanarys.domain.useCases.UpdateMainListUseCase
import javax.inject.Inject

class SharedViewModelFactory @Inject constructor(
    private val updateMainListUseCase: UpdateMainListUseCase,
    private val addDeleteFavoritesUseCase: AddDeleteFavoritesUseCase,
    private val searchForCharacterUseCase: SearchForCharacterUseCase,
    private val favoritesListUseCase: FavoritesListUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel(
                updateMainListUseCase,
                addDeleteFavoritesUseCase,
                searchForCharacterUseCase,
                favoritesListUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}