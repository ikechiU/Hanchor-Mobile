package com.nextgendevs.hanchor.presentation.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.TodoDao
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.toTodo
import com.nextgendevs.hanchor.business.domain.models.Todo
import com.nextgendevs.hanchor.presentation.broadcast.reminder.scheduleReminder
import com.nextgendevs.hanchor.presentation.utils.setNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AppDebug"

@AndroidEntryPoint
class RescheduleReminderService: LifecycleService() {

    private lateinit var ids: Array<String?>
    private lateinit var tasks: Array<String?>
    private lateinit var times: Array<String?>
    private lateinit var isCompleted: Array<String?>

    @Inject lateinit var cache: TodoDao
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = this.setNotification("Hanchor", "Rescheduling Todos")

            val id = (System.currentTimeMillis() / 1000000).toInt()
            startForeground(id, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        scope.launch {
            val cachedTodos:List<Todo>  = cache.fetchTodos().map { it.toTodo() }
            rescheduleExactTodo(cachedTodos, this@RescheduleReminderService)

            Log.d(TAG, "onStartCommand: RescheduleReminderService cachedTodos $cachedTodos")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                stopForeground(true)
            }
        }

        return START_STICKY
    }

    private fun rescheduleExactTodo(todos: List<Todo>, context: Context) {
        if (todos.isNotEmpty()) {
            initializeArray(todos)
            assignArrayValues(todos)

            context.scheduleReminder(ids, tasks, times, isCompleted)
        }
    }

    private fun initializeArray(todos: List<Todo>) {
        ids = arrayOfNulls(todos.size)
        tasks = arrayOfNulls(todos.size)
        times = arrayOfNulls(todos.size)
        isCompleted = arrayOfNulls(todos.size)
    }

    private fun assignArrayValues(todos: List<Todo>) {
        for (i in todos.indices) {
            ids[i] = todos[i].id.toString()
            tasks[i] = todos[i].todoTask
            times[i] = todos[i].todoDate.toString()
            isCompleted[i] = todos[i].todoIsCompleted.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}