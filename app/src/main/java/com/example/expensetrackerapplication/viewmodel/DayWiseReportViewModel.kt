package com.example.expensetrackerapplication.viewmodel

import android.app.Application
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
import com.example.expensetrackerapplication.model.CurrentDayReportModel
import com.example.expensetrackerapplication.`object`.Global
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class CurrentDayReportViewModel(application : Application) : AndroidViewModel(application = application)
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

    var _totalExpenseSummary = MutableLiveData<String>()
    var totalExpenseSummary : LiveData<String> = _totalExpenseSummary

    var _addedExpenseSummary = MutableLiveData<String>()
    var addedExpenseSummary : LiveData<String> = _addedExpenseSummary

    var _deletedExpenseSummary = MutableLiveData<String>()
    var deletedExpenseSummary : LiveData<String> = _deletedExpenseSummary

    var _expenseList = MutableLiveData<List<CurrentDayReportModel>>()
    var expenseList : LiveData<List<CurrentDayReportModel>> = _expenseList

    var _exportStatus = MutableLiveData<Boolean>()
    var exportStatus : LiveData<Boolean> = _exportStatus

    var _isExportLoading = MutableLiveData<Boolean>(false)
    var isExportLoading : LiveData<Boolean> = _isExportLoading

    var _expenseDeleteStatus = MutableLiveData<Boolean>()
    var expenseDeleteStatus : LiveData<Boolean> = _expenseDeleteStatus

    fun fnCloseDayWiseReport()
    {
        _closeDayWiseReport.value=true
    }

    fun fnClearFields(){
        _totalExpenseSummary.value=""
        _addedExpenseSummary.value=""
        _deletedExpenseSummary.value=""
        _expenseList.value = emptyList<CurrentDayReportModel>()
    }

    fun fnGetExpenseDetails(date: String?){
        Log.i("SELECTED DATE","Selected Date2: $date")
        viewModelScope.launch {
            try
            {
                var totalExpenseAmtSum = 0.0f
                var addedExpenseAmtSum = 0.0f
                var deletedExpenseAmtSum = 0.0f

                val res = expenseRepository.fnGetExpenseDetailsPerDate(date)
                var list : MutableList<CurrentDayReportModel> = mutableListOf()
                var exPaymentType = ""
                if(res.isNotEmpty())
                {
                    res.forEach { ex ->
                        list.add(CurrentDayReportModel(
                            expenseId = ex.expenseId,
                            categoryId= ex.expenseCategoryId,
                            catgeoryName= ex.expenseCategoryName,
                            expenseAmt = Global.fnFormatFloatTwoDigits(ex.expenseAmt.toFloat() ?:0.00f),
                            expenseRemarks = ex.expenseRemarks,
                            isDelete = if (ex.expenseStatus == Global.EXPENSE_STATUS_DELETED) "DELETED" else "NOT DELETED",
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
                        totalExpenseAmtSum = totalExpenseAmtSum + (ex.expenseAmt?.toFloat() ?:0.0f )
                        if(ex.expenseStatus == Global.EXPENSE_STATUS_DELETED) deletedExpenseAmtSum = deletedExpenseAmtSum + (ex.expenseAmt?.toFloat() ?:0.0f ) else addedExpenseAmtSum=addedExpenseAmtSum+ (ex.expenseAmt?.toFloat() ?:0.0f )

                    }
                    _expenseList.postValue(list)
                    _totalExpenseSummary.value= Global.fnFormatFloatTwoDigits(totalExpenseAmtSum) .toString()
                    _addedExpenseSummary.value =Global.fnFormatFloatTwoDigits(addedExpenseAmtSum) .toString()
                    _deletedExpenseSummary.value = Global. fnFormatFloatTwoDigits(deletedExpenseAmtSum) .toString()

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
                Log.i("EXPORT LOADING VALUE","Export Loading Value1: ${isExportLoading.value}")
                if(isExportLoading.value==false)
                {
                    _isExportLoading.value=true

                    delay(1000L)

                    Log.i("EXPORT LOADING VALUE","Export Loading Value2: ${isExportLoading.value}")

                    var start = Global.fnGetCurrentTime()

                    var workBook = XSSFWorkbook()
                    var sheet = workBook.createSheet("DAY WISE REPORT")

                    sheet.setColumnWidth(0,30*256)
                    sheet.setColumnWidth(1,20*256)
                    sheet.setColumnWidth(2,20*256)
                    sheet.setColumnWidth(3,50*256)
                    sheet.setColumnWidth(4,20*256)


                    val headerFont = Global.fnHeaderFont(workBook)
                    val summaryFont =  Global.fnSummaryFont(workBook)
                    //Header Style
                    val headerStyle = Global.fnHeaderStyle(workBook,headerFont)
                    //Summary Style
                    val summaryStyle = Global.fnSummaryStyle(workBook,summaryFont)
                    //Create Table Header Style
                    val tableHeaderStyle = Global.fnTableHeaderStyle(workBook)
                    //Create Table Date Style
                    val dataStyle = Global.fnTableDateStyle(workBook)

                    //Header Row
                    var headerRow = sheet.createRow(0)
                    var headerCell = headerRow.createCell(0)
                    headerCell.setCellValue("DAY WISE REPORT")
                    headerCell.cellStyle = headerStyle

                    sheet.addMergedRegion(
                        CellRangeAddress(
                            0,  // first row (0th row)
                            0,  // last row
                            0,  // first column
                            4   // last column
                        )
                    )

                    var dateRow = sheet.createRow(1)
                    var dateCell0 = dateRow.createCell(0)
                    dateCell0.setCellValue("DATE: ${selectedDate.value}")
                    dateCell0.cellStyle=summaryStyle

                    sheet.addMergedRegion(
                        CellRangeAddress(1,1,0,4)
                    )
                    var timeRow = sheet.createRow(2)
                    var timeCell0 = timeRow.createCell(0)
                    timeCell0.setCellValue("TIME: ${Global.fnGetCurrentTime()}")
                    timeCell0.cellStyle=summaryStyle


                    sheet.addMergedRegion(
                        CellRangeAddress(2,2,0,4)
                    )

                    var totalExpenseRow = sheet.createRow(3)
                    var totalExpenseCell0 = totalExpenseRow.createCell(0)
                    totalExpenseCell0.setCellValue("TOTAL EXPENSE: ${totalExpenseSummary.value}")
                    totalExpenseCell0.cellStyle=summaryStyle


                    sheet.addMergedRegion(
                        CellRangeAddress(3,3,0,4)
                    )

                    var addedExpenseRow = sheet.createRow(4)
                    var addedExpenseCell0 = addedExpenseRow.createCell(0)
                    addedExpenseCell0.setCellValue("ADDED EXPENSE: ${addedExpenseSummary.value}")
                    addedExpenseCell0.cellStyle=summaryStyle


                    sheet.addMergedRegion(
                        CellRangeAddress(4,4,0,4)
                    )

                    var deletedExpenseRow = sheet.createRow(5)
                    var deletedExpenseCell0 = deletedExpenseRow.createCell(0)
                    deletedExpenseCell0.setCellValue("DELETED EXPENSE: ${deletedExpenseSummary.value}")
                    deletedExpenseCell0.cellStyle=summaryStyle

                    sheet.addMergedRegion(
                        CellRangeAddress(5,5,0,4)
                    )

                    //Table Header Row
                    var tableHeaderRow = sheet.createRow(6)
                    var cell0 = tableHeaderRow.createCell(0)
                    cell0.setCellValue("CATEGORY")
                    cell0.cellStyle=tableHeaderStyle
                    var cell1 =tableHeaderRow.createCell(1)
                    cell1.setCellValue("EXPENSE AMOUNT")
                    cell1.cellStyle=tableHeaderStyle
                    var cell2=tableHeaderRow.createCell(2)
                    cell2.setCellValue("PAYMENT TYPE")
                    cell2.cellStyle = tableHeaderStyle
                    var cell3=tableHeaderRow.createCell(3)
                    cell3.setCellValue("REMARKS")
                    cell3.cellStyle=tableHeaderStyle
                    var cell4=tableHeaderRow.createCell(4)
                    cell4.setCellValue("STATUS")
                    cell4.cellStyle=tableHeaderStyle

                    //Table Data Row
                    expenseList.value?.forEachIndexed { index, expense ->
                        var dataRow = sheet.createRow(index+8)

                        var dataCell0=dataRow.createCell(0)
                        dataCell0.setCellValue(expense.catgeoryName)
                        dataCell0.cellStyle=dataStyle
                        var dataCell1=dataRow.createCell(1)
                        dataCell1.setCellValue(expense.expenseAmt)
                        dataCell1.cellStyle=dataStyle
                        var dataCell2=dataRow.createCell(2)
                        dataCell2.setCellValue(expense.paymentType)
                        dataCell2.cellStyle=dataStyle
                        var dataCell3=dataRow.createCell(3)
                        dataCell3.setCellValue(expense.expenseRemarks)
                        dataCell3.cellStyle=dataStyle
                        var dataCell4=dataRow.createCell(4)
                        dataCell4.setCellValue(expense.isDelete)
                        dataCell4.cellStyle=dataStyle

                    }

                    _exportStatus.value = fnExportReportToDownloads(workBook,"DayWiseReport_${Global.fnGetCurrentTime()}.xlsx")

                    Log.i("EXPORT LOADING VALUE","Export Loading Value3: ${isExportLoading.value}")

                    Log.i("TIME DIFF","Time Diff: ${Global.fnGetCurrentTime()} ms")

                }
            }
            catch (e : Exception)
            {
                _isExportLoading.value=false
                _exportStatus.value = false
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
//            delay(1000L)
            _isExportLoading.value=false
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