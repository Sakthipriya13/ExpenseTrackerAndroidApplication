package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
import com.example.expensetrackerapplication.model.DayWiseReportModel

@Dao
interface ExpenseDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun fnInsertNewExpense(expenseEntity: ExpenseEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertNewExpense(expenseEntity:ExpenseEntity) : Long

    @Query("SELECT * FROM ExpenseTable")
    suspend fun fnGetAllExpense(): List<ExpenseEntity>

    @Query("SELECT * FROM ExpenseTable WHERE ExpenseDate= :date AND UserId= :luserId")
    suspend fun fnGetExpensePerDate(date: String?,luserId : Int): List<ExpenseEntity>

    @Query("UPDATE ExpenseTable SET ExpenseStatus = :delExpense WHERE expenseId= :id AND UserId= :luserId")
    suspend fun fnDeleteExpensePerId(id: Int?, delExpense: Int,luserId : Int) : Int
}