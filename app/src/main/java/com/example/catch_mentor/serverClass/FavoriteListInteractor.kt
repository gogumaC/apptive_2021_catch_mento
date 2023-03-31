package com.example.catch_mentor.serverClass

import android.annotation.SuppressLint
import android.util.Log
import com.example.catch_mentor.search.SearchAdapter
import com.firebase.ui.auth.AuthUI.TAG
import com.google.firebase.firestore.FirebaseFirestore

open class FavoriteListInteractor(listener: SearchAdapter.OnItemClickListener){
    val db = FirebaseFirestore.getInstance()
    var favList : MutableList<String> = mutableListOf()
    var mCallback = listener
    fun Favadd(ID : String, favID : String){
        db.collection("mentor_user").document(ID)
            .get()
            .addOnSuccessListener { document ->
                favList = document["favorite_post"] as MutableList<String>
                favList.add(favID)
                FavUpload(favList, ID)
                mCallback.updateF()
            }
    }

    fun Favdel(ID : String, favID : String){
        db.collection("mentor_user").document(ID)
            .get()
            .addOnSuccessListener { document ->
                favList = document["favorite_post"] as MutableList<String>
                favList.remove(favID)
                FavUpload(favList, ID)
                mCallback.updateF()
            }
    }

    @SuppressLint("RestrictedApi")
    fun FavUpload(list: MutableList<String>, ID: String){
        val Ref = db.collection("mentor_user").document(ID)

// Set the "isCapital" field of the city 'DC'
        Ref
            .update("favorite_post", list)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }
}