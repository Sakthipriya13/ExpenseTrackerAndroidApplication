package com.example.expensetrackerapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {
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
    var _actionGoToSignUp = MutableLiveData<Boolean>()
    var actionGoToSignUp : LiveData<Boolean> = _actionGoToSignUp

    fun fnClearInputs()
    {
        _name.value="";
        _mobileNo.value="";
        _password.value="";
        _email.value="";
    }

    fun fnGoToSignUp()
    {
        _actionGoToSignUp.value=true
    }



}