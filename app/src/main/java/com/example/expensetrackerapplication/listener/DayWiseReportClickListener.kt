package com.example.expensetrackerapplication.listener

import com.example.expensetrackerapplication.model.DayWiseReportModel

interface DayWiseReportClickListener
{
    fun onDeleteClick(expense : DayWiseReportModel)
}