package com.example.expensetrackerapplication.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.ForgetPasswordBinding
import com.example.expensetrackerapplication.databinding.LoginBinding
import com.example.expensetrackerapplication.`object`.Global
import com.example.expensetrackerapplication.reusefiles.fnShowMessage
import com.example.expensetrackerapplication.ui.main.Main
import com.example.expensetrackerapplication.ui_event.ResultState
import com.example.expensetrackerapplication.viewmodel.ForgetViewModel
import com.example.expensetrackerapplication.viewmodel.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val loginViewModel : LoginViewModel by viewModels()
    private lateinit var loginDataBinding : LoginBinding

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

        loginDataBinding= DataBindingUtil.inflate(inflater,R.layout.login,container,false)
        loginDataBinding.loginViewModel=loginViewModel
        loginDataBinding.lifecycleOwner=viewLifecycleOwner

        return loginDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        loginDataBinding.idUserName.isFocusable=true
//        loginDataBinding.idUserName.requestFocus()

        loginViewModel.clearAllFields.observe(viewLifecycleOwner){ob ->
            if(ob){
                loginDataBinding.idUserName.isFocusable=true
                loginDataBinding.idUserName.requestFocus()
            }
        }

//        loginDataBinding.idPassword.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                loginDataBinding.idLogin.isFocusable = true
//                loginDataBinding.idLogin.requestFocus()
//            }
//        }

//        loginDataBinding.idUserName.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
//                loginDataBinding.idPassword.isFocusable = true
//                loginDataBinding.idPassword.requestFocus()
//            }
//        }

        loginViewModel.userNameEmptyStatus.observe(viewLifecycleOwner){ ob ->
            if(ob){
                fnShowMessage("User Name Are An Empty",requireContext(),R.drawable.error_bg)
            }
        }

        loginViewModel.userPasswordEmptyStatus.observe(viewLifecycleOwner){ ob ->
            if(ob){
                fnShowMessage("User Password Are An Empty",requireContext(),R.drawable.error_bg)
            }
        }

        loginViewModel.bothNameAndPasswordEmptyStatus.observe(viewLifecycleOwner){ ob ->
            if(ob){
                fnShowMessage("User Name & Password Were An Empty",requireContext(),R.drawable.error_bg)
            }
        }

        loginViewModel.loginStatus_fail.observe(viewLifecycleOwner){ ob ->
            if(ob)
            {
                Log.e("LOGIN STATUS", "Login Status Value Was False")
                fnShowMessage("User Not Found,Enter Valid User ",requireContext(),R.drawable.error_bg)
                loginViewModel._userName.value=""
                loginViewModel._userPassword.value=""
                loginDataBinding.idUserName.isFocusable=true
                loginDataBinding.idUserName.requestFocus()
            }
        }
        loginViewModel.loginStatus_success.observe(viewLifecycleOwner){ ob ->
            if(ob)
            {
                fnShowMessage("Successfully Login",requireContext(),R.drawable.bg_success)
//                findNavController().navigate(R.id.action_login_to_main)
                var intent = Intent(requireContext(), Main::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

        }

        loginViewModel.actionGoToSignUp.observe(viewLifecycleOwner){ ob ->
            if(ob)
            {
                findNavController().navigate(R.id.action_login_to_signup)
            }
            else
            {
                Log.e("GO TO SIGNUP", "Go To SignUp Value Was False")
            }
        }

        loginViewModel.isPasswordForget.observe(viewLifecycleOwner){ status ->
            if(status){
                if(Global.isBottomSheetSelected == false){
                    Global.isBottomSheetSelected = true
                    ForgetPassword().show(parentFragmentManager,"ForgetBottomSheet")
                }
            }
        }
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
//
//            val imeBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
//
//            v.setPadding(
//                v.paddingLeft,
//                v.paddingTop,
//                v.paddingRight,
//                imeBottom
//            )
//
//            insets
//        }
//    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class ForgetPassword : BottomSheetDialogFragment(){
    private lateinit var forgetBinding : ForgetPasswordBinding
    private val forgetViewModel : ForgetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        forgetBinding = DataBindingUtil.inflate(inflater,R.layout.forget_password,container,false)
        forgetBinding.forget = forgetViewModel
        forgetBinding.lifecycleOwner = viewLifecycleOwner

        return forgetBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forgetViewModel.isCancel.observe(viewLifecycleOwner){
            Global.isBottomSheetSelected = false
            dismiss()
        }

        forgetViewModel.resetStatus.observe(viewLifecycleOwner){ state ->
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

    }
}