package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import kz.zhanarys.domain.models.CharacterItemModel
import javax.inject.Inject

class UpdateMainListUseCase @Inject constructor(private val apiRepository: ApiRepository) {
    suspend fun getAllCharacters(offset: Int, limit: Int): List<CharacterItemModel> {
        return apiRepository.getAllCharacters(offset, limit)
    }

    suspend fun getCharacterByNameStartingWith(chars: String, offset: Int, limit: Int): List<CharacterItemModel> {
        return apiRepository.getCharacterByNameStartingWith(chars, offset, limit)
    }

    suspend fun getCharacterByName(name: String, offset: Int, limit: Int): List<CharacterItemModel> {
        return apiRepository.getCharacterByName(name, offset, limit)
    }

}
