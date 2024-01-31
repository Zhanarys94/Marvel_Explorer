package kz.zhanarys.data.repositories.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val imageExtension: String,
    val shortInfo: String
)