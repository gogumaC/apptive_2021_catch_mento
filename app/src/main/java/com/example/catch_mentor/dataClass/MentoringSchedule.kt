package com.example.catch_mentor.dataClass


import java.time.DayOfWeek
import java.util.*

data class MentoringSchedule(val dayOfWeek:DayOfWeek?=null, var startTime: Int?=null, var finishTime: Int?=null){
    //Time은 분단위
    fun getTimeString(time:Int,format:String="HH:mm"):String{
        val hour=String.format("%02d", time/60)
        val min=String.format("%02d", time%60)

        var res=format
        res=res.replace("HH",hour)
        res=res.replace("mm",min)

        return res
    }


}