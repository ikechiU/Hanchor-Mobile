package com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list.viewmodel

import com.nextgendevs.hanchor.business.domain.models.Todo
import com.nextgendevs.hanchor.business.domain.utils.Queue
import com.nextgendevs.hanchor.business.domain.utils.StateMessage

data class TodoState (
    val isLoading: Boolean = false,
    val insertResult: Long = 0,
    val updateResult: Int = 0,
    val deleteResult: Int = 0,
    val page: Int = 1,
    val isQueryExhausted: Boolean = false,
    val tokenExpired: Boolean = false,
    val todo: Todo = Todo(-1L, "", "", -1L, true),
    val todoList: List<Todo> = emptyList(),
    val queue: Queue<StateMessage> = Queue(mutableListOf())
)
