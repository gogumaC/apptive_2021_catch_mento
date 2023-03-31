package com.example.catch_mentor.customView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.catch_mentor.R
import com.example.catch_mentor.dataClass.Progress
import com.example.catch_mentor.databinding.CustomProgressRecordGraphBinding

class ProgressRecordGraph @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0)
    : ConstraintLayout(context,attrs,defStyleAttr) {

    private val binding=CustomProgressRecordGraphBinding.inflate(LayoutInflater.from(context),this,true)
    var arr = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.ProgressRecordGraph,
        0, 0
    )


    val percentFontSize:Float
    init{
        try {
            percentFontSize = arr.getFloat(R.styleable.ProgressRecordGraph_percentFontSize,
                12f
            )
        } finally {
            arr.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setProgress(progress:Progress){
        binding.progress=progress
    }
}