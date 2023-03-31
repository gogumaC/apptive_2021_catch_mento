package com.example.catch_mentor.chatting

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.catch_mentor.R
import org.w3c.dom.Text

class ChatDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog(str : String)
    {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setContentView(R.layout.dialog_chatting)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)


        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.findViewById<TextView>(R.id.dialog_chat_txt).text = str
        if(str == "매칭 중") dialog.findViewById<TextView>(R.id.dialog_chat_txt2).text = " 으로"
        if(str == "매칭 완료") dialog.findViewById<TextView>(R.id.dialog_chat_txt3).visibility = View.VISIBLE

        dialog.findViewById<Button>(R.id.dialog_chat_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.dialog_chat_fin).setOnClickListener {
            onClickListener.onClicked(str)
            dialog.dismiss()
        }


    }

    interface OnDialogClickListener
    {
        fun onClicked(name: String)
    }
}