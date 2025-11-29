package com.example.expensetrackerapplication.ui.main.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.NewExpenseBinding
import com.example.expensetrackerapplication.viewmodel.NewExpenseViewModel
import com.example.expensetrackerapplication.viewmodel.SettingsViewModel
import com.google.type.Date
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewExpense.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewExpense : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var newExpenseBinding : NewExpenseBinding

    val newExpenseViewModel : NewExpenseViewModel by viewModels()

    val settingsViewModel : SettingsViewModel by activityViewModels()



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

        newExpenseBinding = DataBindingUtil.inflate(inflater,R.layout.new_expense, container, false)
        newExpenseBinding.newExpenseViewModel=newExpenseViewModel
        newExpenseBinding.lifecycleOwner=viewLifecycleOwner

        newExpenseViewModel._selectedDate.value=fnGetCurrentDate()
        lifecycleScope.launchWhenStarted {
//            settingsViewModel.fnInsertCategories()
            settingsViewModel.fnGetAllCategories()
        }




        newExpenseBinding.idCalendarButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),
                { _,y,m,d ->
                    var date="$d-${m+1}-$y"
                    newExpenseViewModel._selectedDate.value=date

                } , year,month,day)
            datePickerDialog.show()
        }

        newExpenseBinding.idAdd.setOnClickListener {
            Log.v("Expense AMt","Expense Amt: "+newExpenseViewModel.expenseAmt.value)
        }

        settingsViewModel.categoryList.observe(viewLifecycleOwner){ list ->
            val categoryNameList = list.map {it.categoryName}
//            val categoryNameList = mutableListOf<String?>("Food","Vegtables","Transportation","Education")
            val autoCompleteAdapter= ArrayAdapter(
                requireContext(),
                R.layout.text_view_for_list,
                categoryNameList)

            newExpenseBinding.idDCategories.setAdapter(autoCompleteAdapter)

        }

        newExpenseBinding.idDCategories.setOnClickListener {
            Log.v("CATEGORY LIST","Category List: "+settingsViewModel.categoryList.value)
            newExpenseBinding.idDCategories.showDropDown()
        }







        return newExpenseBinding.root
    }

    private fun fnGetCurrentDate() : String {

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(java.util.Date())

        return currentDate
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewExpense.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewExpense().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}