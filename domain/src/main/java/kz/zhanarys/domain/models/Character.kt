package kz.zhanarys.domain.models

interface Character {
    val id: Int
    val name: String
    val imageUrl: String
    val shortInfo: String
    val imageExtension: String
}