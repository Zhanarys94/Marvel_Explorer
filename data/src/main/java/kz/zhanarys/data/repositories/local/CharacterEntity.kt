package kz.zhanarys.data.repositories.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val imageExtension: String,
    val shortInfo: String,
    val url: String
)

fun CharacterEntity.toCharacterItemModel() = CharacterItemModel(
    id,
    name,
    imageUrl,
    imageExtension,
    shortInfo,
    true
)

fun CharacterEntity.toCharacterEntityModel() = CharacterEntityModel(
    id,
    name,
    imageUrl,
    imageExtension,
    shortInfo,
    url
)

fun CharacterEntityModel.toCharacterEntity() = CharacterEntity(
    id,
    name,
    imageUrl,
    imageExtension,
    shortInfo,
    url
)