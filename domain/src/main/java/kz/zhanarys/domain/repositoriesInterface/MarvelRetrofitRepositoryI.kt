package kz.zhanarys.domain.repositoriesInterface

import kz.zhanarys.domain.entities.dto.HeroDto

interface MarvelRetrofitRepositoryI {
    suspend fun getAllHeroes(offset: Int, limit: Int): List<HeroDto>

    suspend fun getHeroById(id: Int, offset: Int, limit: Int): HeroDto
}