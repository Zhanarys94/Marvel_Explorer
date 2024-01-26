package kz.zhanarys.marvelexplorer

import android.app.Application
import androidx.room.Room
import kz.zhanarys.data.HeroesDatabase

class MarvelExplorerApp: Application() {
    lateinit var heroesDatabase: HeroesDatabase
        private set

    override fun onCreate() {
        heroesDatabase = Room.databaseBuilder(
            this,
            HeroesDatabase::class.java,
            "heroes.db"
        ).build()
        super.onCreate()
    }
}