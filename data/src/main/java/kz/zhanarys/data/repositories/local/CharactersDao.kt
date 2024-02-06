package kz.zhanarys.data.repositories.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharactersDao {
    @Insert
    suspend fun insert(vararg character: CharacterEntity)

    @Insert
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Delete
    suspend fun delete(character: CharacterEntity)

    @Query("SELECT * FROM characters")
    suspend fun getAll(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getById(id: Int): CharacterEntity

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :name || '%'")
    suspend fun getByName(name: String): List<CharacterEntity>

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}