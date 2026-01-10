package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var _logoutSatus = MutableLiveData<Boolean>()
    var logoutStatus : LiveData<Boolean> = _logoutSatus

    var _displayTransparentBg = MutableLiveData<Boolean>()
    var displayTransparentBg : LiveData<Boolean> = _displayTransparentBg

    fun fnLogOut()
    {
        _logoutSatus.value = true
    }

}