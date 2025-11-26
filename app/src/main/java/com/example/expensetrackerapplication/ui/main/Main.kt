package com.example.expensetrackerapplication.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.MainBinding
import com.example.expensetrackerapplication.viewmodel.MainViewModel

class Main : AppCompatActivity() {
    lateinit var mainDataBinding: MainBinding
    val mainViewModel : MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainDataBinding= MainBinding.inflate(layoutInflater)
        mainDataBinding.mainViewModel=mainViewModel
        mainDataBinding.lifecycleOwner=this
        setContentView(mainDataBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}