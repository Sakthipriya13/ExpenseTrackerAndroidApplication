package com.example.expensetrackerapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetrackerapplication.data.entity.ExpenseEntity
import com.example.expensetrackerapplication.model.CategoryChartModel
import com.example.expensetrackerapplication.model.DayWiseReportModel
import com.example.expensetrackerapplication.model.PaymentTypeChartModel

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

    @Query("SELECT SUM(ExpenseAmountt) FROM ExpenseTable WHERE ExpenseDate= :curDate")
    suspend fun fnGetDaySummary(curDate : String):Float

    @Query("SELECT SUM(ExpenseAmountt) FROM ExpenseTable WHERE SUBSTR(ExpenseDate,4,2)= :curMonth")
    suspend fun fnGetMonthSummary(curMonth : String):Float

    @Query("SELECT SUM(ExpenseAmountt) FROM ExpenseTable WHERE SUBSTR(ExpenseDate,7,4)= :curYear")
    suspend fun fnGetYearSummary(curYear : String):Float

    @Query("SELECT UserId AS userId" +
            ",ExpenseCategoryId AS categoryId" +
            ",ExpenseCategoryName AS categoryName" +
            ",SUM(ExpenseAmountt) As expenseAmt" +
            " FROM ExpenseTable WHERE ExpenseDate= :day AND ExpenseStatus = :expenseStatus " +
            "GROUP BY ExpenseCategoryId")
    suspend fun fnGetCatDetailsPerDay(day : String,expenseStatus : Int): List<CategoryChartModel>

    @Query(
        "SELECT UserId AS userId" +
                ",ExpenseCategoryId AS categoryId" +
                ",ExpenseCategoryName AS categoryName" +
                ",SUM(ExpenseAmountt) As expenseAmt" +
                " FROM ExpenseTable WHERE SUBSTR(ExpenseDate,4,2)= :month " +
                "GROUP BY ExpenseCategoryId"
    )
    suspend fun fnGetCatDetailsPerMonth(month : String): List<CategoryChartModel>

    @Query("SELECT UserId AS userId" +
            ",ExpenseCategoryId AS categoryId" +
            ",ExpenseCategoryName AS categoryName" +
            ",SUM(ExpenseAmountt) As expenseAmt" +
            " FROM ExpenseTable WHERE SUBSTR(ExpenseDate,7,4)= :year " +
            "GROUP BY ExpenseCategoryId"
    )
    suspend fun fnGetCatDetailsPerYear(year : String): List<CategoryChartModel>

    @Query("SELECT UserId AS userId,SUM(ExpenseAmtInCash) AS paymentType_CashAmt,SUM(ExpenseAmtInCard) AS paymentType_CardAmt," +
            "SUM(ExpenseAmtInUpi) AS paymentType_UpiAmt,SUM(ExpenseAmtInOthers) AS paymentType_OthersAmt " +
            "FROM ExpenseTable WHERE SUBSTR(ExpenseDate,4,2)= :curMonth AND ExpenseStatus = :expenseStatus " +
            "GROUP BY ExpenseAmtInCash AND ExpenseAmtInCard AND ExpenseAmtInUpi AND ExpenseAmtInOthers")
    suspend fun fnGetPaymentTypesForCurMonth(curMonth : String,expenseStatus : Int) : List<PaymentTypeChartModel>

    @Query("SELECT UserId AS userId,SUM(ExpenseAmtInCash) AS paymentType_CashAmt,SUM(ExpenseAmtInCard) AS paymentType_CardAmt," +
            "SUM(ExpenseAmtInUpi) AS paymentType_UpiAmt,SUM(ExpenseAmtInOthers) AS paymentType_OthersAmt FROM ExpenseTable WHERE SUBSTR(ExpenseDate,7,4)= :curYear " +
            "AND ExpenseStatus = :expenseStatus GROUP BY ExpenseAmtInCash AND ExpenseAmtInCard AND ExpenseAmtInUpi AND ExpenseAmtInOthers")
    suspend fun fnGetPaymentTypesForCurYear(curYear : String,expenseStatus : Int) : List<PaymentTypeChartModel>


}