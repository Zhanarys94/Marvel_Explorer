package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.local.LocalDatabaseDao
import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel
import javax.inject.Inject

class FavoritesListUseCase @Inject constructor(
    private val localDatabaseDao: LocalDatabaseDao
) {
    suspend fun getFavoritesList(): List<CharacterItemModel> {
        return localDatabaseDao.getAll().sortedBy { it.name }
    }

    suspend fun getCharacterById(id: Int): CharacterEntityModel {
        return localDatabaseDao.getById(id)
    }
}