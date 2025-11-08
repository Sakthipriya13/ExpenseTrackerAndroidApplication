package com.example.expensetrackerapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel: ViewModel()
{
    var _userName = MutableLiveData<String?>("")
    var userName : LiveData<String?> = _userName

    var _userPassword = MutableLiveData<String?>("")
    var userPassword : LiveData<String?> = _userPassword

    var _loginStatus = MutableLiveData<Boolean>(false)
    var loginStatus : LiveData<Boolean> = _loginStatus

    fun fnCheckUser()
    {

    }

    fun fnClearValues()
    {
        _userName.value=""
        _userPassword.value=""
    }
}