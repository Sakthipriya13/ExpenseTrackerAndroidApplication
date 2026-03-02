package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.entity.UserEntity
import com.example.expensetrackerapplication.data.repositary.UserRepository
import com.example.expensetrackerapplication.`object`.Global
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel( application: Application) : AndroidViewModel(application)
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

    var _userDetailList = MutableLiveData<List<UserEntity>>()
    var userDetailList : LiveData<List<UserEntity>>  = _userDetailList

    var _loginStatus_success = MutableLiveData<Boolean>(false)
    var loginStatus_success : LiveData<Boolean> = _loginStatus_success

    var _loginStatus_fail = MutableLiveData<Boolean>(false)
    var loginStatus_fail : LiveData<Boolean> = _loginStatus_fail

    var _actionGoToSignUp = MutableLiveData<Boolean>()
    var actionGoToSignUp : LiveData<Boolean> = _actionGoToSignUp

    var _userNameEmptyStatus = MutableLiveData<Boolean>(false)
    var userNameEmptyStatus : LiveData<Boolean> = _userNameEmptyStatus

    var _userPasswordEmptyStatus = MutableLiveData<Boolean>(false)
    var userPasswordEmptyStatus : LiveData<Boolean> = _userPasswordEmptyStatus

    var _bothNameAndPasswordEmptyStatus = MutableLiveData<Boolean>(false)
    var bothNameAndPasswordEmptyStatus : LiveData<Boolean> = _bothNameAndPasswordEmptyStatus


    var _clearAllFields = MutableLiveData<Boolean>()
    var clearAllFields : LiveData<Boolean> = _clearAllFields

    var _isPasswordForget = MutableLiveData<Boolean>(false)
    var isPasswordForget : LiveData<Boolean> = _isPasswordForget



    fun fnIsForgetPassword(){
        _isPasswordForget.value = true
    }

    fun fnCheckUser()
    {
        viewModelScope.launch {
            when{

                userName.value.isNullOrBlank() &&  userPassword.value.isNullOrBlank() -> {
                    _bothNameAndPasswordEmptyStatus.value=true
                }

                userName.value.isNullOrBlank()-> {
                    _userNameEmptyStatus.value=true
                }

                userPassword.value.isNullOrBlank()-> {
                    _userPasswordEmptyStatus.value=true
                }

                else -> {
                    _userDetailList.value=userRepository.fnGetUserDetailsBasedOnUserName(userName.value, userPassword.value)
                    var result= _userDetailList.value?.isNotEmpty()
                    Log.v("USER DETAILS","User Details: ${userDetailList.value}")
                    if(result==false){
                        Global.lUserId =-1
                        Global.lUserName=""
                        Global.lUserPassword=""
                        Global.lUserMobileNo=""
                        Global.lUssrEmail=""
                        _loginStatus_fail.value=true
                    }
                    else{
                        Global.lUserId = userDetailList.value?.firstOrNull()?.userId ?: -1
                        Global.lUserName=userDetailList.value?.firstOrNull()?.userName ?: ""
                        Global.lUserPassword=userDetailList.value?.firstOrNull()?.userPassword ?: ""
                        Global.lUserMobileNo=userDetailList.value?.firstOrNull()?.userMobileNo ?: ""
                        Global.lUssrEmail=userDetailList.value?.firstOrNull()?.userEmail ?: ""
                        _loginStatus_success.value=true
                    }
                }

            }
//            if(userName.value?.isNotEmpty() == true && userPassword.value?.isNotEmpty() == true)
//            {
//                _userDetailList.value=userRepository.fnGetUserDetailsBasedOnUserName(userName.value, userPassword.value)
//                _loginStatus.value= _userDetailList.value?.isNotEmpty()
//                Log.v("USER DETAILS","User Details: $userDetailList")
//            }
//            else
//            {
//                _loginStatus.value=false
//            }
        }
    }

    fun fnClearValues()
    {
        _clearAllFields.value=true
        _userName.value=""
        _userPassword.value=""
    }


    fun fnActionGoToSignUp()
    {
        _actionGoToSignUp.value=true
    }
}