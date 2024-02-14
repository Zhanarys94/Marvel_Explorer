package kz.zhanarys.domain.models

class CharacterEntityModel(
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    override val imageExtension: String,
    override val shortInfo: String,
    var url: String
) : Character