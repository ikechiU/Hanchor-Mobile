package com.nextgendevs.hanchor.business.datasource.cache.main.gratitude

import androidx.room.*

@Dao
interface GratitudeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGratitude(gratitudeEntity: GratitudeEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGratitude(gratitudeEntity: GratitudeEntity): Int

    @Query("DELETE FROM gratitude_table WHERE _id = :id")
    suspend fun deleteGratitudeById(id: Long): Int

    @Delete()
    suspend fun deleteGratitudes(gratitudeEntities: List<GratitudeEntity>)

    @Query("SELECT * FROM gratitude_table WHERE _id = :id")
    suspend fun fetchGratitude(id: Long): GratitudeEntity

    @Query("SELECT * FROM gratitude_table ORDER BY _id DESC LIMIT (:page * :limit)")
    suspend fun fetchGratitudes(
        page: Int,
        limit: Int = 10
    ): List<GratitudeEntity>
}