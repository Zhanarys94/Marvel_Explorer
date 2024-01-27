package kz.zhanarys.marvelexplorer

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kz.zhanarys.data.db.MarvelCharacterDao
import kz.zhanarys.data.db.CharactersDatabase
import kz.zhanarys.data.marvelApi.MarvelRetrofitRepository
import kz.zhanarys.data.marvelApi.RetrofitMarvelRest
import kz.zhanarys.domain.repositoriesInterface.MarvelRetrofitRepositoryI
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingsModule {
    @Binds
    @Singleton
    abstract fun bindRepository(repository: MarvelRetrofitRepository): MarvelRetrofitRepositoryI
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://gateway.marvel.com"
    private val contentType = "application/json".toMediaType()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): CharactersDatabase {
        return Room.databaseBuilder(
            context,
            CharactersDatabase::class.java,
            "heroes.db"
        ).build()
    }

    @Provides
    fun provideHeroDao(database: CharactersDatabase): MarvelCharacterDao {
        return database.heroDao()
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
    fun provideMarvelRetrofitService(retrofit: Retrofit): RetrofitMarvelRest {
        return retrofit.create(RetrofitMarvelRest::class.java)
    }
}