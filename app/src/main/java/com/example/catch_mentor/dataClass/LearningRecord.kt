package com.example.catch_mentor.dataClass

import com.google.firebase.firestore.DocumentReference
import java.util.*

data class LearningRecord(val id:String?=null,
                          val title:String?=null,
                          val date: Date?=null,
                          val mentorId:String?=null,//List<DocumentReference>?=null,
                          val mentor:DocumentReference?=null,
                          val mentees:List<DocumentReference>?=null,
                          val mentoring: DocumentReference?=null,
                          val task:Task?=null,
                          val progress:Progress?=null,
                          val startTime:Date?=null,
                          val finishTime:Date?=null,
                          @field:JvmField
                          val isDone:Boolean?=null
                            )
