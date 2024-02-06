package kz.zhanarys.data.repositories.local

import kz.zhanarys.domain.interfaces.repositories.local.LocalDatabaseDao
import kz.zhanarys.domain.models.CharacterEntityModel
import kz.zhanarys.domain.models.CharacterItemModel
import javax.inject.Inject

class LocalRepositoryDao @Inject constructor(
    private val repositoryDao: CharactersDao
): LocalDatabaseDao {
    override suspend fun insert(vararg character: CharacterEntityModel) {
        for (char in character) {
            repositoryDao.insert(char.toCharacterEntity())
        }
    }

    override suspend fun insertAll(characters: List<CharacterEntityModel>) {
        repositoryDao.insertAll(characters.map { it.toCharacterEntity() })
    }

    override suspend fun delete(character: CharacterEntityModel) {
        repositoryDao.delete(character.toCharacterEntity())
    }

    override suspend fun getAll(): List<CharacterItemModel> {
        return repositoryDao.getAll().map { it.toCharacterItemModel() }
    }

    override suspend fun getById(id: Int): CharacterEntityModel {
        return repositoryDao.getById(id).toCharacterEntityModel()
    }

    override suspend fun deleteById(id: Int) {
        repositoryDao.deleteById(id)
    }

    override suspend fun getByName(name: String): List<CharacterItemModel> {
        return repositoryDao.getByName(name).map { it.toCharacterItemModel() }
    }

    override suspend fun deleteAll() {
        repositoryDao.deleteAll()
    }
}