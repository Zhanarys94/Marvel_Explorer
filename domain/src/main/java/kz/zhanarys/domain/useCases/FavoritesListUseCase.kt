package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.local.LocalDatabaseDao
import javax.inject.Inject

class FavoritesListUseCase @Inject constructor(
    private val localDatabaseDao: LocalDatabaseDao
) {
    suspend fun getFavoritesList() = localDatabaseDao.getAll()

    suspend fun getCharacterById(id: Int) = localDatabaseDao.getById(id)
}