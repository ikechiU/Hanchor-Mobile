package com.nextgendevs.hanchor.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nextgendevs.hanchor.business.domain.models.Todo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MySharedPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(Constants.MY_PREF, 0)
    @SuppressLint("CommitPrefEdits")
    private val editor = preferences.edit()!!

    fun storeStringValue(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun storeIntValue(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun storeBooleanValue(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun getStoredString(key: String): String {
        return preferences.getString(key,"")!!
    }

    fun getStoredInt(key: String): Int {
        return preferences.getInt(key,0)
    }

    fun getStoredBooleanDefaultTrue(key: String): Boolean {
        return preferences.getBoolean(key, true)
    }

    fun getStoredBooleanDefaultFalse(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun clearUser() {
        editor.remove(Constants.AUTH_TOKEN).apply()
        editor.remove(Constants.USERID).apply()
        editor.remove(Constants.USERNAME).apply()
        editor.remove(Constants.FIRSTNAME).apply()
        editor.remove(Constants.LASTNAME).apply()
    }

    fun clearPreference(key: String) {
        editor.remove(key).apply()
    }

    fun getStoredLong(key: String): Long {
        return preferences.getLong(key,0L)
    }

    fun storeLongValue(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

}

fun MySharedPreferences.setDefaultTodoId() : Long {
    return if (this.getStoredLong(Constants.TODO_INSERT_ID_LOCAL) == 0L) {
        (0L + 6000L)
    } else {
        (getStoredLong(Constants.TODO_INSERT_ID_LOCAL) + 1L)
    }
}


fun MySharedPreferences.getTodoList() : List<Todo>? {
    val json: String = this.getStoredString(Constants.LIST_OF_TODOS)
    val type = object : TypeToken<List<Todo?>?>() {}.type
    return if (Gson().fromJson<List<Todo>>(json, type) == null)
        null
    else
        Gson().fromJson<List<Todo>>(json, type)
}

