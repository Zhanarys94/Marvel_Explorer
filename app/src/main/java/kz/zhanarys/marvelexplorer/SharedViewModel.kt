package kz.zhanarys.marvelexplorer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kz.zhanarys.domain.useCases.UpdateMainListUseCase
import kz.zhanarys.domain.ViewState
import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.domain.useCases.AddDeleteFavoritesUseCase
import kz.zhanarys.domain.useCases.FavoritesListUseCase
import kz.zhanarys.domain.useCases.SearchForCharacterUseCase
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val updateMainListUseCase: UpdateMainListUseCase,
    private val addDeleteFavoritesUseCase: AddDeleteFavoritesUseCase,
    private val searchForCharacterUseCase: SearchForCharacterUseCase,
    private val favoritesListUseCase: FavoritesListUseCase
) : ViewModel() {

    private var limit = 20
    private var currentOffset = 0

    private val _isLoadingMutableLiveData = MutableLiveData(false)
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingMutableLiveData

    private val _currentScreenMutableLiveData = MutableLiveData(ViewState.MAIN)
    val currentScreenLiveData: LiveData<ViewState> = _currentScreenMutableLiveData

    private val _charactersListMutableLiveData = MutableLiveData<List<CharacterItemModel>>()
    val charactersListLiveData: LiveData<List<CharacterItemModel>> = _charactersListMutableLiveData

    private val _favoritesListMutableLiveData = MutableLiveData<List<CharacterItemModel>>()
    val favoritesListLiveData: LiveData<List<CharacterItemModel>> = _favoritesListMutableLiveData

    init {
        currentOffset = 0
        viewModelScope.launch {
            val data = updateMainListUseCase.getAllCharacters(currentOffset, limit)
            _charactersListMutableLiveData.value = data
        }
    }

    fun updateMainListData() {
        currentOffset = 0
        viewModelScope.launch {
            val data = updateMainListUseCase.getAllCharacters(currentOffset, limit)
            _charactersListMutableLiveData.value = data
        }
    }

    fun fetchMoreData() {
        viewModelScope.launch {
            currentOffset += limit
            val data = updateMainListUseCase.getAllCharacters(currentOffset, limit)
            _charactersListMutableLiveData.value = _charactersListMutableLiveData.value.orEmpty() + data
        }
    }

    fun fetchMoreData(searchText: String) {
        viewModelScope.launch {
            currentOffset += limit
            val data = searchForCharacterUseCase.getCharacterByNameStartingWith(searchText, currentOffset, limit)
            _charactersListMutableLiveData.value = _charactersListMutableLiveData.value.orEmpty() + data
        }
    }

    fun searchForCharacterByNameStartingWith(searchText: String) {
        currentOffset = 0
        viewModelScope.launch {
            val data = searchForCharacterUseCase.getCharacterByNameStartingWith(searchText, currentOffset, limit)
            _charactersListMutableLiveData.value = data
        }
    }

    fun addToFavorites(id: Int) {
        viewModelScope.launch {
            addDeleteFavoritesUseCase.addToFav(id)
        }
    }

    fun removeFromFavorites(id: Int) {
        viewModelScope.launch {
            addDeleteFavoritesUseCase.removeFromFav(id)
            val data = favoritesListUseCase.getFavoritesList()
            _favoritesListMutableLiveData.value = data
        }
    }

    fun goToFavoritesList() {
        viewModelScope.launch {
            val data = favoritesListUseCase.getFavoritesList()
            _favoritesListMutableLiveData.value = data
        }
        _currentScreenMutableLiveData.value = ViewState.FAVORITES
    }

    fun searchForCharacterByNameStartingWithInDb(searchText: String) {
        viewModelScope.launch {
            val data = searchForCharacterUseCase.getCharacterByNameStartingWithFromLocalDB(searchText)
            _favoritesListMutableLiveData.value = data
        }
    }

    fun updateCharacterById(id: Int) {
        viewModelScope.launch {
            val list = _charactersListMutableLiveData.value!!.toMutableList()
            val index = _charactersListMutableLiveData.value!!.indexOfFirst { it.id == id }
            val character = _charactersListMutableLiveData.value!![index]
            val updatedCharacter = character.copy(isFavorite = !character.isFavorite)
            list[index] = updatedCharacter
            _charactersListMutableLiveData.value = list.toList()
        }
    }

    suspend fun getCharacterById(id: Int): CharacterEntityModel {
        return searchForCharacterUseCase.getCharacterById(id)
    }

    suspend fun getCharacterByIdFromDb(id: Int): CharacterEntityModel {
        return viewModelScope.async {
            favoritesListUseCase.getCharacterById(id)
        }.await()
    }
}