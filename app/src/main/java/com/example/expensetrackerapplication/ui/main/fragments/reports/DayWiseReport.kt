package com.example.expensetrackerapplication.ui.main.fragments.reports

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
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
import com.example.expensetrackerapplication.databinding.DayWiseReportBinding
import com.example.expensetrackerapplication.databinding.DayWiseReportListItemBinding
import com.example.expensetrackerapplication.model.DayWiseReportModel
import com.example.expensetrackerapplication.viewmodel.DayWiseReportViewModel
import com.example.expensetrackerapplication.viewmodel.ReportMenuViewModel

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

    private lateinit var dayWiseReportBinding : DayWiseReportBinding

    val dayWiseReportViewModel : DayWiseReportViewModel by viewModels()

    val reportMenuViewModel : ReportMenuViewModel by activityViewModels()

    lateinit var listAdapter : ListAdapter

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
        dayWiseReportBinding = DataBindingUtil.inflate(inflater,R.layout.day_wise_report, container, false)
        dayWiseReportBinding.dayWiseReportViewModel=dayWiseReportViewModel
        dayWiseReportBinding.lifecycleOwner = viewLifecycleOwner

        listAdapter = ListAdapter()
        dayWiseReportBinding.idDayWiseReportView.adapter = listAdapter
        dayWiseReportBinding.idDayWiseReportView.layoutManager = LinearLayoutManager(requireContext())

        dayWiseReportViewModel.closeDayWiseReport.observe(viewLifecycleOwner){ isClose ->
            if(isClose==true){
                findNavController().navigate(R.id.action_day_wise_report_to_report_menu)
            }
        }

        dayWiseReportBinding.idCalendarButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),
                { _,y,m,d ->
                    var date =  "$d-${m+1}-$y"
                    dayWiseReportViewModel._selectedDate.value=date

                    dayWiseReportViewModel.fnGetExpenseDetails(date)

            },year,month,day)

            datePickerDialog.show()
        }

        dayWiseReportViewModel.expenseList.observe(viewLifecycleOwner){ list ->
            listAdapter.fnSubmitList(list)
        }

        return dayWiseReportBinding.root
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
    private lateinit var expenseList : List<DayWiseReportModel>
    fun fnSubmitList(list: List<DayWiseReportModel>){
        expenseList=list
        notifyDataSetChanged()
    }
    inner class ListViewHolder (val binding: DayWiseReportListItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(item: DayWiseReportModel){
            binding.dayWiseReportListItem=item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<DayWiseReportListItemBinding>(inflater,R.layout.day_wise_report_list_item,parent,false)
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_wise_report_list_item,parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ListViewHolder,
        position: Int
    ) {
//        val item = list[position]
        holder.bind(expenseList[position])
    }

    override fun getItemCount(): Int {
       return expenseList.size
    }

}
