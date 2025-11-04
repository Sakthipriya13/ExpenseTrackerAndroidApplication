package com.example.expensetrackerapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel: ViewModel()
{
    val _userName = MutableLiveData<String?>("")
    val userName : LiveData<String?> = _userName

    val _userPassword = MutableLiveData<String?>("")
    val userPassword : LiveData<String?> = _userPassword

    fun loginStatus(){

    }
}