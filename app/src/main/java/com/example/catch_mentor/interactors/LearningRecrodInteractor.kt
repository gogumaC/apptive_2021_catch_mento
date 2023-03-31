package com.example.catch_mentor.interactors

import com.example.catch_mentor.baseClass.BaseInteractor
import com.example.catch_mentor.dataClass.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

open class LearningRecrodInteractor():BaseInteractor<LearningRecord>() {
    final override val collectionPath: String
        get() = "learning_record"

    final override fun parseData(document: DocumentSnapshot): LearningRecord {
        val id=document.id
        val task_title=document["task_title"] as? String
        val task_content=document["task_content"] as? String
        val title=document["title"] as? String
        val mentor=document["mentor_id"] as? String//(document["mentor"] as? List<*>)?.map{it as DocumentReference}
        val mentees=(document["mentees"] as? List<*>)?.map{it as DocumentReference}
        val date=document["date"] as? Timestamp
        val startTime=document["start_time"] as? Date
        val finishTime=document["finish_time"] as? Date
        val doneMentoringCount=document["mentoring_count_done"] as? Long
        val proceedingMentoringCount=document["mentoring_count_proceeding"] as? Long
        val notYetMentoringCount=document["mentoring_count_not_yet"] as? Long
        val isDone=document["is_done"] as? Boolean

        val task=Task(task_title,task_content)
        val progress= Progress(notYetMentoringCount?.toInt(),proceedingMentoringCount?.toInt(),doneMentoringCount?.toInt())
        val learningRecord=LearningRecord(
            id=id,
            title=title,
            date=date?.toDate(),
            startTime=startTime,
            finishTime=finishTime,
            mentorId = mentor,
            mentees = mentees,
            task=task,
            progress = progress,
            isDone = isDone?:false
        )


        return learningRecord
    }
}