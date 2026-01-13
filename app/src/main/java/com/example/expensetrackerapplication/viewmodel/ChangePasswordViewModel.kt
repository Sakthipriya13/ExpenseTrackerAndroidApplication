package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.repositary.UserRepository
import com.example.expensetrackerapplication.`object`.Global
import kotlinx.coroutines.launch

class ChangePasswordViewModel(application : Application) : AndroidViewModel(application = application)
{
    private var userRepository : UserRepository

    init{
        var userDao = AppDatabase.getdatabase(application).userDao()
        userRepository = UserRepository(userDao)

    }
    var _currentPassword = MutableLiveData<String>()
    var currentPassword : LiveData<String> = _currentPassword

    var _newPassword = MutableLiveData<String>()
    var newPassword : LiveData<String> = _newPassword

    var _changePasswordStatus = MutableLiveData<Boolean>()
    var changePasswordStatus : LiveData<Boolean> = _changePasswordStatus

    fun onClickCancel(){

    }

    fun onClickConfirm(){
          viewModelScope.launch {
              try{
                    var updateStatus = userRepository.fnUpdateLoginUserPassword(newPassword =newPassword.value, userId =Global.lUserId, currentPassword = currentPassword.value)

                    if (updateStatus) _changePasswordStatus.postValue(true) else _changePasswordStatus.postValue(false)
              }
              catch (e : Exception){
                  _changePasswordStatus.postValue(false)
              }
          }
    }
}