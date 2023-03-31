package com.example.catch_mentor.interactors

import com.example.catch_mentor.baseClass.SubCollectionInteractor
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.Progress
import com.google.firebase.firestore.DocumentSnapshot

class ProgressInteractor(val mentoringId:String):SubCollectionInteractor<Progress>(mentoringId){
    override val collectionName: String
        get() = "progress"
    override val mainCollectionName: String
        get() = "mentorings"

    override fun parseData(document: DocumentSnapshot): Progress {
        val done=document["done"] as? Long
        val notYet=document["notYet"] as? Long
        val proceeding=document["proceeding"] as? Long
        return Progress(notYet?.toInt(),proceeding?.toInt(),done?.toInt())
    }
}