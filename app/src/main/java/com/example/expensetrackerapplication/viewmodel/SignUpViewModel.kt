package com.example.expensetrackerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapplication.data.dao.UserDao
import com.example.expensetrackerapplication.data.database.AppDatabase
import com.example.expensetrackerapplication.data.entity.CategoryEntitty
import com.example.expensetrackerapplication.data.entity.UserEntity
import com.example.expensetrackerapplication.data.repositary.CategoryRepository
import com.example.expensetrackerapplication.data.repositary.UserRepository
import com.example.expensetrackerapplication.`object`.Global
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : AndroidViewModel(application) {

    var userRepository: UserRepository
    var categoryRepository : CategoryRepository

    init {
        val userDao= AppDatabase.getdatabase(application).userDao()
        userRepository= UserRepository(userDao)

        val categoryDao = AppDatabase.getdatabase(application).CategoryDao()
        categoryRepository= CategoryRepository(categoryDao)
    }

    //User Name
    var _name = MutableLiveData<String?>()
    var  name : LiveData<String?> = _name

    //User Email
    var _email = MutableLiveData<String?>()
    var email : LiveData<String?> = _email

    //User MobileNo
    var _mobileNo = MutableLiveData<String?>()
    var mobileNo : LiveData<String?> = _mobileNo

    //User Password
    var _password = MutableLiveData<String?>()
    var password : LiveData<String?> = _password

    //GoToSignUp
    var _actionGoToLogin = MutableLiveData<Boolean>()
    var actionGoToLogin : LiveData<Boolean> = _actionGoToLogin

    var _insertStatus = MutableLiveData<Boolean>()
    var insertStatus : LiveData<Boolean> = _insertStatus

    var _nameErrorStatus = MutableLiveData<Boolean>()
    var nameErrorStatus : LiveData<Boolean> = _nameErrorStatus

    var _mobileNoErrorStatus = MutableLiveData<Boolean>()
    var mobileNoErrorStatus : LiveData<Boolean> = _mobileNoErrorStatus

    var _emailErrorStatus = MutableLiveData<Boolean>()
    var emailErrorStatus : LiveData<Boolean> = _emailErrorStatus

    var _passwordErrorStatus = MutableLiveData<Boolean>()
    var passwordErrorStatus : LiveData<Boolean> = _passwordErrorStatus


    var _bothFieldsErrorStatus = MutableLiveData<Boolean>()
    var bothFieldsErrorStatus : LiveData<Boolean> = _bothFieldsErrorStatus

    var _clearAllFields = MutableLiveData<Boolean>()
    var clearAllFields : LiveData<Boolean> = _clearAllFields
    fun fnClearInputs()
    {
        _clearAllFields.value=true
        _name.value="";
        _mobileNo.value="";
        _password.value="";
        _email.value="";
    }

    fun fnGoToLogin()
    {
        _actionGoToLogin.value = true
    }

    fun fnStoreUserDetails()
    {
        when {
            name.value.isNullOrBlank() &&
                    mobileNo.value.isNullOrBlank() &&
                        email.value.isNullOrBlank() &&
                    password.value.isNullOrBlank() -> {
                    _bothFieldsErrorStatus.value=true
            }

            name.value.isNullOrBlank() -> {
                _nameErrorStatus.value=true
            }

            mobileNo.value.isNullOrBlank() -> {
                _mobileNoErrorStatus.value=true
            }

            email.value.isNullOrBlank() -> {
                _emailErrorStatus.value=true
            }

            password.value.isNullOrBlank() -> {
                _passwordErrorStatus.value=true
            }

            else -> {
                fnInsert()
            }
        }
    }

    fun fnInsert()
    {
        viewModelScope.launch {
            if(!name.value.isNullOrBlank() &&
                !mobileNo.value.isNullOrBlank() &&
                !email.value.isNullOrBlank() &&
                !password.value.isNullOrBlank())
            {
                val user= UserEntity(
                    userName = name.value, userMobileNo = mobileNo.value,
                    userEmail = email.value, userPassword = password.value, userProfilePhotoUri = null,userId = 0
                )

                val userId = userRepository.fnInsertUserDetails(user)
                if(userId > 0)
                {
                    var categoryEntities=Global.defaultCategories.map{
                        CategoryEntitty(
                            categoryId = 0,
                            categoryName = it,
                            userId = userId.toInt()
                        )
                    }

                    var categoryInsertStatus = categoryRepository.fnInsertDefaultCategoriesToDb(categoryEntities)

                    if(categoryInsertStatus.isNotEmpty() && categoryInsertStatus.all {it > 0})
                    {
                        _insertStatus.postValue(true)
                    }
                    else{
                        _insertStatus.value=false
                    }
                }
                else
                {
                    _insertStatus.value=false
                }
            }
        }
    }

}