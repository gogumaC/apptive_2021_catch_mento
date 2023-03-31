package com.example.catch_mentor.post

import android.annotation.SuppressLint
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.FirebaseFirestore

open class FavoriteListInteractorPost {

    val db = FirebaseFirestore.getInstance()
    var favList : MutableList<String> = mutableListOf()


    fun favadd(ID : String, favID : String){
        db.collection("mentor_user").document(ID)
            .get()
            .addOnSuccessListener { document ->
                favList = document["favorite_post"] as MutableList<String>
                favList.add(favID)
                favUpload(favList, ID)
            }
    }

    fun favdel(ID : String, favID : String){
        db.collection("mentor_user").document(ID)
            .get()
            .addOnSuccessListener { document ->
                favList = document["favorite_post"] as MutableList<String>
                favList.remove(favID)
                favUpload(favList, ID)
            }
    }

    @SuppressLint("RestrictedApi")
    fun favUpload(list: MutableList<String>, ID: String){
        val Ref = db.collection("mentor_user").document(ID)

// Set the "isCapital" field of the city 'DC'
        Ref
            .update("favorite_post", list)
            .addOnSuccessListener { Log.d(AuthUI.TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(AuthUI.TAG, "Error updating document", e) }
    }
}