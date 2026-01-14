package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.expensetrackerapplication.`object`.Global

class AddInComeViewModel(application : Application) : AndroidViewModel(application)
{
    var _selectedDate = MutableLiveData<String>(Global.fnGetCurrentDate())
    var selectedDate : LiveData<String> = _selectedDate


    var _income = MutableLiveData<String>(Global.fnGetCurrentDate())
    var income : LiveData<String> = _income
}