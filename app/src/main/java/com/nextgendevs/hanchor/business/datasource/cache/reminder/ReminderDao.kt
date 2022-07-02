//package com.nextgendevs.hanchor.business.datasource.cache.reminder
//
//import androidx.room.*
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface ReminderDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertReminder(ReminderEntity: ReminderEntity): Long
//
//    @Update()
//    suspend fun updateReminder(ReminderEntity: ReminderEntity): Int
//
//    @Delete()
//    suspend fun deleteReminder(ReminderEntity: ReminderEntity): Int
//
//    @Query("DELETE FROM reminder_table WHERE _id = :id")
//    suspend fun deleteReminderById(id: Int): Int
//
//    @Delete()
//    suspend fun deleteReminders(ReminderEntity: List<ReminderEntity>)
//
//    @Query("SELECT * FROM reminder_table WHERE reminder_time_long = :time")
//    fun findReminderByTime(time: Long): Flow<ReminderEntity>
//
//    @Query("SELECT * FROM reminder_table WHERE _id = :id")
//    suspend fun findReminderById(id: Int): ReminderEntity
//
//    @Query("SELECT * FROM reminder_table")
//    fun readReminders(): Flow<List<ReminderEntity>>
//
//    @Query("SELECT * FROM reminder_table ORDER BY reminder_time_long DESC")
//    suspend fun listReminders(): List<ReminderEntity>
//}