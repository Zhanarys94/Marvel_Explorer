package kz.zhanarys.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.zhanarys.domain.model.hero.Hero

@Entity(tableName = "heroes")
data class HeroEntity(
    @PrimaryKey override val id: Long,
    override val name: String,
    override val imageUrl: String,
    override val shortInfo: String
) : Hero