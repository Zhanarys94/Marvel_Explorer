package kz.zhanarys.data.marvelApi

import kz.zhanarys.data.configurations.MarvelRetrofitException
import kz.zhanarys.data.configurations.Timestamp
import kz.zhanarys.data.configurations.Md5
import kz.zhanarys.domain.entities.dto.HeroDto
import kz.zhanarys.domain.repositoriesInterface.MarvelRetrofitRepositoryI
import javax.inject.Inject

private const val PUBLIC_KEY = "fe55b115a7ff5b40c316c534071319a7"
private const val PRIVATE_KEY = "7ef25cf0f94144971178f94d31c46c4df1226995"

class MarvelRetrofitRepository @Inject constructor(private val retrofit: RetrofitMarvelRest) :
    MarvelRetrofitRepositoryI {

    private val timestamp = Timestamp
    private val md5 = Md5()
    override suspend fun getAllHeroes(offset: Int, limit: Int): List<HeroDto> {
        val formattedTimestamp = timestamp.generateFormattedTimestamp(timestamp.generateTimestamp())
        val hash = md5.md5(formattedTimestamp + PRIVATE_KEY + PUBLIC_KEY)
        return runCatching {
            val response = retrofit.getCharacters(formattedTimestamp, PUBLIC_KEY, hash, offset, limit)
            if (response.code == 200 && response.data != null) {
                response.data!!.results!!
            } else {
                throw MarvelRetrofitException("Error ${response.code} ${response.status}")
            }
        }.getOrElse {
            throw MarvelRetrofitException("Error during network request ${it.localizedMessage} ${it.cause} ${it.message}")
        }
    }

    override suspend fun getHeroById(id: Int, offset: Int, limit: Int): HeroDto {
        val formattedTimestamp = timestamp.generateFormattedTimestamp(timestamp.generateTimestamp())
        val hash = md5.md5(formattedTimestamp + PRIVATE_KEY + PUBLIC_KEY)
        retrofit.getCharacterById(id, formattedTimestamp, PUBLIC_KEY, hash, offset, limit).let { response ->
            if (
                response.code == 200 && response.data != null
            ) {
                return response.data!!.results!!.first()
            } else {
                throw MarvelRetrofitException("Error ${response.code} ${response.status}")
            }
        }
    }
}