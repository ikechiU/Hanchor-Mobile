package com.nextgendevs.hanchor.business.datasource.cache.main.todo

import androidx.room.*
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todoEntity: TodoEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodo(todoEntity: TodoEntity): Int

    @Delete()
    suspend fun deleteJournal(todoEntity: TodoEntity): Int

    @Query("DELETE FROM todo_table WHERE _id = :id")
    suspend fun deleteTodoById(id: Long): Int

    @Delete()
    suspend fun deleteTodos(todos: List<TodoEntity>)

    @Query("SELECT * FROM todo_table WHERE _id = :id")
    suspend fun fetchTodo(id: Long): TodoEntity

    @Query("SELECT * FROM todo_table ORDER BY todo_date DESC LIMIT (:page * :limit)")
    suspend fun fetchTodos(
        page: Int,
        limit: Int = 10
    ): List<TodoEntity>

    @Query("SELECT * FROM todo_table ORDER BY todo_date DESC")
    suspend fun fetchTodos(): List<TodoEntity>
}