package kz.zhanarys.domain

data class HeroShortInfo(
    override val id: Long,
    override val name: String,
    override val imageUrl: String,
    private val shortInfo: String
) : Hero {

    fun getShortInfo() = shortInfo
}