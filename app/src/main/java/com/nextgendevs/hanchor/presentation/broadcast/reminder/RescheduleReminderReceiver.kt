package com.nextgendevs.hanchor.presentation.broadcast.reminder

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.nextgendevs.hanchor.business.domain.models.Todo
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import com.nextgendevs.hanchor.presentation.utils.getTodoList
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "AppDebug"

@AndroidEntryPoint
class RescheduleReminderReceiver : BroadcastReceiver() {
    @Inject
    lateinit var mySharedPreferences: MySharedPreferences
    private lateinit var ids: Array<String?>
    private lateinit var tasks: Array<String?>
    private lateinit var times: Array<String?>
    private lateinit var isCompleted: Array<String?>


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == "android.intent.action.BOOT_COMPLETED" || intent.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
                val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                if (Build.VERSION.SDK_INT >= 31) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        rescheduleExactReminder(context)
                    }
                } else {
                    rescheduleExactReminder(context)
                }
            }
        }
    }

    private fun getEntityList(): List<Todo>? {
        Log.d(TAG, "getEntityList: ${mySharedPreferences.getTodoList()}")
        return mySharedPreferences.getTodoList()
    }

    private fun rescheduleExactReminder(context: Context) {
        val reminders = getEntityList()
        Log.d(TAG, "scheduleReminder: after reboot...$reminders")
        if (reminders != null && reminders.isNotEmpty()) {
            initializeArray(reminders)
            assignArrayValues(reminders)

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
}