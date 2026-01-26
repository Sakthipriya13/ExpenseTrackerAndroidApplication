package com.example.expensetrackerapplication.ui.main.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.CategoryListItemBinding
import com.example.expensetrackerapplication.databinding.ConfirmationPromptBinding
import com.example.expensetrackerapplication.databinding.SettingsBinding
import com.example.expensetrackerapplication.datastore.LanguageDataStore
import com.example.expensetrackerapplication.datastore.ThemeColorDataStore
import com.example.expensetrackerapplication.model.CategoryModel
import com.example.expensetrackerapplication.model.DayWiseReportModel
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.reusefiles.fnShowMessage
import com.example.expensetrackerapplication.ui_event.CategoryItemClickListener
import com.example.expensetrackerapplication.ui_event.DayWiseReportClickListener
import com.example.expensetrackerapplication.ui_event.ResultState
import com.example.expensetrackerapplication.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Settings.newInstance] factory method to
 * create an instance of this fragment.
 */
class Settings : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var settingsBinding: SettingsBinding

    val settingsViewModel : SettingsViewModel by viewModels()

    lateinit  var categoryAdapter : CategoryAdapter

    private lateinit var languageDataStore: LanguageDataStore

    private lateinit var themeColorDataStore : ThemeColorDataStore

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title= "Settings"
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
        settingsBinding= DataBindingUtil.inflate(inflater,R.layout.settings, container, false)
        settingsBinding.settingsViewModel=settingsViewModel
        settingsBinding.lifecycleOwner=viewLifecycleOwner
        return settingsBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        languageDataStore= LanguageDataStore(requireContext())
        themeColorDataStore = ThemeColorDataStore(requireContext())

        categoryAdapter = CategoryAdapter()
        settingsBinding.idAddedCategoryList.adapter=categoryAdapter
        settingsBinding.idAddedCategoryList.layoutManager= LinearLayoutManager(requireContext())

        settingsViewModel.fnGetDefaultCategories()
        settingsViewModel.fnGetAllCategories()

        settingsViewModel.categoryList.observe(viewLifecycleOwner){ list ->
            var categoryNameList : List<CategoryModel> = list.map { name ->
                CategoryModel(userId = name.userId,categoryId = name.categoryId,categoryName = name.categoryName)
            }
            categoryAdapter.fnSubmitList(categoryNameList, object : CategoryItemClickListener{

                override fun onRemoveClick(category: CategoryModel) {
                    fnShowDeletePrompt(category)
                }
            }
            )
        }

        settingsViewModel.insertStaus.observe(viewLifecycleOwner){ state ->
            when(state){
                is ResultState.success -> fnShowMessage(state.message,requireContext(),R.drawable.bg_success)
                is ResultState.fail -> fnShowMessage(state.message,requireContext(),R.drawable.error_bg)
            }
        }

        settingsViewModel.delStatus.observe(viewLifecycleOwner){ state ->
            when(state){
                is ResultState.success -> fnShowMessage(state.message,requireContext(),R.drawable.bg_success)
                is ResultState.fail -> fnShowMessage(state.message,requireContext(),R.drawable.error_bg)
            }
        }

        settingsBinding.idLanGroup.setOnCheckedChangeListener { _,checkedId ->
            when(checkedId){
                R.id.idRbEnglish -> {
                    fnUpdateLan("en")
                }
                R.id.idRbTamil -> {
                    fnUpdateLan("ta")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
//            val lang = languageDataStore.languageFlow?.first() ?:"en"
            val lang = languageDataStore.fnGetLanguage()

//            languageDataStore.languageFlow?.collect { lang ->
            when(lang){
                "en" ->{
                    fnShowMessage("Language: English",requireContext(),R.drawable.bg_success)
                    settingsBinding.idRbEnglish.isChecked = true
                }
                "ta" -> {
                    fnShowMessage("Language: Tamil",requireContext(),R.drawable.bg_success)
                    settingsBinding.idRbTamil.isChecked = true
                }
            }
//            }
        }

        settingsViewModel.isLoading.observe(viewLifecycleOwner){ status ->
            if(status){
                settingsBinding.idLoading.visibility = View.VISIBLE
            }
            else{
                settingsBinding.idLoading.visibility = View.GONE
            }
        }

        settingsBinding.idColor1.setOnClickListener {
            fnUpdateThemeColor(Global.COLOR_CODE1)
        }

        settingsBinding.idColor2.setOnClickListener {
            fnUpdateThemeColor(Global.COLOR_CODE2)
        }

        settingsBinding.idColor3.setOnClickListener {
            fnUpdateThemeColor(Global.COLOR_CODE3)
        }

        settingsBinding.idColor4.setOnClickListener {
            fnUpdateThemeColor(Global.COLOR_CODE4)
        }

        settingsBinding.idColor5.setOnClickListener {
            fnUpdateThemeColor(Global.COLOR_CODE5)
        }


    }

    fun fnUpdateThemeColor(colorCode : Int){
        lifecycleScope.launch {
            val currColor = themeColorDataStore.fnGetThemeColor()

            if(currColor == colorCode) return@launch

            settingsViewModel._isLoading.value = true

            themeColorDataStore.fnSaveThemeColor(colorCode)

            settingsViewModel._isLoading.value = false

            requireActivity().recreate()


        }

    }

    fun fnUpdateLan(langCode : String){
        lifecycleScope.launch {

            val currLang = languageDataStore.fnGetLanguage()

            if(currLang != langCode){

                settingsViewModel._isLoading.value = true

                languageDataStore.fnSaveLanguage(langCode)

                delay(200)

                requireActivity().recreate()

                settingsViewModel._isLoading.value = false
            }
            else{
                settingsViewModel._isLoading.value = false
            }

//            settingsViewModel._isLoading.value = false
        }
    }

    fun fnShowDeletePrompt(category : CategoryModel){
        if(Global.displayDialogPrompt==false){
            Global.displayDialogPrompt=true
            var promptBinding = ConfirmationPromptBinding.inflate(layoutInflater)
            promptBinding.tittle = getString(R.string.warning)
            promptBinding.message = getString(R.string.do_you_want_to_delete_the_category)
            val deletePrompt = AlertDialog.Builder(requireContext())
                .setView(promptBinding.root)
                .setCancelable(false)
                .create()
            deletePrompt.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            promptBinding.idBtnOk.setOnClickListener {
                settingsViewModel.fnDeleteCategory(category.categoryId,category.userId)
                Global.displayDialogPrompt=false
                deletePrompt.dismiss()
            }
            promptBinding.idBtnCancel.setOnClickListener {
                Global.displayDialogPrompt=false
                deletePrompt.dismiss()
            }
            deletePrompt.show()
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Settings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Settings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class CategoryAdapter() : RecyclerView.Adapter<CategoryAdapter.ListViewHolder>()
{
    private var categoryNameList : List<CategoryModel> = emptyList()
    private lateinit var onCLickRemove : CategoryItemClickListener
    fun fnSubmitList(
        list: List<CategoryModel>,
        listener : CategoryItemClickListener){
        categoryNameList=list
        onCLickRemove=listener
        notifyDataSetChanged()
    }

    inner class ListViewHolder(val binding: CategoryListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(name : CategoryModel){
            binding.category=name
            binding.onClickRemove=onCLickRemove
            binding.executePendingBindings()
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.ListViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        var view = DataBindingUtil.inflate<CategoryListItemBinding>(inflater,R.layout.category_list_item,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ListViewHolder, position: Int) {
        holder.bind(categoryNameList[position])
    }

    override fun getItemCount(): Int {
        return categoryNameList.size
    }

}