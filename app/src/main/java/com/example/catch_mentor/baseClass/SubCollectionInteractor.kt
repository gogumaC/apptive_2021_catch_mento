package com.example.catch_mentor.baseClass

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

abstract class SubCollectionInteractor<RES_T>(val documentId: String):BaseInteractor<RES_T>() {

    abstract val collectionName:String
    abstract val mainCollectionName:String
    override val collectionPath: String
        get() = "$mainCollectionName/$documentId/$collectionName"
    override val ref: CollectionReference
        get() =db.collection(collectionPath)
//    final override val collectionPath: String
//        get() = "$mainCollectionName/$documentId/$collectionName"

}