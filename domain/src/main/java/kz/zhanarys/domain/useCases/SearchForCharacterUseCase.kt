package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.local.LocalDatabaseDao
import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import kz.zhanarys.domain.models.CharacterItemModel
import javax.inject.Inject

class SearchForCharacterUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localDatabaseDao: LocalDatabaseDao
) {
    suspend fun getCharacterByNameStartingWith(chars: String, offset: Int, limit: Int): List<CharacterItemModel> {
        val data = apiRepository.getCharacterByNameStartingWith(chars, offset, limit)
        val localData = localDatabaseDao.getAll()
        val updatedData = data.map { character ->
            if (localData.any { it.id == character.id } ) {
                character.copy(isFavorite = true)
            } else {
                character
            }
        }
        return updatedData.sortedBy { it.name }
    }

    suspend fun getCharacterByNameStartingWithFromLocalDB(chars: String): List<CharacterItemModel> {
        return localDatabaseDao.getByName(chars).sortedBy { it.name }
    }
}