package kz.zhanarys.domain.repositoriesInterface

import kz.zhanarys.domain.entities.dto.CharacterDataWrapperDto
import retrofit2.http.Query

interface MarvelApi {
    suspend fun getCharacters(
        timestamp: String,
        publicKey: String,
        hash: String,
        offset: Int,
        limit: Int
    ): CharacterDataWrapperDto

    suspend fun getCharacterById(
        characterId: Int,
        timestamp: String,
        publicKey: String,
        hash: String,
        offset: Int,
        limit: Int
    ): CharacterDataWrapperDto
}