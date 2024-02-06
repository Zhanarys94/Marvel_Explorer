package kz.zhanarys.marvelexplorer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
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

    private var currentOffset = 0
    private var limit = 20

    private val _stateMutableLiveData = MutableLiveData(ViewState.MAIN)
    val stateLiveData: LiveData<ViewState> = _stateMutableLiveData

    private val _searchBarMutableLiveData = MutableLiveData<String>()
    val searchBarLiveData: LiveData<String> = _searchBarMutableLiveData

    private val _charactersListMutableLiveData = MutableLiveData<List<CharacterItemModel>>()
    val charactersListLiveData: LiveData<List<CharacterItemModel>> = _charactersListMutableLiveData

    private val _favoritesListMutableLiveData = MutableLiveData<List<CharacterItemModel>>()
    val favoritesListLiveData: LiveData<List<CharacterItemModel>> = _favoritesListMutableLiveData

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

    fun searchForCharacterByNameStartingWith(searchText: String) {
        currentOffset = 0
        viewModelScope.launch {
            val data = updateMainListUseCase.getCharacterByNameStartingWith(searchText, currentOffset, limit)
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
        }
    }

    fun getFavoritesList() {
        viewModelScope.launch {
            val data = favoritesListUseCase.getFavoritesList()
            _favoritesListMutableLiveData.value = data
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

    fun toMainListButtonClick() {
        _stateMutableLiveData.value = ViewState.MAIN
    }

    fun toSearchListButtonClick() {

    }

    /*fun toSavedListButtonClick() {
        val heroesList = heroDao.getAll().map { HeroEntity(it.id, it.name, it.imageUrl, it.shortInfo ) }
        _stateMutableLiveData.value = ViewState.SAVED
        _savedListMutableLiveData.value = heroesList
    }*/

    fun setSearchBar(text: String) {
        _searchBarMutableLiveData.value = text
    }

    fun setHeroesList(heroesList: List<CharacterItemModel>) {
        _charactersListMutableLiveData.value = heroesList
    }

    /*fun addHeroToDatabase(hero: Hero) {
        val entity = HeroEntity(hero.id, hero.name, hero.imageUrl, hero.shortInfo)
        viewModelScope.launch {
            heroDao.insert(entity)
        }
    }*/

    /*fun deleteHeroFromDatabase(hero: Hero) {
        val entity = HeroEntity(hero.id, hero.name, hero.imageUrl, hero.shortInfo)
        viewModelScope.launch {
            heroDao.delete(entity)
        }
    }*/

    /*fun deleteHeroById(id: Long) {
        viewModelScope.launch {
            heroDao.deleteById(id)
        }
    }*/

    /*fun clearDatabase() {
        viewModelScope.launch {
            heroDao.deleteAll()
        }
    }*/

    /*fun updateSavedList() {
        val heroesList = heroDao.getAll().map { HeroEntity(it.id, it.name, it.imageUrl, it.shortInfo ) }
        _savedListMutableLiveData.value = heroesList
    }

    fun findHeroByNameDb(name: String) {
        val heroesList = heroDao.getByName(name).map { HeroEntity(it.id, it.name, it.imageUrl, it.shortInfo ) }
        _savedListMutableLiveData.value = heroesList
    }*/
}