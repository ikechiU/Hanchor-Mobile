package com.nextgendevs.hanchor.di.main.todo

import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenDao
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.TodoDao
import com.nextgendevs.hanchor.business.datasource.network.main.todo.TodoApiInterface
import com.nextgendevs.hanchor.business.usecase.CheckNetwork
import com.nextgendevs.hanchor.business.usecase.main.todo.*
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoModule {

    @Singleton
    @Provides
    fun provideTodoApiService(retrofit: Retrofit): TodoApiInterface {
        return retrofit.create(TodoApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideFetchTodos(
        appDataStoreManager: AppDataStore,
        service: TodoApiInterface,
        cache: TodoDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork,
    ): FetchTodos {
        return FetchTodos(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideFetchTodo(
        appDataStoreManager: AppDataStore,
        service: TodoApiInterface,
        cache: TodoDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork,
    ): FetchTodo {
        return FetchTodo(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideInsertTodo(
        appDataStoreManager: AppDataStore,
        authTokenCache: AuthTokenDao,
        service: TodoApiInterface,
        cache: TodoDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork
    ): InsertTodo {
        return InsertTodo(
            appDataStoreManager,
            authTokenCache,
            service,
            cache,
            mySharedPreferences,
            checkNetwork
        )
    }

    @Singleton
    @Provides
    fun provideUpdateTodo(
        appDataStoreManager: AppDataStore,
        service: TodoApiInterface,
        cache: TodoDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork
    ): UpdateTodo {
        return UpdateTodo(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

    @Singleton
    @Provides
    fun provideDeleteTodo(
        appDataStoreManager: AppDataStore,
        service: TodoApiInterface,
        cache: TodoDao,
        mySharedPreferences: MySharedPreferences,
        checkNetwork: CheckNetwork
    ): DeleteTodo {
        return DeleteTodo(appDataStoreManager, service, cache, mySharedPreferences, checkNetwork)
    }

}