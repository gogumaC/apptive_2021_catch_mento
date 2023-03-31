package com.example.catch_mentor.dataClass

import com.google.firebase.firestore.DocumentReference

data class Mentee(
    val id:String?=null,
//    val userType:Int?=null,
    val personalityType:String?=null,
    val name:String?=null,
    val school:String?=null,
    val grade:Int?=null,
    val major:String?=null,
    val mentors:List<DocumentReference>?=null
)
