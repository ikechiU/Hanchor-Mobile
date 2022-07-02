//package com.i_africa.shiftcalenderobajana.business.usecase.reminder
//
//import com.nextgendevs.hanchor.business.datasource.cache.reminder.ReminderDao
//import com.nextgendevs.hanchor.business.domain.utils.DataState
//import com.nextgendevs.hanchor.business.domain.utils.MessageType
//import com.nextgendevs.hanchor.business.domain.utils.Response
//import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
//import com.nextgendevs.hanchor.business.usecase.exception.handleUseCaseException
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.flow
//
//class DeleteReminder(val cache: ReminderDao) {
//
//    fun execute(id: Int): Flow<DataState<Int>> = flow {
//
//        val result = cache.deleteReminderById(id)
//        if(result == 1) {
//            emit(
//                DataState.data(
//                    Response("Deleted", UIComponentType.Toast, MessageType.Success),
//                    result
//                )
//            )
//        }
//
//    }.catch { e ->
//        emit(handleUseCaseException(e))
//    }
//
//}