package kz.zhanarys.domain.features

import kz.zhanarys.domain.entities.dto.HeroDto

interface HeroesRepoController {
    suspend fun getAllHeroes(offset: Int, limit: Int): List<HeroDto>

    suspend fun getHeroById(id: Int, offset: Int, limit: Int): HeroDto
}