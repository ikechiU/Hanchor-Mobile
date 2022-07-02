package com.nextgendevs.hanchor.business.datasource.network.response

import com.nextgendevs.hanchor.business.domain.models.Todo

class TodoRest(
    var id: Long = 0,
    var title: String = "",
    var task: String = "",
    var date: Long = 0,
    var isCompleted: Boolean = false
)


fun TodoRest.toTodo() : Todo {
    return Todo(
        id, title, task, date, isCompleted
    )
}