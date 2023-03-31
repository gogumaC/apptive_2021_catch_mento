package com.example.catch_mentor.interactors

import com.example.catch_mentor.baseClass.BaseInteractor
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

open class MentorInteractor:BaseInteractor<Mentor>() {

    final override val collectionPath: String
        get() = "mentor_user"

    final override fun parseData(document: DocumentSnapshot): Mentor {
        val userType=0
        val charecterType=document["personality_type"] as? String
        val univ=document["univ"] as? String
        val major=document["major"] as? String
        val name = document["name"] as? String
        val mentees=(document["mentees"] as? List<*>)?.map{it as DocumentReference}
        val grade=document["grade"] as? Long
        val id=document.id

        val mento=Mentor(
            id=id,
//            userType = userType,
            personalityType = charecterType,
            name = name,
            univ = univ,
            major = major,
            mentees=mentees ,
            grade = grade?.toInt()
            )

        return mento
    }




}