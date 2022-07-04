package com.nextgendevs.hanchor.di.main

import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenDao
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.AffirmationDao
import com.nextgendevs.hanchor.business.datasource.network.main.MainApiInterface
import com.nextgendevs.hanchor.business.usecase.CheckNetwork
import com.nextgendevs.hanchor.business.usecase.main.AuthTokenCache
import com.nextgendevs.hanchor.business.usecase.main.GetAffirmation
import com.nextgendevs.hanchor.business.usecase.main.GetUser
import com.nextgendevs.hanchor.business.usecase.main.UpdateUsername
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun provideMainApiService(retrofit: Retrofit): MainApiInterface {
        return retrofit.create(MainApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideGerUser(
        cache: AffirmationDao,
        appDataStoreManager: AppDataStore,
        service: MainApiInterface,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork,
    ): GetUser {
        return GetUser(cache, appDataStoreManager, service, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideAffirmation(
        cache: AffirmationDao,
    ): GetAffirmation {
        return GetAffirmation(cache)
    }

    @Singleton
    @Provides
    fun provideUpdateUser(
        appDataStoreManager: AppDataStore,
        service: MainApiInterface,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork,
    ): UpdateUsername {
        return UpdateUsername(appDataStoreManager, service, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideAuthToken(
        authTokenCache: AuthTokenDao
    ): AuthTokenCache {
        return AuthTokenCache(authTokenCache)
    }

}