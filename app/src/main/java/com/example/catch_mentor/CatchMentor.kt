package com.example.catch_mentor

import android.app.Application

class CatchMentor:Application() {
    companion object {
        lateinit var prefs: CatchMentorSharedPreference
    }
    override fun onCreate() {
        prefs = CatchMentorSharedPreference(applicationContext)
        super.onCreate()
    }


}