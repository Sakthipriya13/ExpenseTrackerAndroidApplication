package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.repositary.ExpenseRepository
import com.example.expensetrackerapplication.model.DayWiseReportModel
import com.example.expensetrackerapplication.`object`.Global
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class DayWiseReportViewModel(application : Application) : AndroidViewModel(application = application)
{
    val expenseRepository : ExpenseRepository
    init {
         val dao = AppDatabase.getdatabase(application).ExpenseDao()
         expenseRepository= ExpenseRepository(dao)
    }
    var _closeDayWiseReport = MutableLiveData<Boolean>()
    var closeDayWiseReport : LiveData<Boolean> = _closeDayWiseReport

    var _selectedDate = MutableLiveData<String>(Global.fnGetCurrentDate())
    var selectedDate : LiveData<String> = _selectedDate

    var _expenseSummary = MutableLiveData<String>()
    var expenseSummary : LiveData<String> = _expenseSummary

    var _expenseList = MutableLiveData<List<DayWiseReportModel>>()
    var expenseList : LiveData<List<DayWiseReportModel>> = _expenseList

    var _exportStatus = MutableLiveData<Boolean>()
    var exportStatus : LiveData<Boolean> = _exportStatus

    var _expenseDeleteStatus = MutableLiveData<Boolean>()
    var expenseDeleteStatus : LiveData<Boolean> = _expenseDeleteStatus

    fun fnCloseDayWiseReport()
    {
        _closeDayWiseReport.value=true
    }

    fun fnClearFields(){
        _expenseSummary.value=""
        _expenseList.value = emptyList<DayWiseReportModel>()
    }

    fun fnGetExpenseDetails(date: String?){
        Log.i("SELECTED DATE","Selected Date2: $date")
        viewModelScope.launch {
            try
            {
                var expenseAmtSum = 0.0f
                val res = expenseRepository.fnGetExpenseDetailsPerDate(date)
                var list : MutableList<DayWiseReportModel> = mutableListOf()
                var exPaymentType = ""
                if(res.isNotEmpty())
                {
                    res.forEach { ex ->
                        list.add(DayWiseReportModel(
                            expenseId = ex.expenseId,
                            categoryId= ex.expenseCategoryId,
                            catgeoryName= ex.expenseCategoryName,
                            expenseAmt = Global.fnFormatFloatTwoDigits(ex.expenseAmt.toFloat() ?:0.00f).toString(),
                            expenseRemarks = ex.expenseRemarks,
                            isDelete = if (ex.expenseStatus == Global.EXPENSE_STATUS_DELETED) "DELETED" else "-",
                            paymentType = when{
                                ex.paymentType == Global.PAYMENT_TYPE_CASH -> "CASH"
                                ex.paymentType == Global.PAYMENT_TYPE_CARD -> "CARD"
                                ex.paymentType == Global.PAYMENT_TYPE_UPI -> "UPI"
                                ex.paymentType == Global.PAYMENT_TYPE_OTHER -> "OTHERS"
                                else -> {
                                   when{
                                       (ex.expenseAmtInCash  ?: 0.0f) > 0.0f && (ex.expenseAmtInCard  ?: 0.0f) > 0.0f && (ex.expenseAmtInUpi  ?: 0.0f) > 0.0f -> "CASH,CARD,UPI"
                                       (ex.expenseAmtInCash  ?: 0.0f) > 0.0f && (ex.expenseAmtInCard  ?: 0.0f) > 0.0f -> "CASH,CARD"
                                       (ex.expenseAmtInCash  ?: 0.0f) > 0.0f && (ex.expenseAmtInUpi  ?: 0.0f) > 0.0f -> "CASH,UPI"
                                       (ex.expenseAmtInCard  ?: 0.0f) > 0.0f && (ex.expenseAmtInUpi  ?: 0.0f) > 0.0f -> "CARD,UPI"
                                       (ex.expenseAmtInCash  ?: 0.0f) > 0.0f -> "CASH"
                                       (ex.expenseAmtInCard  ?: 0.0f) > 0.0f -> "CARD"
                                       (ex.expenseAmtInUpi   ?: 0.0f) > 0.0f -> "UPI"
                                       else -> ""
                                   }
                                }
                            }
                        ))
                        expenseAmtSum=expenseAmtSum+ (ex.expenseAmt?.toFloat() ?:0.0f )
                    }
                    _expenseList.value = list
                    _expenseSummary.value= Global.fnFormatFloatTwoDigits(expenseAmtSum) .toString()

                    Log.i("EXPENSE DETAILS PER DATE","Expense Details Per Date1: $list")
                }
                else
                {
                    Log.i("EXPENSE DETAILS PER DATE","Expense Details Per Date2: $list")
                }
            }
            catch (e : Exception)
            {
                Log.e("GET EXPENSE DETAILS","Get Expense Detail Per Date3: ${e.message}")
            }
        }
    }

    fun fnDeleteExpense(expenseId: Int?)
    {
        viewModelScope.launch {
            try
            {
                var delStatus = expenseRepository.fnDeleteExpense(expenseId)

                if(delStatus){
                    fnGetExpenseDetails(selectedDate.value)
                }
            }
            catch(e : Exception)
            {
                Log.e("DELETE Expense","Delete Expense: ${e.message}")
            }
        }
    }

    fun fnExportReport(){
        viewModelScope.launch {

            try
            {
                var start = System.currentTimeMillis()


                var workBook = XSSFWorkbook()
                var sheet = workBook.createSheet("DAY WISE REPORT")

                //Header Row
                var headerRow = sheet.createRow(0)
                headerRow.createCell(0).setCellValue("CATEGORY")
                headerRow.createCell(1).setCellValue("EXPENSE AMOUNT")
                headerRow.createCell(2).setCellValue("PAYMENT TYPE")
                headerRow.createCell(3).setCellValue("REMARKS")
                headerRow.createCell(4).setCellValue("STATUS")

                //Data Row
                expenseList.value?.forEachIndexed { index, expense ->
                    var dataRow = sheet.createRow(index+1)

                    dataRow.createCell(0).setCellValue(expense.catgeoryName)
                    dataRow.createCell(1).setCellValue(expense.expenseAmt)
                    dataRow.createCell(2).setCellValue(expense.paymentType)
                    dataRow.createCell(3).setCellValue(expense.expenseRemarks)
                    dataRow.createCell(4).setCellValue(expense.isDelete)

                }

//                for (i in 0..4){
//                    sheet.autoSizeColumn(i)
//                }

                 _exportStatus.value = fnExportReportToDownloads(workBook,"DayWiseReport_${System.currentTimeMillis()}.xlsx")

//                var file = File(
//                    application.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "DayWiseReport.xlsx"
//                )
//
//                var fos = FileOutputStream(file)
//
//                workBook.write(fos)
//                fos.close()
//                workBook.close()
//
//                if(file.exists() && file.length()>0)
//                {
//                    _exportStatus.value = true
//                }
//                else
//                {
//                    _exportStatus.value = false
//                }
                Log.i("TIME DIFF","Time Diff: ${System.currentTimeMillis()-start} ms")
            }
            catch (e : Exception)
            {
                Log.e("EXPORT REPORT AS EXCEL","Export Report As Excel: ${e.message}")
            }
        }

    }

    fun fnExportReportToDownloads(workBook : XSSFWorkbook, fileName : String): Boolean
    {
        return try {
            var values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DOCUMENTS+"/ExpenseTracker"
                )
            }

            val uri: Uri =application.contentResolver.insert(
                MediaStore.Files.getContentUri("external"),
                values
            ) ?: return false

            application.contentResolver.openOutputStream(uri)?.use { os ->
                workBook.write(os)
            }

            workBook.close()

            true
        }
        catch (e : Exception){
            Log.e("FN_EXPORT_REPORT_TO_DOWNLOADS","Fn Export Report To Downloads: ${e.message}")
            false
        }

    }

    fun fnPreWarmExcelEngine() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val wb = XSSFWorkbook()
                wb.createSheet("warmup")
                wb.close()
            } catch (_: Exception) {}
        }
    }

}