package com.nextgendevs.hanchor.business.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenDao
import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenEntity
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.AffirmationDao
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.AffirmationEntity
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeDao
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeEntity
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.TodoDao
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.TodoEntity

@Database(entities = [AffirmationEntity::class, AuthTokenEntity::class, TodoEntity::class, GratitudeEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun affirmationDao(): AffirmationDao
    abstract fun authTokenDao(): AuthTokenDao
    abstract fun gratitudeDao(): GratitudeDao
    abstract fun todoDao(): TodoDao
}