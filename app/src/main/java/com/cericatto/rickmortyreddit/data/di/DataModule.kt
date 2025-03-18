package com.cericatto.rickmortyreddit.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.cericatto.rickmortyreddit.data.local.entity.RedditDatabase
import com.cericatto.rickmortyreddit.data.remote.RickMortyApi
import com.cericatto.rickmortyreddit.data.remote.RickMortyApi.Companion.BASE_URL
import com.cericatto.rickmortyreddit.data.repository.RickMortyRepositoryImpl
import com.cericatto.rickmortyreddit.domain.repository.RickMortyRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

	@Provides
	@Singleton
	fun provideContext(
		app: Application
	): Context {
		return app.applicationContext
	}

	@Provides
	@Singleton
	fun provideOkHttpClient(): OkHttpClient {
		val loggingInterceptor = HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		}

		return OkHttpClient.Builder()
			.addInterceptor(
				HttpLoggingInterceptor().apply {
					level = HttpLoggingInterceptor.Level.BODY
				}
			)
			.addInterceptor(loggingInterceptor)
			.build()
	}

	@Provides
	@Singleton
	fun provideRickMortyApi(
		client: OkHttpClient,
		moshi: Moshi
	): RickMortyApi {
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create(moshi))
			.client(client)
			.build()
			.create()
	}

	@Provides
	@Singleton
	fun provideRepository(
		db: RedditDatabase,
		api: RickMortyApi
	): RickMortyRepository {
		return RickMortyRepositoryImpl(
			dao = db.dao,
			api = api
		)
	}

	@Provides
	@Singleton
	fun provideRickMortyDatabase(app: Application): RedditDatabase {
		return Room.databaseBuilder(
			app,
			RedditDatabase::class.java,
			"reddit_db"
		).build()
	}

	@Provides
	@Singleton
	fun provideMoshi(): Moshi {
		return Moshi.Builder()
			.add(KotlinJsonAdapterFactory())
			.build()
	}
}