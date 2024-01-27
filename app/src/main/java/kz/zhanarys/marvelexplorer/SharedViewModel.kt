package kz.zhanarys.marvelexplorer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.zhanarys.domain.features.MarvelRepoController
import kz.zhanarys.domain.entities.Character
import kz.zhanarys.domain.ViewState
import kz.zhanarys.domain.entities.dto.transformToCharacterEntity
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val marvelRepoController: MarvelRepoController) : ViewModel() {

    private var currentOffset = 0
    private var limit = 20

    private val _stateMutableLiveData = MutableLiveData(ViewState.MAIN)
    val stateLiveData: LiveData<ViewState> = _stateMutableLiveData

    private val _searchBarMutableLiveData = MutableLiveData<String>()
    val searchBarLiveData: LiveData<String> = _searchBarMutableLiveData

    private val _heroesListMutableLiveData = MutableLiveData<List<Character>>()
    val heroesListLiveData: LiveData<List<Character>> = _heroesListMutableLiveData

    private val _savedListMutableLiveData = MutableLiveData<List<Character>>()
    val savedListLiveData: LiveData<List<Character>> = _savedListMutableLiveData

    fun fetchData() {
        viewModelScope.launch {
            val data = marvelRepoController.getAllHeroes(currentOffset, limit)
            _heroesListMutableLiveData.value = data.map { it.transformToCharacterEntity() }
        }
    }

    fun fetchMoreData() {
        viewModelScope.launch {
            currentOffset += limit
            val data = marvelRepoController.getAllHeroes(currentOffset, limit)
            _heroesListMutableLiveData.value = (_heroesListMutableLiveData.value.orEmpty()) + data.map { it.transformToCharacterEntity() }
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

    fun setHeroesList(heroesList: List<Character>) {
        _heroesListMutableLiveData.value = heroesList
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