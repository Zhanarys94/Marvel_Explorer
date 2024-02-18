package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.local.LocalDatabaseDao
import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import kz.zhanarys.domain.models.CharacterItemModel
import javax.inject.Inject

class UpdateMainListUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localDatabaseDao: LocalDatabaseDao) {

    suspend fun getAllCharacters(offset: Int, limit: Int): List<CharacterItemModel>? {
        val data = apiRepository.getAllCharacters(offset, limit) ?: return null
        if (data.isEmpty()) {
            return emptyList()
        }
        val localData = localDatabaseDao.getAll()
        val updatedData = data.map { character ->
            if (localData.any { it.id == character.id } ) {
                character.copy(isFavorite = true)
            } else {
                character
            }
        }
        return updatedData
    }

    suspend fun getMoreCharacters(
        localData: List<CharacterItemModel>,
        offset: Int,
        limit: Int
    ): List<CharacterItemModel>? {
        val data = apiRepository.getAllCharacters(offset, limit) ?: return null
        if (data.isEmpty()) {
            return emptyList()
        }
        val updatedData = data.map { character ->
            if (localData.any { it.id == character.id } ) {
                character.copy(isFavorite = true)
            } else {
                character
            }
        }
        return updatedData
    }

}
