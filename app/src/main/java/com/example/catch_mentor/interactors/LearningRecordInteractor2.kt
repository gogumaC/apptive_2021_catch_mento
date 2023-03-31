package com.example.catch_mentor.interactors

import android.util.Log
import com.example.catch_mentor.baseClass.BaseInteractor
import com.example.catch_mentor.baseClass.SubCollectionInteractor
import com.example.catch_mentor.dataClass.LearningRecord
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.Progress
import com.example.catch_mentor.dataClass.Task
import com.example.catch_mentor.utils.DateUtility.getDayOfWeek
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import java.util.*

class LearningRecordInteractor2(val mentoring: Mentoring,val getDefaultRecroding:Boolean=false) :SubCollectionInteractor<LearningRecord>(mentoring.id.toString()) {
    override val collectionName: String
        get() = "learning_records"
    override val mainCollectionName: String
        get() = "mentorings"
    private var dateCursor:Date?=null


    override fun parseData(document: DocumentSnapshot): LearningRecord {

            val id = document.id
            val task_title = document["task_title"] as? String
            val task_content = document["task_content"] as? String
            val title = document["title"] as? String
            val mentorId =
                document["mentor_id"] as? String//(document["mentor"] as? List<*>)?.map{it as DocumentReference}
            val mentees = (document["mentees"] as? List<*>)?.map { it as DocumentReference }
            val mentor=document["mentor"] as? DocumentReference
        val mentoring=document["mentoring"] as? DocumentReference
            val date = document["date"] as? Timestamp
            val startTime = document["start_time"] as? Date
            val finishTime = document["finish_time"] as? Date
            val doneMentoringCount = document["mentoring_count_done"] as? Long
            val proceedingMentoringCount = document["mentoring_count_proceeding"] as? Long
            val notYetMentoringCount = document["mentoring_count_not_yet"] as? Long
            val isDone=document["isDone"] as? Boolean

            val task = Task(task_title, task_content)
            val progress = Progress(
                notYetMentoringCount?.toInt(),
                proceedingMentoringCount?.toInt(),
                doneMentoringCount?.toInt()
            )
            val learningRecord = LearningRecord(
                id = id,
                title = title,
                date = date?.toDate(),
                startTime = startTime,
                finishTime = finishTime,
                mentorId = mentorId,
                mentor=mentor,
                mentoring=mentoring,
                mentees = mentees,
                task = task,
                progress = progress,
                isDone = isDone?:false
                )


            return learningRecord
        }

    fun getData(query: Query,date:Date) {
        dateCursor=date
        getData(query)
    }

    override fun onRequestSuccessed(list: MutableList<LearningRecord>) {
        if(getDefaultRecroding){
            if(list.isEmpty()){
                mentoring.schedule?.forEach{
                    if(it.dayOfWeek==dateCursor?.getDayOfWeek()){
                        list.add(mentoring.makeEmptyLearningRecord(dateCursor))
                    }
                }
            }
        }
        super.onRequestSuccessed(list)
    }
}