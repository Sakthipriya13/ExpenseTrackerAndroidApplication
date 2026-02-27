package com.example.expensetrackerapplication.ui.main.fragments

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.paging.LOG_TAG
import com.bumptech.glide.Glide
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.AddIncomeBinding
import com.example.expensetrackerapplication.databinding.ChangePasswordBinding
import com.example.expensetrackerapplication.databinding.ConfirmationPromptBinding
import com.example.expensetrackerapplication.databinding.EditProfilePhotoBinding
import com.example.expensetrackerapplication.databinding.ProfileBinding
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.reusefiles.fnShowMessage
import com.example.expensetrackerapplication.ui.auth.Auth
import com.example.expensetrackerapplication.ui_event.EditProfilePhoto
import com.example.expensetrackerapplication.ui_event.ResultState
import com.example.expensetrackerapplication.viewmodel.AddInComeViewModel
import com.example.expensetrackerapplication.viewmodel.ChangePasswordViewModel
import com.example.expensetrackerapplication.viewmodel.EditProfilePhotoViewModel
import com.example.expensetrackerapplication.viewmodel.MainViewModel
import com.example.expensetrackerapplication.viewmodel.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.delay
import java.io.File
import java.security.Permissions
import java.text.SimpleDateFormat
import java.util.Locale

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

    private val mainViewModel : MainViewModel by activityViewModels()

    private  var profilePhotoUri : Uri? = null

