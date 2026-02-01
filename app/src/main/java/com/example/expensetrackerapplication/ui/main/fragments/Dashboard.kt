package com.example.expensetrackerapplication.ui.main.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.DashboardBinding
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.viewmodel.DashBoardViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Dashboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class Dashboard : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var dashBoardBinding : DashboardBinding

    val dashBoardViewModel : DashBoardViewModel by viewModels()

    val months = listOf("Jan","Feb","March")
    val earnings = listOf(500,400,300)

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title= "Dashboard"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dashBoardBinding = DataBindingUtil.inflate(inflater,R.layout.dashboard, container, false)
        dashBoardBinding.dashBoard=dashBoardViewModel
        dashBoardBinding.lifecycleOwner=viewLifecycleOwner

        return dashBoardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashBoardViewModel._isLoading.value = true

        fnAddCalendarOptions()

        dashBoardViewModel.onCLickBtnThisMonth()

        fnDisplayChart()

        dashBoardViewModel.fnGetCateDetailsPerDay()


        dashBoardViewModel._clickBtnThisMonth.observe(viewLifecycleOwner){ isClick ->
            if(isClick){
                fnUpdateBtnUi(dashBoardBinding.idBtnThisMonth,true)
            }
            else {
                fnUpdateBtnUi(dashBoardBinding.idBtnThisMonth,false)
            }
        }

        dashBoardViewModel._clickBtnThisYear.observe(viewLifecycleOwner){ isClick ->
            if(isClick){
                fnUpdateBtnUi(dashBoardBinding.idBtnThisYear,true)
            }
            else {
                fnUpdateBtnUi(dashBoardBinding.idBtnThisYear,false)
            }
        }

        dashBoardBinding.idDOptions.setOnItemClickListener { parent,_,position,_ ->
            when(position)
            {
                Global.DAY_WISE -> {
                    dashBoardViewModel._isLoading.value = true
                    dashBoardViewModel.fnGetCateDetailsPerDay()
                }
                Global.MONTHLY_WISE -> {
                    dashBoardViewModel._isLoading.value = true
                    dashBoardViewModel.fnGetCateDetailsPerMonth()
                }
                Global.YEARLY_WISE-> {
                    dashBoardViewModel._isLoading.value = true
                    dashBoardViewModel.fnGetCateDetailsPerYear()
                }
            }
        }

        dashBoardViewModel.categoryChartList.observe(viewLifecycleOwner) { list ->
            dashBoardViewModel._isLoading.value = false
            var pie : Pie = AnyChart.pie()
            val chartList : MutableList<DataEntry> = mutableListOf()
//            for(i in months.indices){
//                list.add(ValueDataEntry(
//                    months.elementAt(i),earnings.elementAt(i)
//                ))
//            }
            list.forEach { ob ->
                chartList.add(
                    ValueDataEntry(
                        ob.categoryName,
                        ob.expenseAmt
                    )
                )
                Log.i("CATEGORY LIST","Category List: ${ob.categoryName}")
            }

            pie.data(chartList)
            pie.title(resources.getString(R.string.category_overview))
            dashBoardBinding.idCategoryPieChart.setChart(pie)
        }

