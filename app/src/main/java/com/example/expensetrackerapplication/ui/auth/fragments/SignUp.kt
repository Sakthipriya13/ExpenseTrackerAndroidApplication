package com.example.expensetrackerapplication.ui.auth.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expensetrackerapplication.R
import com.example.expensetrackerapplication.databinding.SignUpBinding
import com.example.expensetrackerapplication.databinding.SignUpBindingImpl
import com.example.expensetrackerapplication.viewmodel.SignUpViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUp.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUp : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val signUpViewModel : SignUpViewModel by viewModels()

    private lateinit var signUpDataBinding : SignUpBinding

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
    ): View?
    {

        signUpDataBinding= DataBindingUtil.inflate(inflater,R.layout.sign_up, container, false)
        signUpDataBinding.lifecycleOwner=this
        signUpDataBinding.signUpViewModel=signUpViewModel

        signUpViewModel.insertStatus.observe(viewLifecycleOwner){ ob ->
            if(ob)
            {
                Log.d("DATA_INSERT_STATUS", "Data Successfully Inserted")
                findNavController().navigate(R.id.action_signup_to_login)
            }
            else
            {
                Log.d("DATA_INSERT_STATUS", "Data Successfully Inserted")

            }
        }

        signUpViewModel.actionGoToSignUp.observe(viewLifecycleOwner){ ob ->
            if(ob){
                findNavController().navigate(R.id.action_signup_to_login)
            }
        }

        return signUpDataBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUp.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUp().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}