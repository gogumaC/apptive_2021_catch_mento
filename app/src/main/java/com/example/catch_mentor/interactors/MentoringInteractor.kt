package com.example.catch_mentor.interactors

import com.example.catch_mentor.baseClass.BaseInteractor
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.MentoringSchedule
import com.example.catch_mentor.utils.DateUtility.getDayOfWeek
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import java.time.DayOfWeek
import java.util.*

class MentoringInteractor:BaseInteractor<Mentoring>() {
    override val collectionPath: String
        get() = "mentorings"

    override fun parseData(document: DocumentSnapshot): Mentoring {

        val id=document.getId()
        val title=document["title"] as? String
        val mentorId=document["mentor_id"] as? String
        val menteeId=document["mentee_id"] as? String
        val schedule=(document["schedule"] as? List<*>)?.map{it->
            (it as Map<*, *>)
            val start=(it["startTime"] as? Long)
            val finish=(it["finishTime"] as? Long)

            var dayOfWeek:DayOfWeek?=null
            it["dayOfWeek"]?.let{
                dayOfWeek=DayOfWeek.valueOf(it.toString())
            }
            MentoringSchedule(
                dayOfWeek = dayOfWeek,
                startTime=start?.toInt(),
                finishTime=finish?.toInt()
            )
        }
        val mentor=document["mentor"] as? DocumentReference
        val mentee=document["mentee"] as? DocumentReference
        val startDate=document["startDate"] as? Timestamp
        val subject=document["subject"] as? String

        val mentoring=Mentoring(
            id=id,
            title=title,
            mentorId = mentorId,
            menteeId=menteeId,
            schedule = schedule,
            mentor=mentor,
            mentee=mentee,
            startDate = startDate?.toDate(),
            subject = subject
        )

        return mentoring
    }


}