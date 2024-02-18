package kz.zhanarys.domain.interfaces.repositories.local

import android.graphics.Bitmap

interface ImageHandler {

    suspend fun saveImage(name: String, url: String)

    suspend fun getImage(name: String): Bitmap?
}