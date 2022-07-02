//package com.i_africa.shiftcalenderobajana.business.usecase.reminder
//
//
//import com.nextgendevs.hanchor.business.datasource.cache.reminder.ReminderDao
//import com.nextgendevs.hanchor.business.datasource.cache.reminder.toReminderEntity
//import com.nextgendevs.hanchor.business.domain.models.Reminder
//import com.nextgendevs.hanchor.business.domain.utils.DataState
//import com.nextgendevs.hanchor.business.domain.utils.MessageType
//import com.nextgendevs.hanchor.business.domain.utils.Response
//import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
//import com.nextgendevs.hanchor.business.usecase.exception.handleUseCaseException
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.flow
//
//class UpdateReminder(val cache: ReminderDao) {
//
//    fun execute(reminder: Reminder): Flow<DataState<Int>> = flow {
//
//        val result = cache.updateReminder(reminder.toReminderEntity())
//        if(result == 1) {
//            emit(
//                DataState.data(
//                    Response("Updated", UIComponentType.Toast, MessageType.Success),
//                    result
//                )
//            )
//        }
//    }.catch { e ->
//        emit(handleUseCaseException(e))
//    }
//
//}