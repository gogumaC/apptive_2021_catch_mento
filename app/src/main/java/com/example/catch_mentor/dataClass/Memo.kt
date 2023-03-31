package com.example.catch_mentor.dataClass

import java.util.*

data class Memo(val id:String?=null,
                @field:JvmField
                val date: Date?=null,
                val memo:String?=null)
