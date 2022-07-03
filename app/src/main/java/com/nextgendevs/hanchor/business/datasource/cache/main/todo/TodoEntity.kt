package com.nextgendevs.hanchor.business.datasource.cache.main.todo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nextgendevs.hanchor.business.domain.models.Todo

private const val TODO_TABLE = "todo_table"
private const val TODO_ID = "_id"
private const val TODO_TITLE = "todo_title"
private const val TODO_TASK = "todo_task"
private const val TODO_DATE = "todo_date"
private const val TODO_IS_COMPLETED = "todo_is_completed"

@Entity(tableName = TODO_TABLE)
data class TodoEntity constructor(

    @ColumnInfo(name = TODO_ID)
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    @ColumnInfo(name = TODO_TITLE)
    val todoTitle: String,

    @ColumnInfo(name = TODO_TASK)
    val todoTask: String,

    @ColumnInfo(name = TODO_DATE)
    val todoDate: Long,

    @ColumnInfo(name = TODO_IS_COMPLETED)
    val todoIsCompleted: Boolean
)

fun TodoEntity.toTodo(): Todo {
    return Todo(
        id, todoTitle, todoTask, todoDate, todoIsCompleted
    )
}

fun Todo.toTodoEntity(): TodoEntity {
    return TodoEntity(
        id, todoTitle, todoTask, todoDate, todoIsCompleted
    )
}
