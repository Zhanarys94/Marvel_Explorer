package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.local.LocalDatabaseDao
import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import kz.zhanarys.domain.models.CharacterItemModel
import javax.inject.Inject

class UpdateMainListUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localDatabaseDao: LocalDatabaseDao) {
    suspend fun getAllCharacters(offset: Int, limit: Int): List<CharacterItemModel> {
        val data = apiRepository.getAllCharacters(offset, limit)
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

    suspend fun getCharacterByNameStartingWith(chars: String, offset: Int, limit: Int): List<CharacterItemModel> {
        return apiRepository.getCharacterByNameStartingWith(chars, offset, limit)
    }

    suspend fun getCharacterByName(name: String, offset: Int, limit: Int): List<CharacterItemModel> {
        return apiRepository.getCharacterByName(name, offset, limit)
    }

}
