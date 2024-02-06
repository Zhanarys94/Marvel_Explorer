package kz.zhanarys.data.repositories.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CharacterEntity::class], version = 2)
abstract class CharactersLocalRepository : RoomDatabase() {
    abstract fun characterDao(): CharactersDao

    companion object {
        private const val DATABASE_NAME = "characters_database.db"

        val migration1to2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS `characters`")
                db.execSQL("CREATE TABLE IF NOT EXISTS `characters` (" +
                        "`id` INTEGER NOT NULL, " +
                        "`name` TEXT NOT NULL, " +
                        "`image_url` TEXT NOT NULL, " +
                        "`image_extension` TEXT NOT NULL, " +
                        "`short_info` TEXT NOT NULL, " +
                        "`url` TEXT NOT NULL, " +
                        "PRIMARY KEY(`id`))"
                )
            }
        }
    }
}