package com.example.expensetrackerapplication.data.repositary

import android.util.Log
import com.example.expensetrackerapplication.data.dao.CategoryDao
import com.example.expensetrackerapplication.data.entity.CategoryEntitty

class CategoryRepository(val categoryDao: CategoryDao)
{

    suspend fun fnInsertCategoriesToDb(categoryEntitty: CategoryEntitty): Boolean
    {
        try{
            val insertStatus=categoryDao.fnInsertCategories(categoryEntitty)
            if(insertStatus>0)
            {
                return true
            }
            else
            {
                return false
            }
        }
        catch (e: Exception)
        {
            Log.e("INSERT CATEGORIES","Insert Categories: "+e.message)
            return false
        }

    }

    suspend fun fnInsertDefaultCategoriesToDb(categoryEntity: List<CategoryEntitty>): List<Long>
    {
        try{
            return categoryDao.fnInsertDefaultCategories(categoryEntity)

        }
        catch (e: Exception)
        {
            Log.e("INSERT CATEGORIES","Insert Categories: "+e.message)
            return listOf()
        }

    }

    suspend fun fnGetAllCategoriesFromDb() : List<CategoryEntitty>{
        try {
            var categoryList = categoryDao.fnGetAllCategories()
            return categoryList
        }
        catch (e: Exception)
        {
            Log.e("GET CATEGORIES","Get Categories: "+e.message)
            return mutableListOf()
        }
    }


    suspend fun fnGetDefaultCategoriesFromDb() : List<CategoryEntitty>{
        try {
            var categoryList = categoryDao.fnGetDefaultCategories()
            return categoryList
        }
        catch (e: Exception)
        {
            Log.e("GET CATEGORIES","Get Categories: "+e.message)
            return mutableListOf()
        }
    }
}