package com.example.catch_mentor

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class CatchMentorSharedPreference(context:Context) {
    private val prefsFilename = "prefs"
    private val prefsKeyEdt = "myEditText"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    fun getLastLoginDate(): Date {
        val data=prefs.getLong("lastLogin",0)
        val date=Date(data)
        return date
    }

    fun updateLastLoginDate(){
        prefs.edit().putLong("lastLogin",Date().time).apply()
    }
}