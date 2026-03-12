package com.example.expensetrackerapplication.data.repositary

import android.util.Log
import com.example.expensetrackerapplication.data.dao.IncomeDao
import com.example.expensetrackerapplication.data.entity.IncomeEntity
import com.example.expensetrackerapplication.`object`.Global
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class IncomeRepository(val incomeDao : IncomeDao) {
    var fireStore = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    suspend fun fnInsertIncome(income: IncomeEntity): Boolean{
        return try {
            var result = incomeDao.fnInsertIncome(income)
            if(result <= 0)
            {
                Log.e("INSERT INCOME TO LOCAL STATUS","Insert Income To Local Status: Failed")
                return false
            }

            true
        }
        catch (e : Exception){
            Log.e("INSERT INCOME TO LOCAL AND CLOUD","Insert Income To Local And Cloud: Failed(${e.message})")
            false
        }
    }

    suspend fun fnGetIncomePerMonth(curMonth : String): Float{
        return try {
            incomeDao.fnGetIncomePerMonth(curMonth,Global.lUserId)
        }
        catch (e : Exception){
            Log.e("GET INCOME PER MONTH","Get Income Per Month: ${e.message}")
            0.0f
        }
    }

    suspend fun fnGetIncomePerMonthAndYear(month : String,year:String): Float{
        return try {
            incomeDao.fnGetIncomePerMonthAndYear(month,year,Global.lUserId)
        }
        catch (e : Exception){
            Log.e("GET INCOME PER MONTH","Get Income Per Month: ${e.message}")
            0.0f
        }
    }

    suspend fun fnGetIncomePerYear(year : String): Float{
        return try {
            incomeDao.fnGetIncomePerYear(year,Global.lUserId)
        }
        catch (e : Exception){
            Log.e("GET INCOME PER YEAR","Get Income Per Year: ${e.message}")
            0.0f
        }
    }
}