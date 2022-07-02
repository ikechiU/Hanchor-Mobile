package com.nextgendevs.hanchor.di.main.gratitude

import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeDao
import com.nextgendevs.hanchor.business.datasource.network.main.gratitude.GratitudeApiInterface
import com.nextgendevs.hanchor.business.usecase.CheckNetwork
import com.nextgendevs.hanchor.business.usecase.main.gratitude.*
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GratitudeModule {

    @Singleton
    @Provides
    fun provideGratitudeApiService(retrofit: Retrofit): GratitudeApiInterface {
        return retrofit.create(GratitudeApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideFetchGratitudes(
        appDataStoreManager: AppDataStore,
        service: GratitudeApiInterface,
        cache: GratitudeDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork,
    ): FetchGratitudes {
        return FetchGratitudes(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideFetchGratitude(
        appDataStoreManager: AppDataStore,
        service: GratitudeApiInterface,
        cache: GratitudeDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork,
    ): FetchGratitude {
        return FetchGratitude(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideInsertGratitude(
        appDataStoreManager: AppDataStore,
        service: GratitudeApiInterface,
        cache: GratitudeDao,
        mySharedPreferences: MySharedPreferences,
       checkNetwork: CheckNetwork
    ): InsertGratitude {
        return InsertGratitude(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideUpdateGratitude(
        appDataStoreManager: AppDataStore,
        service: GratitudeApiInterface,
        cache: GratitudeDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork
    ): UpdateGratitude {
        return UpdateGratitude(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideDeleteGratitude(
        appDataStoreManager: AppDataStore,
        service: GratitudeApiInterface,
        cache: GratitudeDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork
    ): DeleteGratitude {
        return DeleteGratitude(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

}