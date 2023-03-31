package com.example.catch_mentor.dataClass

import com.example.catch_mentor.interactors.MentoringInteractor
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DateUtility.changeDate
import com.example.catch_mentor.utils.DateUtility.getDayOfWeek
import com.example.catch_mentor.utils.DateUtility.getFirstDateOfMonth
import com.google.firebase.firestore.DocumentReference
import java.io.Serializable
import java.util.*

data class Mentoring (val id:String?=null,
                      val title:String?=null,
                      val mentorId:String?=null,
                      val menteeId:String?=null,
                      val schedule:List<MentoringSchedule>?=null,
                      val mentor:DocumentReference?=null,
                      val mentee:DocumentReference?=null,
                      val subject:String?=null,
                      val startDate:Date?=null
                      ):Serializable
{

    fun makeEmptyLearningRecord(date: Date?=null):LearningRecord{
       // val mentoring=DocumentReference().
        return LearningRecord(title=title,date=date,mentor=this.mentor,mentoring=MentoringInteractor().getDocumentRef(this.id))
    }


    fun getMentoringCountInMonth(monthDate:Date){
        var total=0
        schedule?.forEachIndexed { index, mentoringSchedule ->
            if(mentoringSchedule.dayOfWeek!=null) {
                total+=DateUtility.getDOWCountInMonth(monthDate,mentoringSchedule.dayOfWeek)
            }

        }
    }

    fun getMonthProgress(monthDate: Date,monthRecords:List<LearningRecord>):Progress{
        val done=monthRecords.size

        var notYet=0
        var proceeding=0


        schedule?.forEachIndexed { index, mentoringSchedule ->
            if(mentoringSchedule.dayOfWeek!=null) {
                var cursor=monthDate.getFirstDateOfMonth()
                val instance=Calendar.getInstance()
                instance.setTime(cursor)

                while(cursor.before(monthDate.changeDate(Calendar.MONTH,1))){
                    if(cursor.getDayOfWeek()==mentoringSchedule.dayOfWeek){
                        if(cursor.before(Date())) proceeding++
                        else notYet++
                    }
                    cursor=cursor.changeDate(Calendar.DATE,1)
                }

            }

        }
        monthRecords.forEach{
            if(it.date?.before(Date())?:false) proceeding--
            else notYet--
        }
        return Progress(notYet,proceeding,done)

    }
}