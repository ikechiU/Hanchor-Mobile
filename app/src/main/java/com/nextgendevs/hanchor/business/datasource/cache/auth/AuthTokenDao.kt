package com.nextgendevs.hanchor.business.datasource.cache.auth

import androidx.room.*

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthToken(authTokenEntity: AuthTokenEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAuthToken(authTokenEntity: AuthTokenEntity): Int

    @Query("SELECT * FROM auth_token_table WHERE tokenId = :id")
    suspend fun fetchAuthToken(id: String): AuthTokenEntity

}