package com.example.catch_mentor.interactors

import com.example.catch_mentor.baseClass.SubCollectionInteractor
import com.example.catch_mentor.dataClass.Memo
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

class CalendarMemoInteractor(documentId:String):SubCollectionInteractor<Memo>(documentId) {
    override val collectionName: String
        get() = "memos"
    override val mainCollectionName: String
        get() = "mentorings"

    override fun parseData(document: DocumentSnapshot): Memo {
        val id=document.id
        val date=document["date"] as? Timestamp
        val memo=document["memo"] as? String
        return Memo(
            id=id,
            date=date?.toDate(),
            memo=memo
            )
    }
}