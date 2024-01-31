package kz.zhanarys.domain.interfaces.repositories.remote

import kz.zhanarys.domain.models.CharacterItemModel

interface ApiRepository {
    suspend fun getAllCharacters(offset: Int, limit: Int): List<CharacterItemModel>

    suspend fun getCharacterById(id: Int, offset: Int, limit: Int): CharacterItemModel

    suspend fun getCharacterByNameStartingWith(chars: String, offset: Int, limit: Int): List<CharacterItemModel>

    suspend fun getCharacterByName(name: String, offset: Int, limit: Int): List<CharacterItemModel>
}