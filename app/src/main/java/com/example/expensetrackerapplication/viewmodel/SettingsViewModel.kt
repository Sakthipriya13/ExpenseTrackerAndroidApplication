package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.entity.CategoryEntitty
import com.example.expensetrackerapplication.data.repositary.CategoryRepository
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.ui_event.ResultState
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application)
{
    var categoryRepository : CategoryRepository
    init {
        var dao = AppDatabase.getdatabase(application).CategoryDao()
        categoryRepository= CategoryRepository(dao)
    }
    var _categoryList = MutableLiveData<List<CategoryEntitty>>()         //mutableListOf<CategoryEntitty>()

    val categoryList: LiveData<List<CategoryEntitty>> get() = _categoryList

    var _newCategory = MutableLiveData<String?>()
    val newCategory : LiveData<String?> = _newCategory

    var _insertStatus = MutableLiveData<ResultState>()
    var insertStaus : LiveData<ResultState> = _insertStatus

    var _delStatus = MutableLiveData<ResultState>()
    var delStatus : LiveData<ResultState> = _delStatus

    var _isLoading= MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun fnClearNewCategory(){
        _newCategory.value = ""
    }

    fun  fnInsertCategories()
    {
        viewModelScope.launch {
            try{
//                var defaultCategories = listOf<String?>("Food","Travel","Health")
//                var categories = mutableListOf<String?>("HouseRent","Grogeries","Savings","Entertainment","Education")

//                for(i in defaultCategories)
//                {
//                    var categoryEntitty = CategoryEntitty(0,i,Global.lUserId)
//                    var result = categoryRepository.fnInsertCategoriesToDb(
//                        categoryEntitty
//                    )
//
//                    Log.v("INSERT STAUS","Insert Status: "+result)
//
//                }
//                for(i in categories)
//                {
                Log.v("CATEGORY NAME","Category Name: $newCategory.value")

                var categoryEntitty = CategoryEntitty(0,newCategory.value,Global.lUserId)
                    var result = categoryRepository.fnInsertCategoriesToDb(
                        categoryEntitty
                    )

                    if(result){
                        fnGetAllCategories()
                        fnClearNewCategory()
                        _insertStatus.postValue(ResultState.success("Successfully Category Inserted"))
                    }
                    else{
                        _insertStatus.postValue(ResultState.success("Category Insert Failed"))
                    }

                    Log.v("INSERT STAUS","Insert Status: $result")

//                }
            }
            catch (e : Exception){
                Log.e("INSERT CATEGORIES FROM VIEW MODEL","Insert Categories From ViewModel: "+e.message)
            }

        }

    }

    fun fnGetDefaultCategories(){
        viewModelScope.launch {
            try {
                var category_List=categoryRepository.fnGetDefaultCategoriesFromDb()

                if(category_List.isNotEmpty()){
                }
                Log.v("CATEGORY LIST","Category List: $category_List")
            }
            catch (e : Exception)
            {
                Log.e("GET DEFAULT CATEGORIES FROM VIEW MODEL","Get Default Categories: $e.message")
            }
        }
    }

    fun fnGetAllCategories(){
        viewModelScope.launch {
            try {
                var category_List=categoryRepository.fnGetAllCategoriesFromDb()
                _categoryList.value=category_List

                Log.v("CATEGORY LIST","Category List: "+categoryList.value)
            }
            catch (e : Exception)
            {
                Log.e("GET CATEGORIES FROM VIEW MODEL","Get Categories Froms" +
                        "s" +
                        "s ViewModel: "+e.message)
            }
        }
    }

    fun fnDeleteCategory(categoryId : Int, userId : Int)
    {
        viewModelScope.launch {
            try {
                var status = categoryRepository.fnDeleteCategory(categoryId,userId)
                if(status) {
                    fnGetAllCategories()
                    _delStatus.postValue(ResultState.success("Successfully Category Deleted"))
                }else
                    _delStatus.postValue(ResultState.success("Category Delete Failed"))
            }
            catch (e: Exception){

            }
        }
    }

}