package kz.zhanarys.data.repositories.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CharacterEntity::class], version = 1)
abstract class CharactersLocalDatabase : RoomDatabase() {
    abstract fun characterDao(): CharactersDao
}