package com.example.catch_mentor.dataClass

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable

data class Mentor(val id:String?=null,
//                  val userType:Int?=null,
                  val personalityType:String?=null,
                  val name:String?=null,
                  val univ:String?=null,
                  val major:String?=null,
                  val grade:Int?=null,
                  val mentees:List<DocumentReference>?=null
): Serializable