package com.example.expensetrackerapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.MainBinding
import com.example.expensetrackerapplication.viewmodel.MainViewModel

import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.ui.auth.Auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlinx.coroutines.withContext

class Main : AppCompatActivity() {
    lateinit var mainDataBinding: MainBinding
    val mainViewModel : MainViewModel by viewModels()

    var isExpanded = false


    val fromBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(this,R.anim.from_bottom_fab)
    }

    val toBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(this,R.anim.to_bottom_fab)
    }

    val rotateClockWiseFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(this,R.anim.rotate_clock_wise)
    }

    val rotateAntiClockWiseFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(this,R.anim.rotate_anticlock_wise)
    }


    val fromBottomAnim : Animation by lazy {
        AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)
    }

    val toBottomAnim : Animation by lazy {
        AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainDataBinding= MainBinding.inflate(layoutInflater)
        mainDataBinding.mainViewModel=mainViewModel
        mainDataBinding.lifecycleOwner=this
        setContentView(mainDataBinding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        setSupportActionBar(mainDataBinding.idToolBar)


        fnShrinkFab()

        mainViewModel.logoutStatus.observe(this) { isLoggedOut ->
            if(isLoggedOut){
                Global.lUserId =-1
                Global.lUserName=""
                Global.lUserPassword=""
                Global.lUserMobileNo=""
                Global.lUssrEmail=""
                val intent = Intent(this, Auth::class.java)
                startActivity(intent)
                finish()
            }
        }

        mainDataBinding.idMenuFab.setOnClickListener {
            if(isExpanded){
                fnExpandFab()
            }
            else{
                fnShrinkFab()
            }
        }
        mainDataBinding.idReportFab.setOnClickListener {
            fnShrinkFab()
            findNavController(R.id.idContainer).navigate(R.id.idParentReport)
        }

        mainDataBinding.idDashboardFab.setOnClickListener {
            fnShrinkFab()
            findNavController(R.id.idContainer).navigate(R.id.idDashBoard)
        }

        mainDataBinding.idAddExpenseFab.setOnClickListener {
            lifecycleScope.launch {
                fnShrinkFab()
                delay(200)
                findNavController(R.id.idContainer).navigate(R.id.idNewExpense)

            }

        }

        mainDataBinding.idSettingsFab.setOnClickListener {
            fnShrinkFab()
            findNavController(R.id.idContainer).navigate(R.id.idSettings)
        }

        mainDataBinding.idProfileFab.setOnClickListener {
            fnShrinkFab()
            findNavController(R.id.idContainer).navigate(R.id.idProfile)
        }

        mainViewModel.displayTransparentBg.observe(this){ isDisplay ->
            if(isDisplay)
                mainDataBinding.idTransparentBg.visibility=View.VISIBLE
            else
                mainDataBinding.idTransparentBg.visibility=View.GONE
        }
    }

    fun fnExpandFab() {

        mainDataBinding.idDashboardFab.visibility=View.INVISIBLE
        mainDataBinding.idReportFab.visibility=View.INVISIBLE
        mainDataBinding.idAddExpenseFab.visibility=View.INVISIBLE
        mainDataBinding.idSettingsFab.visibility=View.INVISIBLE
        mainDataBinding.idProfileFab.visibility=View.INVISIBLE

        mainDataBinding.idHomeText.visibility=View.INVISIBLE
        mainDataBinding.idReportText.visibility=View.INVISIBLE
        mainDataBinding.idAddText.visibility=View.INVISIBLE
        mainDataBinding.idSettingsText.visibility=View.INVISIBLE
        mainDataBinding.idProfileText.visibility=View.INVISIBLE

        mainDataBinding.idTransparentBg.visibility=View.INVISIBLE

        mainDataBinding.idTransparentBg.startAnimation(fromBottomAnim)

        mainDataBinding.idMenuFab.startAnimation(rotateClockWiseFabAnim)

        mainDataBinding.idMenuFab.setImageResource(R.drawable.add)


        mainDataBinding.idDashboardFab.startAnimation(fromBottomFabAnim)
        mainDataBinding.idReportFab.startAnimation(fromBottomFabAnim)
        mainDataBinding.idAddExpenseFab.startAnimation(fromBottomFabAnim)
        mainDataBinding.idSettingsFab.startAnimation(fromBottomFabAnim)
        mainDataBinding.idProfileFab.startAnimation(fromBottomFabAnim)

        mainDataBinding.idHomeText.startAnimation(fromBottomFabAnim)
        mainDataBinding.idReportText.startAnimation(fromBottomFabAnim)
        mainDataBinding.idAddText.startAnimation(fromBottomFabAnim)
        mainDataBinding.idSettingsText.startAnimation(fromBottomFabAnim)
        mainDataBinding.idProfileText.startAnimation(fromBottomFabAnim)

        isExpanded = !isExpanded
    }

    fun fnShrinkFab()
    {

        mainDataBinding.idTransparentBg.startAnimation(toBottomAnim)
        mainDataBinding.idMenuFab.startAnimation(rotateAntiClockWiseFabAnim)

        mainDataBinding.idMenuFab.setImageResource(R.drawable.menu1)

        mainDataBinding.idDashboardFab.startAnimation(toBottomFabAnim)
        mainDataBinding.idAddExpenseFab.startAnimation(toBottomFabAnim)
        mainDataBinding.idReportFab.startAnimation(toBottomFabAnim)
        mainDataBinding.idSettingsFab.startAnimation(toBottomFabAnim)
        mainDataBinding.idProfileFab.startAnimation(toBottomFabAnim)

        mainDataBinding.idHomeText.startAnimation(toBottomFabAnim)
        mainDataBinding.idAddText.startAnimation(toBottomFabAnim)
        mainDataBinding.idReportText.startAnimation(toBottomFabAnim)
        mainDataBinding.idSettingsText.startAnimation(toBottomFabAnim)
        mainDataBinding.idProfileText.startAnimation(toBottomFabAnim)

        isExpanded = !isExpanded


        mainDataBinding.idDashboardFab.visibility=View.GONE
        mainDataBinding.idReportFab.visibility=View.GONE
        mainDataBinding.idAddExpenseFab.visibility=View.GONE
        mainDataBinding.idSettingsFab.visibility=View.GONE
        mainDataBinding.idProfileFab.visibility=View.GONE

        mainDataBinding.idHomeText.visibility=View.GONE
        mainDataBinding.idReportText.visibility=View.GONE
        mainDataBinding.idAddText.visibility=View.GONE
        mainDataBinding.idSettingsText.visibility=View.GONE
        mainDataBinding.idProfileText.visibility=View.GONE

        mainDataBinding.idTransparentBg.visibility=View.GONE


    }
}


