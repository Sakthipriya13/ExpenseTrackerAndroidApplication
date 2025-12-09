package com.example.expensetrackerapplication.reusefiles

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.expensetrackerapplication.R



fun Fragment.fnShowMessage(msg: String, context: Context, bg: Int){
    try
    {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_toast_message,null,false)

        var toastMsg = view.findViewById<TextView>(R.id.success_Msg)
        toastMsg.text=msg
        var layout = view.findViewById<LinearLayout>(R.id.idLayout)
        layout.setBackgroundResource(bg)
        var toast = Toast(context).apply {
            duration = Toast.LENGTH_LONG
            setGravity(Gravity.TOP,0,100)
            this.view =view
        }
//        toast.duration=Toast.LENGTH_LONG
//        toast.setGravity(Gravity.TOP,0,0)
//        toast.view=view
        toast.show()

    }
    catch(e : Exception)
    {
        Log.e("FUNCTION SHOW ERROR MESSAGE","Function Show Error Message: $e")
    }
}