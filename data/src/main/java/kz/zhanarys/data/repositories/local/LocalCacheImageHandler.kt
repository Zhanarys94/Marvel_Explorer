package kz.zhanarys.data.repositories.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.zhanarys.domain.interfaces.repositories.local.ImageHandler
import java.io.IOException
import javax.inject.Inject

class LocalCacheImageHandler @Inject constructor(
    private val context: Context,
    private val imageLoader: ImageLoader
): ImageHandler {
    override suspend fun saveImage(name: String, url: String) {
        withContext(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(url)
                .target {image ->
                    val bitmap = (image as? BitmapDrawable)?.bitmap
                    if (bitmap != null) {
                        context.openFileOutput("$name.png", Context.MODE_PRIVATE).use { stream ->
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        }
                    }
                }
                .build()
            imageLoader.enqueue(request)
        }
    }

    override suspend fun getImage(name: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            val file = context.getFileStreamPath("$name.png")
            if (file.exists()) {
                try {
                    context.openFileInput("$name.png").use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            } else {
                null
            }
        }
    }
}