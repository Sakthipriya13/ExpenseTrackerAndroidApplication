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

    var _newCategory = MutableLiveData<String?>()
    val newCategory : LiveData<String?> = _newCategory

    var _defCat1 = MutableLiveData<String?>()
    var defCat1 : LiveData<String?> = _defCat1


    var _defCat2 = MutableLiveData<String?>()
    var defCat2 : LiveData<String?> = _defCat2


    var _defCat3 = MutableLiveData<String?>()
    var defCat3 : LiveData<String?> = _defCat3


    var _defCat4 = MutableLiveData<String?>()
    var defCat4 : LiveData<String?> = _defCat4


    var _defCat5 = MutableLiveData<String?>()
    var defCat5 : LiveData<String?> = _defCat5

    var _lan_eng= MutableLiveData<Boolean>(true)
    val lan_eng : LiveData<Boolean> = _lan_eng


    var _lan_tam= MutableLiveData<Boolean>()
    val lan_tam : LiveData<Boolean> = _lan_tam

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
                    var categoryEntitty = CategoryEntitty(0,newCategory.value,Global.lUserId)
                    var result = categoryRepository.fnInsertCategoriesToDb(
                        categoryEntitty
                    )

                    Log.v("INSERT STAUS","Insert Status: "+result)

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
                    _defCat1.value=category_List.get(0).toString()
                    _defCat2.value=category_List.get(1).toString()
                    _defCat3.value=category_List.get(2).toString()
                    _defCat4.value=category_List.get(3).toString()
                    _defCat5.value=category_List.get(4).toString()
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

}