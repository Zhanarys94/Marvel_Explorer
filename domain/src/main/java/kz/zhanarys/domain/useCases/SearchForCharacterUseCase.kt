package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import kz.zhanarys.domain.models.CharacterItemModel
import javax.inject.Inject

class SearchForCharacterUseCase @Inject constructor(private val apiRepository: ApiRepository) {
    suspend fun getCharacterById(id: Int, offset: Int, limit: Int): CharacterItemModel {
        return apiRepository.getCharacterById(id, offset, limit)
    }
}