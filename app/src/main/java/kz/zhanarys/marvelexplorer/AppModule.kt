package kz.zhanarys.marvelexplorer

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import coil.ImageLoader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.zhanarys.data.repositories.local.CharactersDao
import kz.zhanarys.data.repositories.local.CharactersLocalRepository
import kz.zhanarys.data.repositories.local.LocalCacheImageHandler
import kz.zhanarys.data.repositories.local.LocalRepositoryDao
import kz.zhanarys.data.repositories.network.MarvelApiRepository
import kz.zhanarys.data.repositories.network.MarvelApiRest
import kz.zhanarys.domain.interfaces.repositories.local.ImageHandler
import kz.zhanarys.domain.interfaces.repositories.local.LocalDatabaseDao
import kz.zhanarys.domain.interfaces.repositories.remote.ApiRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    @Binds
    @Singleton
    abstract fun provideApiRepository(apiRepository: MarvelApiRepository): ApiRepository
}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    @Binds
    @Singleton
    abstract fun provideDatabase(database: LocalRepositoryDao): LocalDatabaseDao
}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class ImageHandlerModule {
    @Binds
    @Singleton
    abstract fun provideImageHandler(imageHandler: LocalCacheImageHandler): ImageHandler
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://gateway.marvel.com"
    private const val DATABASE_NAME = "characters_database.db"

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): CharactersLocalRepository {
        return Room.databaseBuilder(
            context,
            CharactersLocalRepository::class.java,
            DATABASE_NAME
        )
            .addMigrations(CharactersLocalRepository.migration1to2)
            .build()
    }

    @Provides
    fun provideCharactersDao(database: CharactersLocalRepository): CharactersDao {
        return database.characterDao()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitService(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideMarvelRetrofitService(retrofit: Retrofit): MarvelApiRest {
        return retrofit.create(MarvelApiRest::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideImageHandler(
        @ApplicationContext context: Context,
        imageLoader: ImageLoader): LocalCacheImageHandler {
        return LocalCacheImageHandler(context, imageLoader)
    }

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context).build()
    }
}