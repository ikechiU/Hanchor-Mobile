package com.nextgendevs.hanchor.presentation

interface UICommunicationListener {
    fun displayProgressBar(isLoading: Boolean)
    fun hideSoftKeyboard()
}