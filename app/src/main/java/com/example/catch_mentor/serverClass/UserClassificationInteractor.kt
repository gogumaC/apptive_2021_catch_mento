package com.example.catch_mentor.serverClass

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.navigation.findNavController
import com.example.catch_mentor.R
import com.example.catch_mentor.utils.UserDataManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.core.Single

open class UserClassificationInteractor() {

    open fun userClass(userID: String): Single<Boolean> {
        //멘토유저면 true 반환 아니면 false 반환
        return Single.create {

            val db = FirebaseFirestore.getInstance()
            var docRef = db.collection("mentor_user").document(userID)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists())  {
                        it.onSuccess(true)
                        Log.d("checkfor", userID + "멘토입니다. ${docRef.path}")
                        Log.d(ContentValues.TAG, userID + "멘토입니다.")
                    }else{
                        it.onSuccess(false)
                        Log.d("checkfor", userID + "멘티입니다. ${docRef.path}")
                        Log.d(ContentValues.TAG, userID + "멘티입니다.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }
}