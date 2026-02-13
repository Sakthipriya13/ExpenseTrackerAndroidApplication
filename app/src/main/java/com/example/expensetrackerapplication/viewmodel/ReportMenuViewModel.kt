package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ReportMenuViewModel(application : Application) : AndroidViewModel(application = application)
{
    var _showDayWiseReport = MutableLiveData<Boolean>()
    var showDayWiseReport : LiveData<Boolean> = _showDayWiseReport

    var _showMonthlySummaryReport = MutableLiveData<Boolean>()
    var showMonthlySummaryReport : LiveData<Boolean> = _showMonthlySummaryReport

    var _showCategoryReport = MutableLiveData<Boolean>()
    var showCategoryReport : LiveData<Boolean> = _showCategoryReport

    var _showYearlyReport = MutableLiveData<Boolean>()
    var showYearlyReport : LiveData<Boolean> = _showYearlyReport

    var _showPaymentTypeReport = MutableLiveData<Boolean>()
    var showPaymentTypeReport : LiveData<Boolean> = _showPaymentTypeReport

    fun fnShowDayWiseReport()
    {
        _showDayWiseReport.value=true
    }

    fun fnShowMonthlySummaryReport()
    {
        _showMonthlySummaryReport.value=true
    }

    fun fnShowCategoryReport()
    {
        _showCategoryReport.value=true
    }

    fun fnShowPaymentTypeReport()
    {
        _showPaymentTypeReport.value=true
    }

    fun fnShowYearlyReport(){
        _showYearlyReport.value = true
    }
}