package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NewExpenseViewModel(application: Application) : AndroidViewModel(application)
{

    var _selectedDate = MutableLiveData<String?>("")
    var selectedDate : LiveData<String?> = _selectedDate

    var _expenseAmt = MutableLiveData<String?>()
    var expenseAmt : LiveData<String?> = _expenseAmt


}