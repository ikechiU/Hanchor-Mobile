package com.nextgendevs.hanchor.business.datasource.cache.datastore


interface AppDataStore {

    suspend fun setValue(key: String, value: String)

    suspend fun readValue(key: String): String?

    suspend fun setTime(key: String, value: Long)

    suspend fun readTime(key: String): Long?

}