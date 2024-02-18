package kz.zhanarys.domain.useCases

import kz.zhanarys.domain.interfaces.repositories.local.ImageHandler
import kz.zhanarys.domain.interfaces.repositories.local.LocalDatabaseDao
import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import javax.inject.Inject

class AddDeleteFavoritesUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localDatabaseDao: LocalDatabaseDao,
    private val imageHandler: ImageHandler
) {
    suspend fun addToFav(id: Int) {
        val imageVariant = "/standard_xlarge"
        val characterEntityModel = apiRepository.getCharacterById(id)
        val imageUrl = characterEntityModel.imageUrl + imageVariant + characterEntityModel.imageExtension
        imageHandler.saveImage(characterEntityModel.id.toString(), imageUrl)
        localDatabaseDao.insert(characterEntityModel)
    }

    suspend fun removeFromFav(id: Int) {
        localDatabaseDao.deleteById(id)
    }

}