package com.example.catch_mentor.interactors

import com.example.catch_mentor.baseClass.BaseInteractor
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot

class MenteeInteractor:BaseInteractor<Mentee>() {

    final override val collectionPath: String
        get() = "mentee_user"

    final override fun parseData(document: DocumentSnapshot): Mentee {
        val userType=1
        val charecterType=document["personality_type"] as? String
        val school=document["school"] as? String
        val major=document["major"] as? String
        val name = document["name"] as? String
        val grade=document["grade"] as? Long
        val mentors=(document["mentors"] as? List<*>)?.map{it as DocumentReference }
        val id=document.id

        val mentee= Mentee(
            id=id,
            //userType = userType,
            personalityType = charecterType,
            name = name,
            school = school,
            major = major,
            mentors=mentors,
            grade = grade?.toInt()
            )
        return mentee
    }
}