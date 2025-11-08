package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.entity.UserEntity
import com.example.expensetrackerapplication.data.repositary.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application)
{
    var userRepository : UserRepository
    init {
        val userDao= AppDatabase.getdatabase(application).userDao()
        userRepository= UserRepository(userDao)
    }
    var _userName = MutableLiveData<String?>("")
    var userName : LiveData<String?> = _userName

    var _userPassword = MutableLiveData<String?>("")
    var userPassword : LiveData<String?> = _userPassword

    var _userDetailList = MutableStateFlow<MutableList<UserEntity>>(mutableListOf())
    var userDetailList : StateFlow<MutableList<UserEntity>> = _userDetailList
    var _loginStatus = MutableLiveData<Boolean>(false)
    var loginStatus : LiveData<Boolean> = _loginStatus

    var _actionGoToSignUp = MutableLiveData<Boolean>()
    var actionGoToSignUp : LiveData<Boolean> = _actionGoToSignUp

    fun fnCheckUser()
    {
        viewModelScope.launch {
            if(userName.value?.isNotEmpty() == true && userPassword.value?.isNotEmpty() == true)
            {
                userRepository.fnGetUserDetailsBasedOnUserName(userName.value,
                    userPassword.value).collect { list ->
                    _userDetailList.value=list
                    _loginStatus.value=true
                }
            }
            else
            {
                _loginStatus.value=false
            }
        }
    }

    fun fnClearValues()
    {
        _userName.value=""
        _userPassword.value=""
    }


    fun fnActionGoToSignUp()
    {
        _actionGoToSignUp.value=false
    }
}