package com.example.expensetrackerapplication.ui.main.fragments.reports

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.ConfirmationPromptBinding
import com.example.expensetrackerapplication.databinding.DayWiseReportBinding
import com.example.expensetrackerapplication.databinding.DayWiseReportListItemBinding
import com.example.expensetrackerapplication.databinding.MainBinding
import com.example.expensetrackerapplication.model.CurrentDayReportModel
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.reusefiles.fnShowMessage
import com.example.expensetrackerapplication.ui_event.DayWiseReportClickListener
import com.example.expensetrackerapplication.viewmodel.DayWiseReportViewModel

import com.example.expensetrackerapplication.viewmodel.MainViewModel
import com.example.expensetrackerapplication.viewmodel.ReportMenuViewModel
import java.text.SimpleDateFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DayWiseReport.newInstance] factory method to
 * create an instance of this fragment.
 */
class DayWiseReport : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var DayWiseReportBinding : DayWiseReportBinding

    val DayWiseReportViewModel: DayWiseReportViewModel by viewModels()

    val reportMenuViewModel : ReportMenuViewModel by activityViewModels()

    val mainViewModel : MainViewModel by activityViewModels()

    lateinit var listAdapter : ListAdapter

//    private  var mainViewBinding : MainBinding = (requireActivity() as Main).mainDataBinding

    private lateinit var mainViewBinding : MainBinding


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
        DayWiseReportBinding = DataBindingUtil.inflate(inflater,R.layout.current_day_report2, container, false)
        DayWiseReportBinding.currentDayReportViewModel=DayWiseReportViewModel

        DayWiseReportBinding.lifecycleOwner = viewLifecycleOwner

        return DayWiseReportBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = ListAdapter()
        DayWiseReportBinding.idDayWiseReportView.adapter = listAdapter
        DayWiseReportBinding.idDayWiseReportView.layoutManager = LinearLayoutManager(requireContext())

        DayWiseReportViewModel.fnPreWarmExcelEngine()

        DayWiseReportViewModel.closeDayWiseReport.observe(viewLifecycleOwner){ isClose ->
            if(isClose==true){
                findNavController().navigate(R.id.action_day_wise_report_to_report_menu)
            }
        }

        DayWiseReportBinding.idCalendarButton.setOnClickListener {
            if(Global.isCalendarSelected==false)
            {
                Global.isCalendarSelected=true
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(requireContext(),
                    { _,y,m,d ->

                        calendar.set(y,m,d)
                        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                        val date = sdf.format(calendar.time)
//                    var date =  "$d-${m+1}-$y"
                        DayWiseReportViewModel._selectedDate.value=date
                        Global.isCalendarSelected=false

                    },year,month,day)
                datePickerDialog.setCancelable(false)
                datePickerDialog.setCanceledOnTouchOutside(false)

                datePickerDialog.setOnCancelListener {
                    Global.isCalendarSelected=false
                }

                datePickerDialog.show()
            }
        }

        DayWiseReportViewModel.selectedDate.observe(viewLifecycleOwner){ date ->
            Log.i("SELECTED DATE","Selected Date1: $date")
            DayWiseReportViewModel.fnClearFields()
            DayWiseReportViewModel.fnGetExpenseDetails(date)
        }

        DayWiseReportViewModel.expenseList.observe(viewLifecycleOwner){ list ->
            listAdapter.fnSubmitList(list, object : DayWiseReportClickListener {
                override fun onDeleteClick(expense: CurrentDayReportModel) {
                    if(!expense.isDelete.equals("DELETED"))
                        fnShowDeletePrompt(expense)
                    else
                        fnShowMessage("Expense Was Already Deleted",requireContext(),R.drawable.bg_info)

                }

            })
        }

        DayWiseReportViewModel.exportStatus.observe(viewLifecycleOwner){ status ->
            if(status){
                fnShowMessage("Report Successfully Exported",requireContext(),R.drawable.bg_success)
            }
            else{
                fnShowMessage("Report Export Failed",requireContext(),R.drawable.error_bg)
            }
        }

        DayWiseReportViewModel.expenseDeleteStatus.observe(viewLifecycleOwner){ status ->
            if(status){
                fnShowMessage("Successfully Expense Details Was Deleted",requireContext(),R.drawable.bg_success)
            }
            else{
                fnShowMessage("Delete Expense Details Was Failed",requireContext(),R.drawable.error_bg)
            }
        }

        DayWiseReportViewModel.isExportLoading.observe(viewLifecycleOwner){ isLoading ->
            if(isLoading){
                DayWiseReportBinding.isExportLoading.visibility=View.VISIBLE
            }
            else{
                DayWiseReportBinding.isExportLoading.visibility=View.GONE
            }
        }

    }

    fun fnShowDeletePrompt(expense : CurrentDayReportModel){
        var promptBinding = ConfirmationPromptBinding.inflate(layoutInflater)
        promptBinding.tittle = getString(R.string.warning)
        promptBinding.message = getString(R.string.do_you_want_to_delete_the_expense)
        val deletePrompt = AlertDialog.Builder(requireContext())
            .setView(promptBinding.root)
            .setCancelable(false)
            .create()
        deletePrompt.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        promptBinding.idBtnOk.setOnClickListener {
            DayWiseReportViewModel.fnDeleteExpense(expense.expenseId)
            deletePrompt.dismiss()
        }
        promptBinding.idBtnCancel.setOnClickListener {
            deletePrompt.dismiss()
        }
        deletePrompt.show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DayWiseReport.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DayWiseReport().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class ListAdapter() : RecyclerView.Adapter<ListAdapter.ListViewHolder>()
{
    private  var expenseList : List<CurrentDayReportModel> = emptyList()
    lateinit var deleteClickListener : DayWiseReportClickListener
    fun fnSubmitList(
        list: List<CurrentDayReportModel>,
        listener: DayWiseReportClickListener
    ){
        expenseList=list
        deleteClickListener = listener
        notifyDataSetChanged()
    }
    inner class ListViewHolder (val binding: DayWiseReportListItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: CurrentDayReportModel){
            binding.dayWiseReportListItem=item
            binding.deleteClickListener=deleteClickListener
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<DayWiseReportListItemBinding>(inflater,R.layout.day_wise_report_list_item,parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
        holder.bind(expenseList[position])
    }

    override fun getItemCount(): Int {
       return expenseList.size
    }

}
