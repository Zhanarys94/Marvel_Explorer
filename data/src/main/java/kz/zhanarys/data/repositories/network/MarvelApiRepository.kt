package kz.zhanarys.data.repositories.network

import kz.zhanarys.data.repositories.network.configurations.Timestamp
import kz.zhanarys.data.repositories.network.configurations.Md5
import kz.zhanarys.data.repositories.network.dto.toCharacterEntityModel
import kz.zhanarys.data.repositories.network.dto.toCharacterItemModel
import kz.zhanarys.data.repositories.network.dto.toComicsItemModel
import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.domain.models.ComicItemModel
import javax.inject.Inject
import javax.inject.Singleton

private const val PUBLIC_KEY = "fe55b115a7ff5b40c316c534071319a7"
private const val PRIVATE_KEY = "7ef25cf0f94144971178f94d31c46c4df1226995"

@Singleton
class MarvelApiRepository @Inject constructor(private val retrofit: MarvelApiRest): ApiRepository {

    private val timestamp = Timestamp
    private val md5 = Md5()

    override suspend fun getAllCharacters(offset: Int, limit: Int): List<CharacterItemModel>? {
        val formattedTimestamp = timestamp.generateFormattedTimestamp(timestamp.generateTimestamp())
        val hash = md5.md5(formattedTimestamp + PRIVATE_KEY + PUBLIC_KEY)
        return runCatching {
            val response = retrofit.getCharacters(formattedTimestamp, PUBLIC_KEY, hash, offset, limit)
            if (response.code == 200) {
                response.data.results.map { it.toCharacterItemModel() }
            } else {
                throw MarvelApiException("Error ${response.code} ${response.status}")
            }
        }.getOrNull()
    }

    override suspend fun getCharacterById(id: Int): CharacterEntityModel {
        val formattedTimestamp = timestamp.generateFormattedTimestamp(timestamp.generateTimestamp())
        val hash = md5.md5(formattedTimestamp + PRIVATE_KEY + PUBLIC_KEY)
        retrofit.getCharacterById(id, formattedTimestamp, PUBLIC_KEY, hash).let { response ->
            if (response.code == 200) {
                return response.data.results.first().toCharacterEntityModel()
            } else {
                throw MarvelApiException("Error ${response.code} ${response.status}")
            }
        }
    }

    override suspend fun getCharacterByNameStartingWith(chars: String, offset: Int, limit: Int): List<CharacterItemModel> {
        val formattedTimestamp = timestamp.generateFormattedTimestamp(timestamp.generateTimestamp())
        val hash = md5.md5(formattedTimestamp + PRIVATE_KEY + PUBLIC_KEY)
        if (chars.isEmpty()) {
            return getAllCharacters(offset, limit) ?: emptyList()
        }
        retrofit.getCharacterByNameStartingWith(chars, formattedTimestamp, PUBLIC_KEY, hash, offset, limit).let { response ->
            if (response.code == 200) {
                return response.data.results.map { it.toCharacterItemModel() }
            } else {
                return emptyList()
            }
        }
    }

    override suspend fun getCharacterByName(name: String, offset: Int, limit: Int): List<CharacterItemModel> {
        val formattedTimestamp = timestamp.generateFormattedTimestamp(timestamp.generateTimestamp())
        val hash = md5.md5(formattedTimestamp + PRIVATE_KEY + PUBLIC_KEY)
        retrofit.getCharacterByName(name, formattedTimestamp, PUBLIC_KEY, hash, offset, limit).let { response ->
            if (response.code == 200) {
                return response.data.results.map { it.toCharacterItemModel() }
            } else {
                return emptyList()
            }
        }
    }

    override suspend fun getCharacterComics(id: Int, offset: Int, limit: Int): List<ComicItemModel>? {
        val formattedTimestamp = timestamp.generateFormattedTimestamp(timestamp.generateTimestamp())
        val hash = md5.md5(formattedTimestamp + PRIVATE_KEY + PUBLIC_KEY)
        return runCatching {
            val response = retrofit.getCharacterComics(id, formattedTimestamp, PUBLIC_KEY, hash, offset, limit)
            if (response.code == 200) {
                response.data.results.map { it.toComicsItemModel() }
            } else {
                emptyList()
            }
        }.getOrNull()
    }
}