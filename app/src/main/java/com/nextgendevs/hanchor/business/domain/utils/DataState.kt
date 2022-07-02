package com.nextgendevs.hanchor.business.domain.utils

data class DataState<T>(
    val stateMessage: StateMessage? = null,
    val data: T? = null,
    val isLoading: Boolean = false
) {

    companion object {

        fun <T> error(
            response: Response
        ): DataState<T> {
            return DataState(
                stateMessage = StateMessage(
                    response
                ),
                isLoading = false
            )
        }

        fun <T> data(
            response: Response?,
            data: T? = null
        ): DataState<T> {
            return DataState(
                stateMessage = response?.let {
                    StateMessage(
                        it
                    )
                },
                data = data,
                isLoading = false
            )
        }

        fun <T> loading(response: Response? = null): DataState<T> = DataState(
            isLoading = true,
            stateMessage = response?.let {
                StateMessage(
                    it
                )
            }
        )
    }
}