//    private var galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri : Uri? ->
//        uri.let {
//            Log.i("IMAGE URI","Image Uri: $uri")
////            profileViewModel._profileUri.value=uri
//            profilePhotoUri=uri
//
//            profileViewModel.fnUpdateUserProfilePhoto(profilePhotoUri)
//
//        }
//    }


    private var galleryLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){ uri : Uri ->
        uri.let {
            Log.i("IMAGE URI","Image Uri: $uri")

            requireContext().contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            profilePhotoUri=it

            profileViewModel.fnUpdateUserProfilePhoto(profilePhotoUri)

        }
    }

    private var takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){ success ->
        if(success){
//            profileViewModel._profileUri.value=profilePhotoUri
            profileViewModel.fnUpdateUserProfilePhoto(profilePhotoUri)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.title= resources.getString(R.string.profile_frag)
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

        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        profileViewModel.fnGetUserProfilePhotoUri()


        profileViewModel.isDelAccount.observe(viewLifecycleOwner){ isDelAc ->
            if(isDelAc){
                if(Global.isCalendarSelected==false) {
                    Global.isCalendarSelected = true
                    val view = ConfirmationPromptBinding.inflate(layoutInflater)
                    view.tittle = "Warning!"
                    view.message = "Do You Want To Delete The Account?"

                    var delAcPrompt = AlertDialog.Builder(requireContext())
                        .setView(view.root)
                        .setCancelable(false)
                        .create()
                    delAcPrompt.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    view.idBtnOk.setOnClickListener {
                        Global.isCalendarSelected = false
                        profileViewModel.fnDeleteUserAccount()
                    }
                    view.idBtnCancel.setOnClickListener {
                        Global.isCalendarSelected = false
                        delAcPrompt.dismiss()
                    }
                    delAcPrompt.show()
                }
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
                if(Global.isBottomSheetSelected==false) {
                    Global.isBottomSheetSelected = true
                    ChangePassword().show(parentFragmentManager, "ChangePasswordBottomSheet")
                }
            }
        }

        profileViewModel.isEdit.observe(viewLifecycleOwner){ status ->
            if(status == true ){
                if(Global.isCalendarSelected==false) {
                    Global.isCalendarSelected = true
                    val view = EditProfilePhotoBinding.inflate(layoutInflater)

                    var delAcPrompt = AlertDialog.Builder(requireContext())
                        .setView(view.root)
                        .setCancelable(false)
                        .create()
                    delAcPrompt.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    view.idGalleryFlow.setOnClickListener {
                        galleryLauncher.launch(arrayOf("image/*"))
//                    profileViewModel.fnUpdateUserProfilePhoto(profilePhotoUri)
                        Global.isCalendarSelected = false
                        delAcPrompt.dismiss()
                    }

                    view.idCameraFlow.setOnClickListener {
                        checkCameraPermissions()
                        Global.isCalendarSelected = false
                        delAcPrompt.dismiss()
                    }

                    view.idBtnDelProfilePhoto.setOnClickListener {
                        profileBinding.idProfileImage.setImageResource(R.drawable.user)
                        profileViewModel.fnUpdateUserProfilePhoto(null)
                        Global.isCalendarSelected = false
                        delAcPrompt.dismiss()
                    }

                    view.idBtnCancel.setOnClickListener {
                        Global.isCalendarSelected = false
                        delAcPrompt.dismiss()
                    }
                    delAcPrompt.show()


//                val editOptions = arrayOf("Gallery","Camera")
//                var editPrompt = AlertDialog.Builder(requireContext())
//                    .setItems(editOptions){ _,which ->
//                        when(which){
//                            0 -> {
//                                galleryLauncher.launch("image/*")
//                            }
//                            1 -> {
//                                checkCameraPermissions()
//                            }
//                        }
//                    }
//                    .show()
                }
            }
        }


        profileViewModel.isAddIncome.observe(viewLifecycleOwner){ status ->
            if(status){
                if(Global.isBottomSheetSelected==false){
                    Global.isBottomSheetSelected=true
                    AddIncome().show(parentFragmentManager,"AddIncomeBottomSheet")
                }
            }
        }

        profileViewModel.profileUri.observe(viewLifecycleOwner){ uri ->
            if(uri!=null){
                Log.i("PROFILE URI","Profile Uri: $uri")
                profileBinding.idProfileImage.setImageURI(uri)
                mainViewModel.fnGetUserProfilePhotoUri()
            }
            else{
                profileBinding.idProfileImage.setImageResource(R.drawable.user)
                fnShowMessage("Something Wrong , Can't Load Profile Photo",requireContext(),R.drawable.error_bg)
            }
        }
    }

    fun checkCameraPermissions(){
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED )
        {
            openCamera()
        }
        else{
            requestPermissions(arrayOf(Manifest.permission.CAMERA),100)
        }
    }

    fun openCamera(){
        profilePhotoUri=createImageUri()
        takePictureLauncher.launch(profilePhotoUri)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            openCamera()
        }
    }

    fun createImageUri(): Uri{
        val file = File(requireContext().externalCacheDir,"camera_img_${Global.fnGetCurrentTime()}.jpg")
        return FileProvider.getUriForFile(requireContext(),"${requireContext().packageName}.provider",file)
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
                Global.isBottomSheetSelected = false
                dismiss()
            }
        }
        changePasswordViewModel.result.observe(viewLifecycleOwner){ state ->
            when(state){
                is ResultState.success  -> {
                    fnShowMessage(state.message,requireContext(),R.drawable.bg_success)
                    Global.isBottomSheetSelected = false
                    dismiss()
                }
                is ResultState.fail  -> {
                    fnShowMessage(state.message,requireContext(),R.drawable.bg_success)
                    Global.isBottomSheetSelected = false
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

        addIncomeBinding.idBtnCalendar.setOnClickListener {
            if(Global.isCalendarSelected==false)
            {
                Global.isCalendarSelected=true
                var calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(), { _,y,m,d ->
                       calendar.set(y,m,d)
                        val sdf1 = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

                        var date = sdf.format(calendar.time)
                        var dateUi = sdf1.format(calendar.time)

                        addIncomeViewModel._selectedDate.value=date
                        addIncomeViewModel._selectedDateUi.value=dateUi

                        Global.isCalendarSelected=false
                    },year,month,day
                )

                datePickerDialog.setCancelable(false)
                datePickerDialog.setCanceledOnTouchOutside(false)
                datePickerDialog.setOnCancelListener {
                    Global.isCalendarSelected=false
                    datePickerDialog.dismiss()
                }
                datePickerDialog.show()
            }
        }

        addIncomeViewModel.isLeave.observe(viewLifecycleOwner){ isLeave ->
            if(isLeave){
                Global.isBottomSheetSelected=false
                dismiss()
            }
        }

        addIncomeViewModel.insertStatus.observe(viewLifecycleOwner){ state ->
            when(state){
                is ResultState.success -> {
                    fnShowMessage(state.message,requireContext(),R.drawable.bg_success)
                    Global.isBottomSheetSelected=false
                    dismiss()
                }

                is ResultState.fail -> {
                    fnShowMessage(state.message,requireContext(),R.drawable.error_bg)
                    Global.isBottomSheetSelected=false
                    dismiss()
                }
            }
        }

        return addIncomeBinding.root
    }
}

//class EditProfile : BottomSheetDialogFragment(){
//
//    private lateinit var editProfilePhotoBinding : EditProfilePhotoBinding
//
//    val editProfilePhotoViewModel : EditProfilePhotoViewModel by viewModels()
//
//    val profileViewModel : ProfileViewModel by activityViewModels()
//
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        isCancelable = false
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        editProfilePhotoBinding = DataBindingUtil.inflate(inflater,R.layout.edit_profile_photo,container,false)
//        editProfilePhotoBinding.editProfilePhoto=editProfilePhotoViewModel
//        editProfilePhotoBinding.lifecycleOwner=viewLifecycleOwner
//
//
//        editProfilePhotoViewModel.isLeave.observe(viewLifecycleOwner){ isLeave ->
//            if(isLeave){
//                dismiss()
//            }
//        }
//
//        editProfilePhotoViewModel.isDelProfilePhoto.observe(viewLifecycleOwner){ isDel ->
//            if(isDel){
//                profileViewModel._isDelProfilePhoto.value =true
//            }
//        }
//
////        editProfilePhotoViewModel.editResult.observe(viewLifecycleOwner){ res ->
////            when(res){
////                is EditProfilePhoto.gallery -> galleryLauncher.launch("image/*")
////                is EditProfilePhoto.camera -> {
////
////                }
////            }
////        }
//
//        return editProfilePhotoBinding.root
//    }
//}