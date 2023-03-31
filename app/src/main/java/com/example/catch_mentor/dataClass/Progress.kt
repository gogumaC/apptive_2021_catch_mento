package com.example.catch_mentor.dataClass

import android.util.Log

data class Progress (val notYet:Int?,val proceeding:Int?,val done:Int?){

    val progressPercentage:Int
        get() = getPercentage()

    fun getPercentage():Int{
        try{
            val res:Float=(done!!)/(notYet!!+proceeding!!+done).toFloat()*100
            Log.e("progressPercent","done : $done $notYet $proceeding  "+res.toString())
            return res.toInt()
        }catch(e:Exception){
            Log.e("progressPercent",e.toString())
            return 0
        }

    }

}