//        dashBoardViewModel.dayWiseExpenseChartList.observe(viewLifecycleOwner){ list ->
//            // Create a Cartesian chart instance
//            val cartesian: Cartesian = AnyChart.column()
//
//            // Prepare data: category name + value
//            val data: MutableList<DataEntry?> = ArrayList<DataEntry?>()
//            data.add(ValueDataEntry("Apples", 55))
//            data.add(ValueDataEntry("Bananas", 78))
//            data.add(ValueDataEntry("Oranges", 33))
//            data.add(ValueDataEntry("Grapes", 90))
//            data.add(ValueDataEntry("Pears", 41))
//            data.add(ValueDataEntry("Apples1", 55))
//            data.add(ValueDataEntry("Bananas1", 78))
//            data.add(ValueDataEntry("Oranges1", 33))
//            data.add(ValueDataEntry("Grapes1", 90))
//            data.add(ValueDataEntry("Pears1", 41))
//            data.add(ValueDataEntry("Apples2", 55))
//            data.add(ValueDataEntry("Bananas2", 78))
//            data.add(ValueDataEntry("Oranges2", 33))
//            data.add(ValueDataEntry("Grapes2", 90))
//            data.add(ValueDataEntry("Pears2", 41))
//
//            data.add(ValueDataEntry("Apples3", 55))
//            data.add(ValueDataEntry("Bananas3", 78))
//            data.add(ValueDataEntry("Oranges3", 33))
//            data.add(ValueDataEntry("Grapes3", 90))
//            data.add(ValueDataEntry("Pears3", 41))
//            data.add(ValueDataEntry("Apples4", 55))
//            data.add(ValueDataEntry("Bananas4", 78))
//            data.add(ValueDataEntry("Oranges4", 33))
//            data.add(ValueDataEntry("Grapes4", 90))
//            data.add(ValueDataEntry("Pears4", 41))
//            data.add(ValueDataEntry("Apples6", 55))
//            data.add(ValueDataEntry("Bananas6", 78))
//            data.add(ValueDataEntry("Oranges6", 33))
//            data.add(ValueDataEntry("Grapes6", 90))
//            data.add(ValueDataEntry("Pears6", 41))
//
//            data.add(ValueDataEntry("Apples7", 55))
//
//            // Add data to a Column series
//            val column: Column? = cartesian.column(data)
//
//            // Optional: set chart title and labels
//            cartesian.title("Fruit Sales Example")
//            cartesian.xAxis(0).title("Fruit")
//            cartesian.yAxis(0).title("Quantity")
//
//            // Set chart in AnyChartView
//            dashBoardBinding.idCategoryPieChart1.setChart(cartesian)
//        }

        dashBoardViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            if(isLoading){
                dashBoardBinding.idProgressBar.visibility = View.VISIBLE
            }
            else{
                dashBoardBinding.idProgressBar.visibility = View.GONE
            }
        }
    }

    fun fnDisplayChart(){

        Log.i("DISPLAY COLUMN CHART1","Display Column Chart1")

        // Create a Cartesian chart instance
        val cartesian = AnyChart.column()

        // Prepare data: category name + value
        val data: MutableList<DataEntry?> = ArrayList<DataEntry?>()
        data.add(ValueDataEntry("Apples", 55))
        data.add(ValueDataEntry("Bananas", 78))
        data.add(ValueDataEntry("Oranges", 33))
        data.add(ValueDataEntry("Grapes", 90))
        data.add(ValueDataEntry("Pears", 41))

        // Add data to a Column series
        val column = cartesian.column(data)

        // Optional: set chart title and labels
        cartesian.animation(true)
        cartesian.title("Fruit Sales Example")
        cartesian.xAxis(0).title("Fruit")
        cartesian.yAxis(0).title("Quantity")

        // Set chart in AnyChartView
        dashBoardBinding.idChart.setChart(cartesian)

        Log.i("DISPLAY COLUMN CHART2","Display Column Chart2")

        var pie : Pie = AnyChart.pie()
        val chartList : MutableList<DataEntry> = mutableListOf()
        for(i in months.indices){
            chartList.add(ValueDataEntry(
                months.elementAt(i),earnings.elementAt(i)
            ))
        }
        pie.data(chartList)
        pie.title(resources.getString(R.string.category_overview))
        dashBoardBinding.idChart1.setChart(pie)

        Log.i("DISPLAY COLUMN CHART2","Display Column Chart3")

    }

    fun fnUpdateBtnUi(btnId : MaterialButton,isClick : Boolean){
        dashBoardViewModel._isLoading.value = false
        if(isClick){
            btnId.setBackgroundColor(
                MaterialColors.getColor(
                    requireView(),
                    com.google.android.material.R.attr.colorOnPrimary
                )
            )
            btnId.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.text_color_white
                )
            )
        }
        else{
            btnId.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.text_color_white
                )
            )
            btnId.setTextColor(
                MaterialColors.getColor(
                    requireView(),
                    com.google.android.material.R.attr.colorOnPrimary
                )
            )
            btnId.strokeColor= ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_grey
                )
            )
            btnId.strokeWidth=2
        }
    }


    fun fnAddCalendarOptions(){
        val options = resources.getStringArray(R.array.calendar_options)
        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.dropdown_item,
            options
        )
        dashBoardBinding.idDOptions.setAdapter(adapter)
        dashBoardBinding.idDOptions.setText(adapter.getItem(0),false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Dashboard.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Dashboard().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}