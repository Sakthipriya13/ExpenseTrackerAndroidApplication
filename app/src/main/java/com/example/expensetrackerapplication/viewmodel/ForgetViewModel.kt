package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.repositary.UserRepository
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.ui_event.ResultState
import kotlinx.coroutines.launch

class ForgetViewModel(application: Application) : AndroidViewModel(application = application)
{
    var userRepository : UserRepository

    init {
        var userDao = AppDatabase.getdatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }
    var _email = MutableLiveData<String>()
    var email : LiveData<String> = _email

    var _newPassword = MutableLiveData<String>()
    var newPassword : LiveData<String> = _newPassword

    var _isCancel = MutableLiveData<Boolean>()
    var isCancel : LiveData<Boolean> = _isCancel

    var _resetStatus = MutableLiveData<ResultState>()
    var resetStatus : LiveData<ResultState> = _resetStatus

    fun onClickCancel(){
       _isCancel.value = true
    }

    fun onClickReset(){
        viewModelScope.launch {
            try{
                Log.e("PASSWORD RESET","Email: ${email.value} And New Password: ${newPassword.value}")

                var updateStatus = userRepository.fnResetLoginUserPassword(newPassword =newPassword.value?.trim(),email =email.value?.trim())

                if (updateStatus)
                    _resetStatus.postValue(ResultState.success("Successfully User Password Reset"))
                else
                    _resetStatus.postValue(ResultState.fail("Password Reset Failed1"))
            }
            catch (e : Exception){
                Log.e("PASSWORD RESET","Password Reset: ${e.message}")
                _resetStatus.postValue(ResultState.fail("Password Reset Failed2"))
            }
        }
    }
}