package kz.zhanarys.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import kz.zhanarys.domain.entities.CharacterEntity


@Database(entities = [CharacterEntity::class], version = 1)
abstract class CharactersDatabase : RoomDatabase() {
    abstract fun heroDao(): MarvelCharacterDao
}