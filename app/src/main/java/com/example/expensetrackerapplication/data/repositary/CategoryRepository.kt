package com.example.expensetrackerapplication.data.repositary

import android.util.Log
import com.example.expensetrackerapplication.data.dao.CategoryDao
import com.example.expensetrackerapplication.data.entity.CategoryEntitty
import com.example.expensetrackerapplication.`object`.Global

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
            var categoryList = categoryDao.fnGetAllCategories(Global.lUserId)
            return categoryList
        }
        catch (e: Exception)
        {
            Log.e("GET CATEGORIES","Get Categories: "+e.message)
            return mutableListOf()
        }
    }


    suspend fun fnGetDefaultCategoriesFromDb() : List<CategoryEntitty>{
        return try {
            var categoryList = categoryDao.fnGetDefaultCategories(Global.lUserId)
            categoryList
        }
        catch (e: Exception)
        {
            Log.e("GET CATEGORIES","Get Categories: "+e.message)
            mutableListOf()
        }
    }


    suspend fun fnDeleteCategory(categoryId : Int, userId : Int):Boolean{
        return try{
            var res= categoryDao.fnDeleteCategoryFromDb(categoryId = categoryId,userId)

            if(res > 0)
                true
            else
                false
        }
        catch (e : Exception){
            Log.e("GET CATEGORIES","Get Categories: "+e.message)
            false
        }
    }
}