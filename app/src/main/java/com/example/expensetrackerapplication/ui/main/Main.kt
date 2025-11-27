package com.example.expensetrackerapplication.ui.main

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
            findNavController(R.id.idContainer).navigate(R.id.reports
            )
        }

        mainDataBinding.idDashboardFab.setOnClickListener {
            fnShrinkFab()
            findNavController(R.id.idContainer).navigate(R.id.dashBoard)
        }

        mainDataBinding.idAddExpenseFab.setOnClickListener {
            fnShrinkFab()
            findNavController(R.id.idContainer).navigate(R.id.newExpense)
        }

        mainDataBinding.idSettingsFab.setOnClickListener {
            fnShrinkFab()
            findNavController(R.id.idContainer).navigate(R.id.settings)
        }

        mainDataBinding.idProfileFab.setOnClickListener {
            fnShrinkFab()
            findNavController(R.id.idContainer).navigate(R.id.profile)
        }
    }

    fun fnExpandFab() {
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
    }
}