package com.example.expensetrackerapplication.data.repositary

import android.util.Log
import com.example.expensetrackerapplication.data.dao.UserDao
import com.example.expensetrackerapplication.data.entity.UserEntity

class UserRepository(var userDao: UserDao)
{
    suspend fun fnInsertUserDetails(user: UserEntity) : Long
    {
        try {
            return userDao.fnInsertUser(user)
//            if(insertStatus>0)
//                return true
//            else
//                return false
        }
        catch (e: Exception)
        {
            Log.e("INSERT USER","Insert User In The Database: $e.message")
            return 0
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

    suspend fun fnDeleteUser(userId : Int) : Boolean{
        return try {
            var result = userDao.fnDeleteUserAccountFromDb(userId)
            if(result > 0) true else false
        }
        catch (e: Exception)
        {
            Log.e(
                "DELETE USER ACCOUNT",
                "Delete User Account: ${e.message}"
            )
            false
        }
    }

    suspend fun fnUpdateLoginUserPassword(currentPassword: String?, userId: Int, newPassword: String?):Boolean{
        return try {
            var result = userDao.fnUpdateUserPassword(newPassword = newPassword,userId, currentPassword = currentPassword)
            if(result>0) true else false
        } catch (e : Exception){
            Log.e("UPDATE USER PASSWORD","Update User Password: ${e.message}")
            false
        }
    }

    suspend fun fnResetLoginUserPassword(newPassword: String?, email: String?):Boolean {
        return try {
            var result = userDao.fnResetUserPassword(newPassword = newPassword,email)
            if(result>0) true else false
        } catch (e : Exception){
            Log.e("UPDATE USER PASSWORD","Update User Password: ${e.message}")
            false
        }
    }

    suspend fun fnUpdateLoginUserProfilePhoto(userImgUri: String?, userId: Int):Boolean{
        return try {
            var result = userDao.fnUpdateUserProfilePhoto(userImgUri,userId)
            if(result>0) true else false
        } catch (e : Exception){
            Log.e("UPDATE USER PASSWORD","Update User Password: ${e.message}")
            false
        }
    }

    suspend fun fnGetLoginUserProfilePhotoUri(userId: Int):String?{
        return try {
             userDao.fnGetUserProfilePhotoUri(userId)
        }
        catch (e : Exception)
        {
            Log.e("GET USER PROFILE PHOTO","Get User Profile Photo: ${e.message}")
            null
        }
    }
}