package kz.zhanarys.domain.model.hero

interface Hero {
    val id: Long
    val name: String
    val imageUrl: String
    val shortInfo: String
}