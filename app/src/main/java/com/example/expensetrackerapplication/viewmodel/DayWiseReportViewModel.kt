package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DayWiseReportViewModel(application : Application) : AndroidViewModel(application = application)
{
    var _closeDayWiseReport = MutableLiveData<Boolean>()
    var closeDayWiseReport : LiveData<Boolean> = _closeDayWiseReport

    fun fnCloseDayWiseReport()
    {
        _closeDayWiseReport.value=true
    }
}