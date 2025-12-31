package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ReportMenuViewModel(application : Application) : AndroidViewModel(application = application)
{
    var _showDayWiseReport = MutableLiveData<Boolean>()
    var showDayWiseReport : LiveData<Boolean> = _showDayWiseReport

    var _showMonthlyReport = MutableLiveData<Boolean>()
    var showMonthlyReport : LiveData<Boolean> = _showMonthlyReport

    var _showWeeklyReport = MutableLiveData<Boolean>()
    var showWeeklyReport : LiveData<Boolean> = _showWeeklyReport

    var _showCategoryReport = MutableLiveData<Boolean>()
    var showCategoryReport : LiveData<Boolean> = _showCategoryReport

    fun fnShowDayWiseReport()
    {
        _showDayWiseReport.value=true
    }

    fun fnShowWeeklyReport()
    {
        _showWeeklyReport.value=true
    }

    fun fnShowMonthlyReport()
    {
        _showMonthlyReport.value=true
    }

    fun fnShowCategoryReport()
    {
        _showCategoryReport.value=true
    }
}