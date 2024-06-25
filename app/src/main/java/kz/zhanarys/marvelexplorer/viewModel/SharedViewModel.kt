package kz.zhanarys.marvelexplorer.viewModel

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.zhanarys.domain.useCases.UpdateMainListUseCase
import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.domain.models.ComicItemModel
import kz.zhanarys.domain.useCases.AddDeleteFavoritesUseCase
import kz.zhanarys.domain.useCases.CharacterDetailsUseCase
import kz.zhanarys.domain.useCases.FavoritesListUseCase
import kz.zhanarys.domain.useCases.SearchForCharacterUseCase
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val updateMainListUseCase: UpdateMainListUseCase,
    private val addDeleteFavoritesUseCase: AddDeleteFavoritesUseCase,
    private val searchForCharacterUseCase: SearchForCharacterUseCase,
    private val favoritesListUseCase: FavoritesListUseCase,
    private val characterDetailsUseCase: CharacterDetailsUseCase,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _internetConnection = MutableLiveData<Boolean>()
    val internetConnection: LiveData<Boolean> = _internetConnection

    private var currentOffset = 0
    private var limit = 20

    private var currentOffsetComics = 0
    private var limitComics = 20

    private val _currentCharacterMutableLiveData = MutableLiveData<CharacterEntityModel>()
    val currentCharacterLiveData: LiveData<CharacterEntityModel> = _currentCharacterMutableLiveData

    private val _charactersListMutableLiveData = MutableLiveData<List<CharacterItemModel>>()
    val charactersListLiveData: LiveData<List<CharacterItemModel>> = _charactersListMutableLiveData

    private val _favoritesListMutableLiveData = MutableLiveData<List<CharacterItemModel>>()
    val favoritesListLiveData: LiveData<List<CharacterItemModel>> = _favoritesListMutableLiveData

    private val _comicsListMutableLiveData = MutableLiveData<List<ComicItemModel>>()
    val comicsListLiveData: LiveData<List<ComicItemModel>> = _comicsListMutableLiveData

    init {
        checkInternetConnection()
    }
    fun checkInternetConnection() {
        _internetConnection.value = checkConnection()
    }

    fun updateMainListData() {
        currentOffset = 0
        _isLoading.value = true
        viewModelScope.launch {
            val data = updateMainListUseCase.getAllCharacters(currentOffset, limit)
            if (data == null) {
                _isLoading.value = false
            } else {
                _isLoading.value = false
                _charactersListMutableLiveData.value = emptyList<CharacterItemModel>() + data
            }
        }
    }

    fun fetchMoreData() {
        viewModelScope.launch {
            _isLoading.value = true
            currentOffset += limit
            val data = updateMainListUseCase.getMoreCharacters(favoritesListLiveData.value.orEmpty(), currentOffset, limit)
            if (data == null) {
                _isLoading.value = false
            } else {
                _isLoading.value = false
                _charactersListMutableLiveData.value = _charactersListMutableLiveData.value.orEmpty() + data
            }
        }
    }

    fun fetchMoreData(searchText: String) {
        _isLoading.value = true
        currentOffset += limit
        viewModelScope.launch {
            val data = searchForCharacterUseCase.getCharacterByNameStartingWith(searchText, currentOffset, limit)
            _charactersListMutableLiveData.value = _charactersListMutableLiveData.value.orEmpty() + data
            _isLoading.value = false
        }
    }

    fun searchForCharacterByNameStartingWith(searchText: String) {
        currentOffset = 0
        viewModelScope.launch {
            _isLoading.value = true
            val data = searchForCharacterUseCase.getCharacterByNameStartingWith(searchText, currentOffset, limit)
            _charactersListMutableLiveData.value = data
            _isLoading.value = false
        }
    }

    fun addToFavorites(id: Int) {
        viewModelScope.launch {
            addDeleteFavoritesUseCase.addToFav(id)
            updateFavorites()
        }
    }

    fun removeFromFavorites(id: Int) {
        viewModelScope.launch {
            addDeleteFavoritesUseCase.removeFromFav(id)
            updateFavorites()
        }
    }

    private suspend fun updateFavorites() {
        val data = favoritesListUseCase.getFavoritesList()
        _favoritesListMutableLiveData.postValue(data)
    }

    fun goToFavoritesList() {
        viewModelScope.launch {
            val data = favoritesListUseCase.getFavoritesList()
            _favoritesListMutableLiveData.value = data
        }
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

    fun getCharacterDetails(character: CharacterItemModel) {
        currentOffsetComics = 0
        viewModelScope.launch {
            _currentCharacterMutableLiveData.value = characterDetailsUseCase.getCharacterDetails(character)
            val comics = characterDetailsUseCase.getCharacterComics(character.id, currentOffsetComics, limitComics)
            _comicsListMutableLiveData.value = comics
        }
    }

    fun fetchMoreComics() {
        viewModelScope.launch {
            currentOffsetComics += limitComics
            val data = characterDetailsUseCase.getCharacterComics(currentCharacterLiveData.value!!.id, currentOffsetComics, limitComics)
            _comicsListMutableLiveData.value = _comicsListMutableLiveData.value.orEmpty() + data
        }
    }

    private fun checkConnection(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}