package com.example.catch_mentor.dataClass

import java.util.*

data class Task(val id:String?=null,
                val title:String?=null,
                val content:String?=null,
                val startDate: Date?=null,
                val finishDate:Date?=null,
                val menteeId:String?=null,
                val mentorId:String?=null,
                var isDone:Boolean=false
)
