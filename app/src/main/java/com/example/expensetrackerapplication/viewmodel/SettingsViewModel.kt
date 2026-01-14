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

    fun  fnInsertCategories()
    {
        viewModelScope.launch {
            try{
//                var defaultCategories = listOf<String?>("Food","Travel","Health")
                var categories = mutableListOf<String?>("HouseRent","Grogeries","Savings","Entertainment","Education")

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
                for(i in categories)
                {
                    var categoryEntitty = CategoryEntitty(0,i,Global.lUserId)
                    var result = categoryRepository.fnInsertCategoriesToDb(
                        categoryEntitty
                    )

                    Log.v("INSERT STAUS","Insert Status: "+result)

                }
            }
            catch (e : Exception){
                Log.e("INSERT CATEGORIES FROM VIEW MODEL","Insert Categories From ViewModel: "+e.message)
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

}