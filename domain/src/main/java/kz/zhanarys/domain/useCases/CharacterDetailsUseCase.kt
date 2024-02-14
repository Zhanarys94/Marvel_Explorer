package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.domain.models.ComicItemModel
import kz.zhanarys.domain.models.toCharacterEntityModel
import javax.inject.Inject

class CharacterDetailsUseCase @Inject constructor(private val apiRepository: ApiRepository) {
    suspend fun getCharacterById(id: Int): CharacterEntityModel {
        return apiRepository.getCharacterById(id)
    }

    fun getCharacterDetails(character: CharacterItemModel): CharacterEntityModel {
        return character.toCharacterEntityModel()
    }

    suspend fun getCharacterComics(characterId: Int, offset: Int, limit: Int): List<ComicItemModel> {
        return apiRepository.getCharacterComics(characterId, offset, limit)
    }
}