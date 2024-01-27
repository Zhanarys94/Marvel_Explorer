package kz.zhanarys.domain.repositoriesInterface

import kz.zhanarys.domain.entities.CharacterEntity

interface CharacterDao {
    fun getAll(): List<CharacterEntity>
    fun getById(id: Long): CharacterEntity?
    fun deleteById(id: Long)
    fun insert(vararg hero: CharacterEntity)
    fun insertAll(heroes: List<CharacterEntity>)
    fun delete(hero: CharacterEntity)
    fun getByName(name: String): List<CharacterEntity>
    fun deleteAll()
}