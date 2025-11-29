package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.dao.UserDao
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.entity.UserEntity
import com.example.expensetrackerapplication.data.repositary.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    var userRepository: UserRepository

    init {
        val userDao= AppDatabase.getdatabase(application).userDao()
        userRepository= UserRepository(userDao)

    }

    //User Name
    var _name = MutableLiveData<String?>()
    var  name : LiveData<String?> = _name

    //User Email
    var _email = MutableLiveData<String?>()
    var email : LiveData<String?> = _email

    //User MobileNo
    var _mobileNo = MutableLiveData<String?>()
    var mobileNo : LiveData<String?> = _mobileNo

    //User Password
    var _password = MutableLiveData<String?>()
    var password : LiveData<String?> = _password

    //GoToSignUp
    var _actionGoToLogin = MutableLiveData<Boolean>()
    var actionGoToLogin : LiveData<Boolean> = _actionGoToLogin

    var _insertStatus = MutableLiveData<Boolean>()
    var insertStatus : LiveData<Boolean> = _insertStatus


    fun fnClearInputs()
    {
        _name.value="";
        _mobileNo.value="";
        _password.value="";
        _email.value="";
    }

    fun fnGoToLogin()
    {
        _actionGoToLogin.value = true
    }

    fun fnStoreUserDetails()
    {
        viewModelScope.launch {
            val user= UserEntity(
                userName = name.value, userMobileNo = mobileNo.value,
                userEmail = email.value, userPassword = password.value, userId = 0
            )
            val status = userRepository.fnInsertUserDetails(user)
            _insertStatus.value=status
        }
    }

}