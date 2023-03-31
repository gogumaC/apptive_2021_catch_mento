package com.example.catch_mentor.utils

import android.util.Log
import com.example.catch_mentor.utils.DateUtility.clearDate
import com.example.catch_mentor.utils.DateUtility.clearTime
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

object DateUtility {

    fun getNextDays(period:Int=60):List<Date>{
        val calendar=Calendar.getInstance()
        val today=Date()
        calendar.setTime(today)
        val days= mutableListOf<Date>()
        for(i in 0..period){
            calendar.add(Calendar.DATE,1)
            days.add(calendar.time)
        }
        return days
    }

//    fun Date.getDayOfWeek():String{
//        val dayOfWeekString=listOf("","일","월","화","수","목","금","토")
//        val calendar=Calendar.getInstance()
//        calendar.setTime(this)
//        return try {
//            dayOfWeekString.get(calendar.get(Calendar.DAY_OF_WEEK))
//        }catch (e:Exception){
//            "에러"
//        }
//
//    }

    fun Date.getDayOfWeek(): DayOfWeek {
        val calendar=Calendar.getInstance()
        calendar.setFirstDayOfWeek(Calendar.MONDAY)
        calendar.setTime(this)

        val dow=calendar.get(Calendar.DAY_OF_WEEK)
        val value=if(dow==1)7 else dow-1
        return DayOfWeek.of(value)

    }

    fun getFisrtDateOfWeek(date:Date=Date()):Date{
        val calendar=Calendar.getInstance()
        calendar.setTime(date)
        while(calendar.get(Calendar.DAY_OF_WEEK)!=2){
            calendar.add(Calendar.DATE,-1)
        }
        return calendar.getTime()
    }

    fun getLastDateOfWeek(date:Date=Date()):Date{
        val calendar=Calendar.getInstance()
        calendar.setTime(date)
        while(calendar.get(Calendar.DAY_OF_WEEK)!=1){
            calendar.add(Calendar.DATE,1)
        }
        return calendar.getTime()
    }

    fun Date.getFirstDateOfMonth():Date{
        return this.clearDate()
    }

    fun Date.getLastDateOfMonth():Date{
        val calendar=Calendar.getInstance()
        calendar.setTime(this.clearDate())
        calendar.add(Calendar.MONTH,1)
        calendar.add(Calendar.DATE,-1)

        return calendar.getTime()
    }

    fun Calendar.clearTime(){
        set(Calendar.MILLISECOND, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }

    fun Calendar.clearDate(){
        set(Calendar.MILLISECOND, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.DATE,1)
    }
    fun Date.clearTime():Date{
        val cal=Calendar.getInstance()
        cal.setTime(this)
        cal.clearTime()
        return cal.time
    }
    fun Date.clearDate():Date{
        val cal=Calendar.getInstance()
        cal.setTime(this)
        cal.clearDate()
        return cal.time
    }

    fun Date.getYear2():Int{
        val cal=Calendar.getInstance()
        cal.setTime(this)
        return cal.get(Calendar.YEAR)
    }
    fun Date.getMonth2():Int{
        val cal=Calendar.getInstance()
        cal.setTime(this)
        return cal.get(Calendar.MONTH)
    }
    fun Date.getDate2():Int{
        val cal=Calendar.getInstance()
        cal.setTime(this)
        return cal.get(Calendar.DATE)
    }
//1일~ 첫번째 일요일,
//    fun getWeekDates(month:Int,weekCount:Int):List<Date>{
//        val cal=Calendar.getInstance()
//        cal.set(Calendar.WEEK_OF_MONTH,weekCount)
//        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH,)
//        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
//    }

    fun makeDate(year:Int,month:Int,date:Int):Date{
        val cal=Calendar.getInstance()
        cal.set(year,month, date)
        return cal.time
    }

    fun parseDate(sDate:String,format:String="yyyy/MM/dd"):Date{
        val sf=SimpleDateFormat(format)
        val date=sf.parse(sDate)
        return date
    }

    fun formatDate(date:Date,format:String="yyyy/MM/dd"):String{
        val sf=SimpleDateFormat(format)
        val sDate=sf.format(date)
        return sDate
    }

    fun Date.changeDate(unit:Int=Calendar.DATE,num:Int):Date{
        val instance=Calendar.getInstance()
        instance.setTime(this)
        instance.add(unit,num)
        return instance.time
    }

    fun getDOWCountInMonth(date:Date,dow:DayOfWeek):Int{
        var count=0
        val cursor=date.getFirstDateOfMonth()

        val instance=Calendar.getInstance()
        instance.setTime(cursor)

        while(cursor.before(date.changeDate(Calendar.MONTH,1))){
            if(cursor.getDayOfWeek()==dow)count++
            cursor.changeDate(Calendar.DATE,1)
            count++
        }

        return count
    }

    fun Date.getFieldData(field:Int):Int{
        val inst=Calendar.getInstance()
        inst.setTime(this)
        return inst.get(field)
    }

    //fun Date.getString():String{}

}