package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.dao.UserDao
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.repositary.UserRepository
import com.example.expensetrackerapplication.`object`.Global
import kotlinx.coroutines.launch

class ProfileViewModel(application : Application) : AndroidViewModel(application = application)
{
    private  var userRepository  : UserRepository

    init{
        var userDao = AppDatabase.getdatabase(application).userDao()
        userRepository= UserRepository(userDao)
    }
    var _lUserName = MutableLiveData<String>(Global.lUserName)
    var lUserName : LiveData<String> = _lUserName

    var _lUserMobileNo = MutableLiveData<String>(Global.lUserMobileNo)
    var lUserMobileNo : LiveData<String> = _lUserMobileNo

    var _lUserEmail = MutableLiveData<String>(Global.lUssrEmail)
    var lUserEmail : LiveData<String> = _lUserEmail

    var _isChangePassword = MutableLiveData<Boolean>()
    var isChangePassword : LiveData<Boolean> = _isChangePassword

    var _isAddIncome = MutableLiveData<Boolean>()
    var isAddIncome : LiveData<Boolean> = _isAddIncome

    var _isEdit = MutableLiveData<Boolean>()
    var isEdit : LiveData<Boolean> = _isEdit

    var _isDelAccount = MutableLiveData<Boolean>()
    var isDelAccount : LiveData<Boolean> = _isDelAccount

    var _deleteUserAcStatus = MutableLiveData<Boolean>()
    var deleteUserAcStatus : LiveData<Boolean> = _deleteUserAcStatus

    fun onClickEditProfilePicture(){
        _isEdit.value=true
    }

    fun onClickAddIncome(){
        _isAddIncome.value=true
    }

    fun onClickChangePassword(){
        _isChangePassword.value=true
    }

    fun onClickDeleteAccount(){
        _isDelAccount.value=true
    }

    fun fnDeleteUserAccount()  {
        viewModelScope.launch {
            try{
                _deleteUserAcStatus.value= userRepository.fnDeleteUser(Global.lUserId)
            }
            catch (e : Exception){
                Log.e("DELETE USER ACCOUNT","Delete User Account: ${e.message}")
                _deleteUserAcStatus.value = false
            }
        }
    }

}