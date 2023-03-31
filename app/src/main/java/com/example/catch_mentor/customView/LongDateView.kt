package com.example.catch_mentor.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.catch_mentor.R
import com.example.catch_mentor.databinding.CustomLongDateBinding
import com.example.catch_mentor.utils.DateUtility.getDayOfWeek
import java.time.format.TextStyle
import java.util.*

class LongDateView @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0)
    : ConstraintLayout(context,attrs,defStyleAttr) {

        private val binding=CustomLongDateBinding.inflate(LayoutInflater.from(context),this,true)
        private var date:Date=Date()
        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        }

    fun setDate(date: Date){
        this.date=date
        binding.itemSimpleLongDateWOD.text=date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN)
        binding.itemSimpleLongDateDate.text=date.date.toString()
    }

    fun getDate():Date =this.date

    fun changeSelected(isSelected:Boolean){
        if(isSelected){

            binding.itemSimpleLongDateBackground.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.date_selected))
            binding.itemSimpleLongDateDate.setTextColor(context.getColor(R.color.white))
            binding.itemSimpleLongDateWOD.setTextColor(context.getColor(R.color.white))

        }else{
            binding.itemSimpleLongDateBackground.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.date_unselected))
            binding.itemSimpleLongDateDate.setTextColor(context.getColor(R.color.text_dark))
            binding.itemSimpleLongDateWOD.setTextColor(context.getColor(R.color.text_light_dark))
        }
    }
}