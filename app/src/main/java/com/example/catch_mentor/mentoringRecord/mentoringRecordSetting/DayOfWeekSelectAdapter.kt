package com.example.catch_mentor.mentoringRecord.mentoringRecordSetting

import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TableLayout
import androidx.annotation.RequiresApi
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseAdapter
import com.example.catch_mentor.utils.DeviceUtil.dpToPx
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*


class DayOfWeekSelectAdapter(val deviceWidth:Int):BaseAdapter<DayOfWeekSelectAdapter.DayOfWeekViewHolder,DayOfWeek>() {
//리스트를 체크박스로 변경 요
    //dow에 대한 선택사항을 뷰모델에서 처리 요
    //
private var selectedDays= mutableListOf<DayOfWeek>()

    init{
        val dayOfWeek=DayOfWeek.values().toList()
        itemList.addAll(dayOfWeek)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayOfWeekViewHolder {
        val checkBox=CheckBox(parent.context).apply{
            setGravity(Gravity.CENTER)
            setPadding(10)
            setBackgroundResource(R.drawable.sytle_r10)
            buttonDrawable=null
            val lp=ViewGroup.LayoutParams(((deviceWidth-(68+28).dpToPx(parent.context))/7).toInt(),ViewGroup.LayoutParams.WRAP_CONTENT)
            setLayoutParams(lp)
        }

        return DayOfWeekViewHolder(checkBox)
    }

    override fun onBindViewHolder(holder: DayOfWeekViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun refreshSelectDays(list:MutableList<DayOfWeek>){
        selectedDays=list
        notifyDataSetChanged()
    }
    fun unSelect(dayOfWeek: DayOfWeek){
        selectedDays.remove(dayOfWeek)
        notifyItemChanged(itemList.indexOf(dayOfWeek))
    }

    inner class DayOfWeekViewHolder(val checkBox: CheckBox): RecyclerView.ViewHolder(checkBox){


        fun bind(dayOfWeek:DayOfWeek){
            checkBox.setEnabled(!selectedDays.contains(dayOfWeek))
            checkBox.setText(dayOfWeek.getDisplayName(TextStyle.SHORT,Locale.KOREAN))
            checkBox.setChecked(selectedDays.contains(dayOfWeek))
            checkBox.setOnClickListener {
                if(checkBox.isChecked) {
                  //  checkBox.setTextColor(Color.parseColor("#2f54c4"))
                    publishItemClick.onNext(dayOfWeek)
                    checkBox.setEnabled(false)
                }
                else{
                   // checkBox.setTextColor(Color.parseColor("#505050"))
                }
            }



        }


    }

}