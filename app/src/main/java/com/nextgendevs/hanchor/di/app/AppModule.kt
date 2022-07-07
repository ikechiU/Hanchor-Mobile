package com.nextgendevs.hanchor.di.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessaging
import com.nextgendevs.hanchor.business.datasource.cache.AppDatabase
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStoreManager
import com.nextgendevs.hanchor.business.usecase.CheckNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val BASE_URL = "https://hanchor.herokuapp.com/"
    private val APP_DATABASE_NAME = "my_db"

    @Singleton
    @Provides
    fun provideDataStoreManager(application: Application): AppDataStore {
        return AppDataStoreManager(application)
    }

    @Singleton
    @Provides
    fun provideCheckNetwork(application: Application): CheckNetwork {
        return CheckNetwork(application)
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        okHttpClientBuilder.addNetworkInterceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36"
                    )
                    .build()
            )
        }.addNetworkInterceptor(logger)
            .addInterceptor(HttpLoggingInterceptor())
            .connectTimeout(15, TimeUnit.MINUTES)
            .readTimeout(15, TimeUnit.MINUTES)

        return okHttpClientBuilder.build()
    }



    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideFirebaseInstance(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        APP_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideGratitudeDao(database: AppDatabase) = database.gratitudeDao()

    @Singleton
    @Provides
    fun provideTodoDao(database: AppDatabase) = database.todoDao()

    @Singleton
    @Provides
    fun provideAuthTokenDao(database: AppDatabase) = database.authTokenDao()

    @Singleton
    @Provides
    fun provideAffirmationDao(database: AppDatabase) = database.affirmationDao()

}