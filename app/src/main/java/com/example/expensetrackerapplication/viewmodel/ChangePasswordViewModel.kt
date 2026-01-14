package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.repositary.UserRepository
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.ui_event.ResultState
import kotlinx.coroutines.launch

class ChangePasswordViewModel(application : Application) : AndroidViewModel(application = application)
{
    private var userRepository : UserRepository

    init{
        var userDao = AppDatabase.getdatabase(application).userDao()
        userRepository = UserRepository(userDao)

    }
    var _currentPassword = MutableLiveData<String>(""+Global.lUserPassword)
    var currentPassword : LiveData<String> = _currentPassword

    var _newPassword = MutableLiveData<String>()
    var newPassword : LiveData<String> = _newPassword

    var _result = MutableLiveData<ResultState>()
    var result : LiveData<ResultState> = _result

    var _isCancel = MutableLiveData<Boolean>()
    var isCancel : LiveData<Boolean> = _isCancel

    fun onClickCancel(){
        _isCancel.value = true
    }

    fun onClickConfirm(){
          viewModelScope.launch {
              try{
                    var updateStatus = userRepository.fnUpdateLoginUserPassword(newPassword =newPassword.value, userId =Global.lUserId, currentPassword = currentPassword.value)

                    if (updateStatus)
                        _result.postValue(ResultState.success("Successfully User Password Changed"))
                    else
                        _result.postValue(ResultState.fail("Password Changes Failed"))
              }
              catch (e : Exception){
                  _result.postValue(ResultState.fail("Password Changes Failed"))
              }
          }
    }
}