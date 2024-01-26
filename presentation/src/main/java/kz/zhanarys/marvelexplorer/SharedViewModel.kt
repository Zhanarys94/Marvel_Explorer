package kz.zhanarys.marvelexplorer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.zhanarys.data.HeroesDatabase
import kz.zhanarys.domain.entities.HeroEntity
import kz.zhanarys.domain.model.hero.Hero
import kz.zhanarys.domain.model.hero.HeroShortInfo
import kz.zhanarys.domain.model.viewState.ViewState

class SharedViewModel(private val heroesDatabase: HeroesDatabase): ViewModel() {
    private val _stateMutableLiveData = MutableLiveData(ViewState.MAIN)
    val stateLiveData: LiveData<ViewState> = _stateMutableLiveData

    private val _searchBarMutableLiveData = MutableLiveData<String>()
    val searchBarLiveData: LiveData<String> = _searchBarMutableLiveData

    private val _heroesListMutableLiveData = MutableLiveData<List<Hero>>()
    val heroesListLiveData: LiveData<List<Hero>> = _heroesListMutableLiveData

    private val _savedListMutableLiveData = MutableLiveData<List<Hero>>()
    val savedListLiveData: LiveData<List<Hero>> = _savedListMutableLiveData

    fun toMainListButtonClick() {
        _stateMutableLiveData.value = ViewState.MAIN
    }

    fun toSavedListButtonClick() {
        val heroesList = heroesDatabase.heroDao().getAll().map { HeroShortInfo(it.id, it.name, it.imageUrl, it.shortInfo ) }
        _stateMutableLiveData.value = ViewState.SAVED
        _savedListMutableLiveData.value = heroesList
    }

    fun setSearchBar(text: String) {
        _searchBarMutableLiveData.value = text
    }

    fun setHeroesList(heroesList: List<Hero>) {
        _heroesListMutableLiveData.value = heroesList
    }

    fun setSavedList(savedList: List<Hero>) {
        val entitiesList = savedList.map { HeroEntity(it.id, it.name, it.imageUrl, it.shortInfo) }
        heroesDatabase.heroDao().insertAll(entitiesList)
        _savedListMutableLiveData.value = savedList
    }

}