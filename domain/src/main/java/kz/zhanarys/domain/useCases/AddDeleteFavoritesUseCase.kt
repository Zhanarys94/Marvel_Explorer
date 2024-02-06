package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.local.LocalDatabaseDao
import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import javax.inject.Inject

class AddDeleteFavoritesUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localDatabaseDao: LocalDatabaseDao
) {
    suspend fun addToFav(id: Int) {
        val characterEntityModel = apiRepository.getCharacterById(id)
        localDatabaseDao.insert(characterEntityModel)
    }

    suspend fun removeFromFav(id: Int) {
        localDatabaseDao.deleteById(id)
    }

}