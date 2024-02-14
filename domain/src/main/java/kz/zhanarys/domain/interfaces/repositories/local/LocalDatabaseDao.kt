package kz.zhanarys.domain.interfaces.repositories.local

import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel

interface LocalDatabaseDao {
    suspend fun insert(vararg character: CharacterEntityModel)


    suspend fun insertAll(characters: List<CharacterEntityModel>)


    suspend fun delete(character: CharacterEntityModel)

    suspend fun getAll(): List<CharacterItemModel>

    suspend fun getById(id: Int): CharacterEntityModel

    suspend fun deleteById(id: Int)

    suspend fun getByName(name: String): List<CharacterItemModel>

    suspend fun deleteAll()
}