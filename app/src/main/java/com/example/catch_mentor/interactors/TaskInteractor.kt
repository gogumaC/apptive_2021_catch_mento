package com.example.catch_mentor.interactors

import android.util.Log
import com.example.catch_mentor.baseClass.BaseInteractor
import com.example.catch_mentor.dataClass.Task
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

class TaskInteractor:BaseInteractor<Task>() {
    override val collectionPath: String
        get() = "tasks"

    override fun parseData(document: DocumentSnapshot): Task {
        val title=document["title"] as? String
        val content=document["content"] as? String
        val startDate=document["startDate"] as? Date
        val finishDate=document["finishDate"] as? Date
        val id=document.id

        val task= Task(
            id=id,
            title=title,
            content=content,
            startDate=startDate,
            finishDate=finishDate
        )

        return task

    }

    override fun createData(data: Task) {

        val task= hashMapOf(
          //  "title" to data.title,
            "content" to data.content,
            "start_date" to data.startDate,
            "finish_date" to data.finishDate,
            "mentor_id" to data.mentorId,
            "mentee_id" to data.menteeId
        )
        db.collection(collectionPath).document()
            .set(task)
            .addOnSuccessListener {
                publishIsSuccess.onNext(true)
            }
            .addOnFailureListener { e ->
                publishIsSuccess.onNext(false)
                Log.w("checkfor", "Error writing document", e) }
    }
}