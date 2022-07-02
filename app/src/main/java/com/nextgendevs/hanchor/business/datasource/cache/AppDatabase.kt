package com.nextgendevs.hanchor.business.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenDao
import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenEntity
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeDao
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeEntity
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.TodoDao
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.TodoEntity

@Database(entities = [AuthTokenEntity::class, TodoEntity::class, GratitudeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun authTokenDao(): AuthTokenDao
    abstract fun gratitudeDao(): GratitudeDao
    abstract fun todoDao(): TodoDao
}