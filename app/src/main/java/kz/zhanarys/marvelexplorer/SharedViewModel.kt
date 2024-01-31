package kz.zhanarys.marvelexplorer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.zhanarys.domain.useCases.UpdateMainListUseCase
import kz.zhanarys.domain.ViewState
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.domain.useCases.SearchForCharacterUseCase
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val updateMainListUseCase: UpdateMainListUseCase,
    private val searchForCharacterUseCase: SearchForCharacterUseCase
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
        viewModelScope.launch {
            val data = updateMainListUseCase.getCharacterByNameStartingWith(searchText, currentOffset, limit)
            _charactersListMutableLiveData.value = data
        }
    }

    fun searchForCharacterByName(searchText: String) {
        viewModelScope.launch {
            val data = updateMainListUseCase.getCharacterByName(searchText, currentOffset, limit)
            _charactersListMutableLiveData.value = data
        }
    }

    fun searchForCharacterById(id: Int) {
        viewModelScope.launch {
            val data = searchForCharacterUseCase.getCharacterById(id, currentOffset, limit)
            // TODO
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