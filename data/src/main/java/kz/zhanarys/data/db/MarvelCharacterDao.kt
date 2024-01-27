package kz.zhanarys.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kz.zhanarys.domain.entities.CharacterEntity
import kz.zhanarys.domain.repositoriesInterface.CharacterDao

@Dao
interface MarvelCharacterDao : CharacterDao {
    @Insert
    override fun insert(vararg hero: CharacterEntity)

    @Insert
    override fun insertAll(heroes: List<CharacterEntity>)

    @Delete
    override fun delete(hero: CharacterEntity)

    @Query("SELECT * FROM characters")
    override fun getAll(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    override fun getById(id: Long): CharacterEntity?

    @Query("DELETE FROM characters WHERE id = :id")
    override fun deleteById(id: Long)

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :name || '%'")
    override fun getByName(name: String): List<CharacterEntity>

    @Query("DELETE FROM characters")
    override fun deleteAll()
}