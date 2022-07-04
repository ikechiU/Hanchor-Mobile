package com.nextgendevs.hanchor.di.auth

import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenDao
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.AffirmationDao
import com.nextgendevs.hanchor.business.datasource.network.auth.AuthApiInterface
import com.nextgendevs.hanchor.business.usecase.auth.forgot_password.ForgotPassword
import com.nextgendevs.hanchor.business.usecase.auth.login.Login
import com.nextgendevs.hanchor.business.usecase.auth.register.Register
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthApiService(retrofit: Retrofit): AuthApiInterface {
        return retrofit.create(AuthApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideLogin(
        appDataStoreManager: AppDataStore,
        authTokenCache: AuthTokenDao,
        service: AuthApiInterface,
        mySharedPreferences: MySharedPreferences
    ): Login {
        return Login(appDataStoreManager, authTokenCache, service, mySharedPreferences)
    }

    @Singleton
    @Provides
    fun provideRegister(service: AuthApiInterface): Register {
        return Register(service)
    }

    @Singleton
    @Provides
    fun provideForgotPassword(service: AuthApiInterface): ForgotPassword {
        return ForgotPassword(service)
    }

}