package com.nextgendevs.hanchor.di.main.affirmation

import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.AffirmationDao
import com.nextgendevs.hanchor.business.datasource.network.main.affirmation.AffirmationApiInterface
import com.nextgendevs.hanchor.business.usecase.CheckNetwork
import com.nextgendevs.hanchor.business.usecase.main.affirmation.DeleteAffirmation
import com.nextgendevs.hanchor.business.usecase.main.affirmation.InsertAffirmation
import com.nextgendevs.hanchor.business.usecase.main.affirmation.UpdateAffirmation
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AffirmationModule {

    @Singleton
    @Provides
    fun provideAffirmationApiService(retrofit: Retrofit): AffirmationApiInterface {
        return retrofit.create(AffirmationApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideInsertAffirmation(
        appDataStoreManager: AppDataStore,
        service: AffirmationApiInterface,
        cache: AffirmationDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork
    ): InsertAffirmation {
        return InsertAffirmation(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideUpdateAffirmation(
        appDataStoreManager: AppDataStore,
        service: AffirmationApiInterface,
        cache: AffirmationDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork
    ): UpdateAffirmation {
        return UpdateAffirmation(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideDeleteAffirmation(
        appDataStoreManager: AppDataStore,
        service: AffirmationApiInterface,
        cache: AffirmationDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork
    ): DeleteAffirmation {
        return DeleteAffirmation(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

}