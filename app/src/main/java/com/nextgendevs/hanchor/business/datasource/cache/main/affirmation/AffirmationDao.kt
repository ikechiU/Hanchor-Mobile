package com.nextgendevs.hanchor.business.datasource.cache.main.affirmation

import androidx.room.*

@Dao
interface AffirmationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAffirmation(affirmationEntity: AffirmationEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAffirmation(affirmationEntity: AffirmationEntity): Int

    @Query("DELETE FROM affirmation_table WHERE _id = :id")
    suspend fun deleteAffirmationById(id: Long): Int

    @Delete()
    suspend fun deleteAffirmations(affirmationEntities: List<AffirmationEntity>)

    @Query("SELECT * FROM affirmation_table WHERE _id = :id")
    suspend fun fetchAffirmation(id: Long): AffirmationEntity

    //@Query("SELECT * FROM todo_table ORDER BY todo_date DESC LIMIT (:page * :limit)")
    @Query("SELECT * FROM affirmation_table ORDER BY _id ASC LIMIT (:page * :limit)")
    suspend fun fetchAffirmations(
        page: Int,
        limit: Int = 14
    ): List<AffirmationEntity>

    @Query("SELECT * FROM affirmation_table WHERE affirmation_title = :query")
    suspend fun fetchAffirmations(query: String): List<AffirmationEntity>
//    @Query("SELECT * FROM affirmation_table")
//    suspend fun fetchAffirmations(): List<AffirmationEntity>

}