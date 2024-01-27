package kz.zhanarys.domain.features

import kz.zhanarys.domain.entities.dto.HeroDto
import kz.zhanarys.domain.repositoriesInterface.MarvelRetrofitRepositoryI
import javax.inject.Inject



class MarvelRepoController @Inject constructor(private val repositoryI: MarvelRetrofitRepositoryI) :
    HeroesRepoController {
    override suspend fun getAllHeroes(offset: Int, limit: Int): List<HeroDto> {
        return repositoryI.getAllHeroes(offset, limit)
    }

    override suspend fun getHeroById(id: Int, offset: Int, limit: Int): HeroDto {
        return repositoryI.getHeroById(id, offset, limit)
    }
}