package com.example.catch_mentor.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.catch_mentor.R
import com.example.catch_mentor.databinding.CustomCatchMentoBottomNavBarBinding
import java.util.*

class CatchMentoBottomNavBar @JvmOverloads constructor(context: Context, attrs: AttributeSet?=null, defStyleAttr: Int=0)
    : ConstraintLayout(context,attrs,defStyleAttr) {

    /**
     * 콜백 사용법
     * CatchMentoBottomNavBar.setOnCatchMentoBottomNavBarCallBackListener(object:CatchMentoBottomNavBarCallBackListener{
     *      [onButtonClicked함수 오버라이드 해서 사용 state-> 버튼 누른 페이지]
     * })
     *
     * state
     * 0: 홈
     * 1: 마이페이지
     * 2: 학습현황
     * **/
    interface CatchMentoBottomNavBarCallBackListener{
        fun onButtonClicked(state:Int){}
    }

    var listener:CatchMentoBottomNavBarCallBackListener?=null
    fun setOnCatchMentoBottomNavBarCallbackListener(listener:CatchMentoBottomNavBarCallBackListener){
        this.listener=listener
    }

    companion object{
        val HOME=0
        val MY_PAGE=1
        val PROGRESS=2
    }
    private val binding= CustomCatchMentoBottomNavBarBinding.inflate(LayoutInflater.from(context),this,true)
    var arr = context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.CatchMentoBottomNavBar,
        0, 0
    )

    val state:Int
    init{
        try {
            state = arr.getInteger(R.styleable.CatchMentoBottomNavBar_state, HOME)

        } finally {
            arr.recycle()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        binding.catchMentoBottomNavBarHomeBtn.setOnClickListener {
            Log.d("btnbtn","home")
            listener?.onButtonClicked(HOME)
        }
        binding.catchMentoBottomNavBarMyPageBtn.setOnClickListener {
            Log.d("btnbtn","mp")
            listener?.onButtonClicked(MY_PAGE)
        }
        binding.catchMentoBottomNavBarProgressBtn.setOnClickListener {
            Log.d("btnbtn","prog")
            listener?.onButtonClicked(PROGRESS)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d("btnbtn","onDraw")

    }
}