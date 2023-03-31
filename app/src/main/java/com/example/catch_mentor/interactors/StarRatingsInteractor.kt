package com.example.catch_mentor.interactors

import android.util.Log
import com.example.catch_mentor.baseClass.SubCollectionInteractor
import com.example.catch_mentor.dataClass.starRatingUnitData

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.Timestamp

class StarRatingsInteractor(documentId:String):SubCollectionInteractor<starRatingUnitData>(documentId) {
    override val collectionName: String
        get() = "starRatings"

    override val mainCollectionName: String
        get() = "mentor_user"

    override fun parseData(document: DocumentSnapshot): starRatingUnitData {
        val id=ref.id //TODO(얘는 좀 수정 필요할듯?)
        val time=document["time"] as? Timestamp
        val mentoringId=document["mentoringId"] as? String
        val starRating=document["starRating"] as? Long
        return starRatingUnitData(id,time?.toDate(),mentoringId,starRating)

    }

    override fun createData(data: starRatingUnitData) {
        super.createData(data)
        updateAverageStarRating()
    }

    private fun updateAverageStarRating(){
        var sum=0
        var count=0
        ref.get().addOnSuccessListener { result ->
            // 성공할 경우
            Log.d("checkfor","ref : "+ref.path )
            for (document in result) {
                Log.d("checkfor", document.toString())
                val item = parseData(document)
                item.starRating?.let{
                    sum+=it.toInt()
                    count++
                }

            }
            val avg=sum.toFloat()/count
            db.collection("mentor_user").document(documentId).update("star_rating",avg)
        }
    }
}