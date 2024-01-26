package kz.zhanarys.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kz.zhanarys.domain.entities.HeroEntity

@Dao
interface HeroDao {
    @Insert
    fun insert(vararg hero: HeroEntity)

    @Insert
    fun insertAll(heroes: List<HeroEntity>)

    @Delete
    fun delete(hero: HeroEntity)

    @Query("SELECT * FROM heroes")
    fun getAll(): List<HeroEntity>

    @Query("SELECT * FROM heroes WHERE id = :id")
    fun getById(id: Long): HeroEntity?

    @Query("DELETE FROM heroes WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM heroes WHERE name LIKE '%' || :name || '%'")
    fun getByName(name: String): List<HeroEntity>

    @Query("DELETE FROM heroes")
    fun deleteAll()
}