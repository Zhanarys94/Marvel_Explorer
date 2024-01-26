package kz.zhanarys.domain.model.hero

data class HeroShortInfo(
    override val id: Long,
    override val name: String,
    override val imageUrl: String,
    override val shortInfo: String
) : Hero