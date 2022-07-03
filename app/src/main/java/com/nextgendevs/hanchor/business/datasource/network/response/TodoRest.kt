package com.nextgendevs.hanchor.business.datasource.network.response

import com.nextgendevs.hanchor.business.domain.models.Todo

class TodoRest(
    val id: Long,
    val title: String,
    val task: String,
    val date: Long,
    val completed: Boolean
)


fun TodoRest.toTodo() : Todo {
    return Todo(
        id, title, task, date, completed
    )
}