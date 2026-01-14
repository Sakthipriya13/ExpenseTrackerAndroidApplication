package com.example.expensetrackerapplication.ui.main.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.AddIncomeBinding
import com.example.expensetrackerapplication.databinding.ChangePasswordBinding
import com.example.expensetrackerapplication.databinding.ConfirmationPromptBinding
import com.example.expensetrackerapplication.databinding.EditProfilePhotoBinding
import com.example.expensetrackerapplication.databinding.ProfileBinding
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.reusefiles.fnShowMessage
import com.example.expensetrackerapplication.ui.auth.Auth
import com.example.expensetrackerapplication.ui_event.ResultState
import com.example.expensetrackerapplication.viewmodel.AddInComeViewModel
import com.example.expensetrackerapplication.viewmodel.ChangePasswordViewModel
import com.example.expensetrackerapplication.viewmodel.EditProfilePhotoViewModel
import com.example.expensetrackerapplication.viewmodel.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var profileBinding: ProfileBinding

    private val profileViewModel : ProfileViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title= "Profile"
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
        profileBinding= DataBindingUtil.inflate(inflater,R.layout.profile,container,false)
        profileBinding.profile=profileViewModel
        profileBinding.lifecycleOwner=viewLifecycleOwner

        profileViewModel.isEdit.observe(viewLifecycleOwner) { isEdit ->
            if (isEdit) {

            }
        }

        profileViewModel.isDelAccount.observe(viewLifecycleOwner){ isDelAc ->
            if(isDelAc){
                val view = ConfirmationPromptBinding.inflate(layoutInflater)
                view.tittle="Warning!"
                view.message="Do You Want To Delete The Account?"

                var delAcPrompt = AlertDialog.Builder(requireContext())
                    .setView(view.root)
                    .setCancelable(false)
                    .create()
                delAcPrompt.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                view.idBtnOk.setOnClickListener {
                    profileViewModel.fnDeleteUserAccount()
                }
                view.idBtnCancel.setOnClickListener {
                    delAcPrompt.dismiss()
                }
                delAcPrompt.show()
            }
        }

        profileViewModel.deleteUserAcStatus.observe(viewLifecycleOwner){ isDeleted ->
            if(isDeleted){
                fnShowMessage("User Account Successfully Deleted",requireContext(),R.drawable.bg_success)
                var intent = Intent(requireContext(),Auth::class.java)
                startActivity(intent)
            }
            else{
                fnShowMessage("User Account Delete Was Failed",requireContext(),R.drawable.bg_success)
            }
        }

        profileViewModel.isChangePassword.observe(viewLifecycleOwner){ status ->
            if(status){
                ChangePassword().show(parentFragmentManager,"ChangePasswordBottomSheet")
            }
        }


        profileViewModel.isEdit.observe(viewLifecycleOwner){ status ->
            if(status){
                AddIncome().show(parentFragmentManager,"EditProfilePhotoBottomSheet")
            }
        }


        profileViewModel.isAddIncome.observe(viewLifecycleOwner){ status ->
            if(status){
                EditProfile().show(parentFragmentManager,"AddIncomeBottomSheet")
            }
        }

        return profileBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class ChangePassword : BottomSheetDialogFragment(){
    private lateinit var changePasswordBinding : ChangePasswordBinding
    private val changePasswordViewModel : ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        changePasswordBinding= DataBindingUtil.inflate(inflater,R.layout.change_password,container,false)
        changePasswordBinding.changePassword=changePasswordViewModel
        changePasswordBinding.lifecycleOwner=viewLifecycleOwner

        changePasswordViewModel.isCancel.observe(viewLifecycleOwner){ isCancel ->
            if(isCancel){
                dismiss()
            }
        }
        changePasswordViewModel.result.observe(viewLifecycleOwner){ state ->
            when(state){
                is ResultState.success  -> {
                    fnShowMessage(state.message,requireContext(),R.drawable.bg_success)
                    dismiss()
                }
                is ResultState.fail  -> {
                    fnShowMessage(state.message,requireContext(),R.drawable.bg_success)
                    dismiss()
                }
            }
        }
//        return super.onCreateView(inflater, container, savedInstanceState)
        return changePasswordBinding.root

    }
}

class AddIncome : BottomSheetDialogFragment(){

    private lateinit var addIncomeBinding : AddIncomeBinding
    val addIncomeViewModel : AddInComeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addIncomeBinding = DataBindingUtil.inflate(inflater,R.layout.add_income,container,false)
        addIncomeBinding.addIncome = addIncomeViewModel
        addIncomeBinding.lifecycleOwner=viewLifecycleOwner

        return addIncomeBinding.root
    }
}

class EditProfile : BottomSheetDialogFragment(){

    private lateinit var editProfilePhotoBinding : EditProfilePhotoBinding

    val editProfilePhotoViewModel : EditProfilePhotoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        editProfilePhotoBinding = DataBindingUtil.inflate(inflater,R.layout.edit_profile_photo,container,false)
        editProfilePhotoBinding.editProfilePhoto=editProfilePhotoViewModel
        editProfilePhotoBinding.lifecycleOwner=viewLifecycleOwner


        return editProfilePhotoBinding.root
    }
}