package com.example.expensetrackerapplication.ui_event

import com.example.expensetrackerapplication.model.CategoryModel
import com.example.expensetrackerapplication.model.DayWiseReportModel

interface DayWiseReportClickListener
{
    fun onDeleteClick(expense : DayWiseReportModel)

}