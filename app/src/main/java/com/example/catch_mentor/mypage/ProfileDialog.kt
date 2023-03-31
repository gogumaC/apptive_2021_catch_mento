package com.example.catch_mentor.mypage

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.example.catch_mentor.R

class ProfileDialog(context: Context)
{
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog(str : String)
    {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

        dialog.setContentView(R.layout.dialog_profile)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)


        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val edit_name : EditText = dialog.findViewById<EditText>(R.id.name_edit)
        edit_name.setText(str)

        dialog.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.finish_button).setOnClickListener {
            onClickListener.onClicked(edit_name.text.toString())
            dialog.dismiss()
        }


    }

    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }

}