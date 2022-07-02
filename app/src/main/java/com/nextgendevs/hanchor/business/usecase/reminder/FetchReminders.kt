//package com.i_africa.shiftcalenderobajana.business.usecase.reminder
//
//import com.nextgendevs.hanchor.business.datasource.cache.reminder.ReminderDao
//import com.nextgendevs.hanchor.business.datasource.cache.reminder.toReminder
//import com.nextgendevs.hanchor.business.domain.models.Reminder
//import com.nextgendevs.hanchor.business.domain.utils.DataState
//import com.nextgendevs.hanchor.business.usecase.exception.handleUseCaseException
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.flow
//
//class FetchReminders(val cache: ReminderDao) {
//
//    fun execute(): Flow<DataState<List<Reminder>>> = flow {
//
//        val cachedReminders = cache.listReminders().map { it.toReminder()}
//        emit(
//            DataState.data(null, cachedReminders)
//        )
//
//    }.catch { e ->
//        emit(handleUseCaseException(e))
//    }
//
//}