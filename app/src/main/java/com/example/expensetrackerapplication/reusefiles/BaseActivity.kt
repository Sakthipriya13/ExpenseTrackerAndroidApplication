package com.example.expensetrackerapplication.reusefiles

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.expensetrackerapplication.datastore.LanguageDataStore
import com.example.expensetrackerapplication.`object`.LocaleHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

abstract class LanguageBaseActivity : AppCompatActivity()
{
    override fun attachBaseContext(newBase: Context?) {
        val languageCode = runBlocking {
//            LanguageDataStore(newBase).languageFlow?.first()
            LanguageDataStore(newBase).fnGetLanguage()
        }
        val context = LocaleHelper.fnSetLocale(newBase,languageCode)
        super.attachBaseContext(context)
    }
}