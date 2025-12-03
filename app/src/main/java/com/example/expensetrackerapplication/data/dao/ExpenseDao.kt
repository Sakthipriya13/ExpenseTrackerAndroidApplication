package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
import com.example.expensetrackerapplication.data.entity.UserEntity

@Dao
interface ExpenseDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun fnInsertNewExpense(expenseEntity: ExpenseEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fnInsertNewExpense(expenseEntity:ExpenseEntity) : Long

    @Query("SELECT * FROM ExpenseTable")
    suspend fun fnGetAllExpense(): List<ExpenseEntity>

}