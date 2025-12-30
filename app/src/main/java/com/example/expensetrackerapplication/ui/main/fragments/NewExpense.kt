package com.example.expensetrackerapplication.ui.main.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.data.entity.CategoryEntitty
import com.example.expensetrackerapplication.databinding.NewExpenseBinding
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.reusefiles.fnShowMessage
import com.example.expensetrackerapplication.viewmodel.NewExpenseViewModel
import com.example.expensetrackerapplication.viewmodel.SettingsViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

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
    var categoryList =emptyList<CategoryEntitty>()

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title= "New Expense"
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

        newExpenseBinding = DataBindingUtil.inflate(inflater,R.layout.new_expense, container, false)
        newExpenseBinding.newExpenseViewModel=newExpenseViewModel
        newExpenseBinding.lifecycleOwner=viewLifecycleOwner

        // Apply default only on first load
//        newExpenseViewModel.applyDefaultIfFirstOpen()

        newExpenseViewModel.clearAllFields.observe(viewLifecycleOwner){ ob ->
            if(ob){
                newExpenseBinding.idENewExpense.isFocusable=true
                newExpenseBinding.idENewExpense.requestFocus()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                settingsViewModel.fnGetAllCategories()
            }
        }

//        newExpenseViewModel.expenseAmt.observe(viewLifecycleOwner){
//
//            Log.v("DEFAULT PAYMENT TYPE","Default Payment Type: ${newExpenseViewModel.amtInCash}")
//        }

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

        settingsViewModel.categoryList.observe(viewLifecycleOwner){ list ->
            categoryList = list
            val categoryNameList= list.map {it.categoryName}
            val autoCompleteAdapter= ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item,
                categoryNameList)

            newExpenseBinding.idDCategories.setAdapter(autoCompleteAdapter)

        }

        newExpenseBinding.idDCategories.setOnItemClickListener { parent,_,position,_ ->
            var selectedCategoryName = parent.getItemAtPosition(position).toString()
            newExpenseViewModel._selectedCategoryName.value=selectedCategoryName
            newExpenseViewModel._selectedCategoryId.value=categoryList[position].categoryId
            Log.v("SELECTED CATEGORY ID","Selected Category Id: ${newExpenseViewModel._selectedCategoryId.value}")
            Log.v("SELECTED CATEGORY NAME","Selected Category Name: ${newExpenseViewModel._selectedCategoryName.value}")
        }


//        newExpenseBinding.idSplitPayment.setOnCheckedChangeListener { _, isChecked ->
//            if(isChecked)
//            {
         newExpenseViewModel.showSplitDialog.observe(viewLifecycleOwner){ isChecked ->
             if(isChecked){
                 val view = layoutInflater.inflate(R.layout.split_dialogue,null)
                 var amtInCash = view.findViewById<TextInputEditText>(R.id.idEAmtInCash)
                 var amtInCard = view.findViewById<TextInputEditText>(R.id.idEAmtInCard)
                 var amtInUpi = view.findViewById<TextInputEditText>(R.id.idEAmtInUpi)

                 var btnOk=view.findViewById<Button>(R.id.idBtnOk)
                 var btnCancel=view.findViewById<TextView>(R.id.idBtnCancel)

                 var dialog = AlertDialog.Builder(requireContext())
                     .setView(view)
                     .setCancelable(false)
                     .create()

                 dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                 dialog.show()

                 btnOk.setOnClickListener {
                     newExpenseViewModel._amtInCash.value=amtInCash.text.toString().toFloatOrNull() ?:0.0f
                     newExpenseViewModel._amtInCard.value=amtInCard.text.toString().toFloatOrNull() ?:0.0f
                     newExpenseViewModel._amtInUpi.value=amtInUpi.text.toString().toFloatOrNull() ?:0.0f

                     val totAmt = newExpenseViewModel._amtInCash.value!! +
                             newExpenseViewModel._amtInCard.value!! +
                             newExpenseViewModel._amtInUpi.value!!

                     newExpenseViewModel._expenseAmt.value=totAmt.toString()



                     Log.v("AMT IN CASH","Amt In Cash: ${newExpenseViewModel.amtInCash.value}")
                     Log.v("AMT IN CARD","Amt In Card: ${newExpenseViewModel.amtInCard.value}")
                     Log.v("AMT IN UPI","Amt In Upi: ${newExpenseViewModel.amtInUpi.value}")
                     Log.v("TOTAL EXPENSE AMT","Total Expense Amt: ${newExpenseViewModel.expenseAmt.value}")


                     dialog.dismiss()

//                     Log.v("PAYMENT TYPE","Payment Type: SPLIT")
//                     newExpenseViewModel._paymentType.value=Global.PAYMENT_TYPE_SPLIT
//                     newExpenseViewModel._selectedpaymentType.value=R.id.idSplitPayment

                 }

                 btnCancel.setOnClickListener {
//                     newExpenseViewModel.fnCashPayment()
                     newExpenseViewModel._paymentType.value=-1
                     newExpenseViewModel._selectedpaymentType.value=-1
                     dialog.dismiss()
                 }
             }
         }


//            }

//        }

        newExpenseViewModel.valueMissingError.observe(viewLifecycleOwner){ errMsg ->
            if(!errMsg.isNullOrBlank())
            {
                fnShowMessage(errMsg,requireContext(),R.drawable.error_bg)
            }
        }

        newExpenseViewModel.insertStatus.observe(viewLifecycleOwner){ status ->
            if(status){
                fnShowMessage("Successfully Expense Details Are Stored",requireContext(),R.drawable.success_bg)
            }
            else{
                fnShowMessage("Something Wrong, Expense Details Are Not Stored",requireContext(),R.drawable.error_bg)
            }
        }

        return newExpenseBinding.root 
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

//private fun NewExpense.fnShowCategoryList(it: View) {
//    val inflater = LayoutInflater.from(requireContext())
//    val popupView = inflater.inflate(R.layout.category_list, null)
//
//
//
//    val popupWindow = PopupWindow(
//        popupView,
//        LinearLayout.LayoutParams.WRAP_CONTENT,
//        LinearLayout.LayoutParams.WRAP_CONTENT,
//        true
//    )
//
//    popupWindow.elevation = 8f
//
//    // Show below the button
//    popupWindow.showAsDropDown(it)
//}
