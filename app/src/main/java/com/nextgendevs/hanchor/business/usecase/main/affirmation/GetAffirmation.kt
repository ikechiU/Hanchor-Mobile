package com.nextgendevs.hanchor.business.usecase.main.affirmation

import android.util.Log
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.AffirmationDao
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.toAffirmation
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
import com.nextgendevs.hanchor.business.domain.models.Affirmation
import com.nextgendevs.hanchor.business.domain.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val USERNAME_REQUIRED = "Username required"

class GetAffirmation (
    val cache: AffirmationDao
) {

    fun execute(): Flow<DataState<List<String>>> = flow {

        emit(DataState.loading<List<String>>())

        val cachedAffirmations = cache.fetchAffirmations(1, 20).map { it.toAffirmation().affirmation }

        emit(
            DataState.data(response = null, data = cachedAffirmations)
        )

    }.catch { e ->
        emit(handleUseCaseException(e))
    }


    fun execute(query: String): Flow<DataState<List<Affirmation>>> = flow {

        emit(DataState.loading<List<Affirmation>>())

        val cachedAffirmations = cache.fetchAffirmations(query).map { it.toAffirmation() }

        emit(
            DataState.data(response = null, data = cachedAffirmations)
        )

    }.catch { e ->
        emit(handleUseCaseException(e))
    }


}