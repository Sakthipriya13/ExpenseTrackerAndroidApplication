package com.example.expensetrackerapplication.data.repositary

import android.util.Log
import com.example.expensetrackerapplication.data.dao.ExpenseDao
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
import com.example.expensetrackerapplication.model.CategoryChartModel
import com.example.expensetrackerapplication.model.DayWiseExpenseSummaryModel
import com.example.expensetrackerapplication.model.DayWiseReportModel
import com.example.expensetrackerapplication.`object`.Global

class ExpenseRepository(val expenseDao: ExpenseDao)
{
    suspend fun fnInsertExpenseDatasToDb(expenseEntity : ExpenseEntity) : Boolean
    {
        var result = expenseDao.fnInsertNewExpense(expenseEntity)

        if(result > 0)
        {
            return true
        }
        else
        {
            return false
        }
    }

    suspend fun fnGetExpenseDetailsPerDate(date: String?): List<ExpenseEntity>{
        try {
            return expenseDao.fnGetExpensePerDate(date,Global.lUserId)
        }
        catch (e : Exception){
            Log.e("GET EXPENSE DETAILS PER DATE","Get Expense Details Per Date: ${e.message}")
            return listOf()
        }
    }

    suspend fun fnDeleteExpense(expenseId: Int?): Boolean {
        try
        {
            var delStatus = expenseDao.fnDeleteExpensePerId(expenseId,Global.EXPENSE_STATUS_DELETED,Global.lUserId)
            if(delStatus>0)
                return true
            else
                return false
        }
        catch (e : Exception){
            Log.e("DELETE EXPENSE","Delete Expense: ${e.message}")
            return false
        }
    }

    suspend fun fnGetDaySummary(curDate : String): Float
    {
        return try {
            expenseDao.fnGetDaySummary(curDate)
        }
        catch (e : Exception){
            Log.e("GET CURRENT DAY SUMMARY AMOUNT","Get Current Day Summary Amount: ${e.message}")
            0.0f
        }
    }

    suspend fun fnGetMonthSummary(curMonth : String): Float
    {
        return try {
            expenseDao.fnGetMonthSummary(curMonth)
        }
        catch (e : Exception){
            Log.e("GET CURRENT DAY SUMMARY AMOUNT","Get Current Day Summary Amount: ${e.message}")
            0.0f
        }
    }

    suspend fun fnGetYearSummary(curYear : String): Float
    {
        return try {
            expenseDao.fnGetYearSummary(curYear)
        }
        catch (e : Exception){
            Log.e("GET CURRENT DAY SUMMARY AMOUNT","Get Current Day Summary Amount: ${e.message}")
            0.0f
        }
    }

    suspend fun fnGetCateDetailsPerDay(day : String): List<CategoryChartModel>
    {
        return try {
            expenseDao.fnGetCatDetailsPerDay(day)
        }
        catch (e : Exception){
            Log.e("GET CURRENT DAY SUMMARY AMOUNT","Get Current Day Summary Amount: ${e.message}")
            emptyList<CategoryChartModel>()
        }
    }

    suspend fun fnGetCateDetailsPerMonth(month : String): List<CategoryChartModel>
    {
        return try {
            expenseDao.fnGetCatDetailsPerMonth(month)
        }
        catch (e : Exception){
            Log.e("GET CURRENT DAY SUMMARY AMOUNT","Get Current Day Summary Amount: ${e.message}")
            emptyList<CategoryChartModel>()
        }
    }

    suspend fun fnGetCateDetailsPerYear(year : String): List<CategoryChartModel>
    {
        return try {
            expenseDao.fnGetCatDetailsPerYear(year)
        }
        catch (e : Exception){
            Log.e("GET CURRENT DAY SUMMARY AMOUNT","Get Current Day Summary Amount: ${e.message}")
            emptyList<CategoryChartModel>()
        }
    }

    suspend fun fnGetCateDetailsPerYear(month:String,year : String): List<DayWiseExpenseSummaryModel>
    {
        return try {
            expenseDao.fnGetMonthWiseExpense(month,year)
        }
        catch (e : Exception){
            Log.e("GET CURRENT DAY SUMMARY AMOUNT","Get Current Day Summary Amount: ${e.message}")
            emptyList<DayWiseExpenseSummaryModel>()
        }
    }
}