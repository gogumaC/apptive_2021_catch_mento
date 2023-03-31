package com.example.catch_mentor.mentoringRecord.mentoringList

import android.graphics.Typeface
import android.icu.text.RelativeDateTimeFormatter
import android.text.Spannable
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseAdapter
import com.example.catch_mentor.dataClass.MentoringSchedule
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DateUtility.getDayOfWeek
import com.google.firebase.database.collection.LLRBNode
import java.time.format.TextStyle
import java.util.*

class MentoringListScheduleAdapter:BaseAdapter<MentoringListScheduleAdapter.MentoringListScheduleViewHolder, MentoringSchedule>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MentoringListScheduleViewHolder {
        val textView= TextView(parent.context).apply{
            val lp=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            this.setLayoutParams(lp)
        }
        return MentoringListScheduleViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MentoringListScheduleViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class MentoringListScheduleViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView){
        fun bind(schedule:MentoringSchedule){

            if(schedule.startTime!=null&&schedule.finishTime!=null){
                val dayOfWeek=schedule.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.KOREAN)
               // ("${String.format("%02d", hourOfDay)}:${String.format("%02d", minute)}

                val start= schedule.getTimeString(schedule.startTime!!)
                val finish=schedule.getTimeString(schedule.finishTime!!)

                val span: Spannable ="$dayOfWeek $start ~ $finish".toSpannable()
                span.setSpan(StyleSpan(Typeface.BOLD),0,0,Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                textView.setText(span)
            }


        }
    }
}