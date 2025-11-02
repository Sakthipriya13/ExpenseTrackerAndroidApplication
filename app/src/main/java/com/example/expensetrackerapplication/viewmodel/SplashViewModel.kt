package com.example.expensetrackerapplication.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel() : ViewModel()
{

    private val _navigateToLogin = MutableStateFlow(false)

    val navigateToLogin = _navigateToLogin.asStateFlow()

    // The code inside the init block runs automatically when the viewmodel is created
    init {
        viewModelScope.launch {
            delay(2500)
            _navigateToLogin.value=true
        }
    }


}