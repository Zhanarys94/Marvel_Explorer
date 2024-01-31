package kz.zhanarys.data.repositories.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharactersDao {
    @Insert
    fun insert(vararg character: CharacterEntity)

    @Insert
    fun insertAll(characters: List<CharacterEntity>)

    @Delete
    fun delete(character: CharacterEntity)

    @Query("SELECT * FROM characters")
    fun getAll(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getById(id: Long): CharacterEntity?

    @Query("DELETE FROM characters WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :name || '%'")
    fun getByName(name: String): List<CharacterEntity>

    @Query("DELETE FROM characters")
    fun deleteAll()
}