package com.example.expensetrackerapplication.ui.main.fragments.reports

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.MonthlyReportBinding
import com.example.expensetrackerapplication.viewmodel.MonthlySummaryViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MonthlyReport.newInstance] factory method to
 * create an instance of this fragment.
 */
class MonthlyReport : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var monthlySummaryBinding : MonthlyReportBinding

    private val monthlySummaryViewModel : MonthlySummaryViewModel by viewModels()

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
        monthlySummaryBinding = DataBindingUtil.inflate(inflater,R.layout.monthly_report, container, false)
        monthlySummaryBinding.monthlySummaryViewModel = monthlySummaryViewModel
        monthlySummaryBinding.lifecycleOwner = viewLifecycleOwner

        // Inflate the layout for this fragment
        return monthlySummaryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monthlySummaryBinding.idBtnCalendar.setOnClickListener{
            showMonthPicker()
        }
    }


    private fun showMonthPicker() {

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Month")
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()

        picker.show(parentFragmentManager, "MONTH_PICKER")

        picker.addOnPositiveButtonClickListener { selection ->

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection

            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)

            println("Selected Month: $month Year: $year")

            // Example UI update
            // binding.txtMonth.text = "$month-$year"
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MonthlyReport.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MonthlyReport().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}