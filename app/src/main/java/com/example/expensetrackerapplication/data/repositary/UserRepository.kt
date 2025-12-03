package com.example.expensetrackerapplication.data.repositary

import android.util.Log
import com.example.expensetrackerapplication.data.dao.UserDao
import com.example.expensetrackerapplication.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserRepository(var userDao: UserDao)
{
    suspend fun fnInsertUserDetails(user: UserEntity) : Boolean
    {
        try {
            val insertStatus=userDao.fnInsertUser(user)
            if(insertStatus>0)
                return true
            else
                return false
        }
        catch (e: Exception)
        {
            Log.e("INSERT USER","Insert User In The Database: $e.message")
            return false
        }

    }

    suspend fun fnGetUserDetailsBasedOnUserName(name: String?, password : String?): List<UserEntity>
    {
        return try {
            userDao.fnGetUserBasedOnUserName(name,password)
        }
        catch (e: Exception)
        {
            Log.e(
                "GET USER DETAILS BASED ON USERNAME",
                "Error fetching user details: ${e.message}"
            )
            mutableListOf()
        }
    }
}