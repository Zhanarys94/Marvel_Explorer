package kz.zhanarys.domain.interfaces.repositories.remote

import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.domain.models.ComicItemModel

interface ApiRepository {
    suspend fun getAllCharacters(offset: Int, limit: Int): List<CharacterItemModel>

    suspend fun getCharacterById(id: Int): CharacterEntityModel

    suspend fun getCharacterByNameStartingWith(chars: String, offset: Int, limit: Int): List<CharacterItemModel>

    suspend fun getCharacterByName(name: String, offset: Int, limit: Int): List<CharacterItemModel>

    suspend fun getCharacterComics(id: Int, offset: Int, limit: Int): List<ComicItemModel>
}