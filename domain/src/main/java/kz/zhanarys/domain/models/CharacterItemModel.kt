package kz.zhanarys.domain.models

data class CharacterItemModel(
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    override val imageExtension: String,
    override val shortInfo: String,
    var isFavorite: Boolean
) : Character