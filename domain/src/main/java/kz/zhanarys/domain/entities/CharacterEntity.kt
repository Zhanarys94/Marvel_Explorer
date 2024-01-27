package kz.zhanarys.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey override val id: Int,
    override val name: String,
    override val imageUrl: String,
    override val imageExtension: String,
    override val shortInfo: String
) : Character