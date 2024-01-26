package kz.zhanarys.data

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.zhanarys.domain.entities.HeroEntity

@Database(entities = [HeroEntity::class], version = 1)
abstract class HeroesDatabase : RoomDatabase() {
    abstract fun heroDao(): HeroDao
}