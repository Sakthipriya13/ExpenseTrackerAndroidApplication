package com.example.expensetrackerapplication.ui_event

sealed class ResultState {
    data class success(var message : String) : ResultState()
    data class fail(var message : String) : ResultState()
}