package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
import com.example.expensetrackerapplication.data.entity.UserEntity
import com.example.expensetrackerapplication.model.DayWiseReportModel
import com.example.expensetrackerapplication.viewmodel.DayWiseReportViewModel

@Dao
interface ExpenseDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun fnInsertNewExpense(expenseEntity: ExpenseEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertNewExpense(expenseEntity:ExpenseEntity) : Long

    @Query("SELECT * FROM ExpenseTable")
    suspend fun fnGetAllExpense(): List<ExpenseEntity>

    @Query("SELECT ExpenseCategoryId,ExpenseCategoryName,ExpenseAmountt,PaymentType,ExpenseRemarks,ExpenseStatus FROM ExpenseTable WHERE ExpenseDate= :date")
    suspend fun fnGetExpensePerDate(date : String): List<DayWiseReportModel>

}