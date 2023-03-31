package com.example.catch_mentor.launch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.utils.UserDataManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LaunchViewModel:BaseViewModel() {
    private lateinit var auth: FirebaseAuth
    val loginResult= MutableLiveData<Boolean>()
    init{
        auth= Firebase.auth
        val currentUser=auth.currentUser

        if(currentUser!=null){
            //이거 나중에 런치 화면에서 체크해서 넘기기로
            Log.d("checfor","이미 로그인된 사용자 : ${currentUser.uid}")
            UserDataManager.loadUserData()
            loginResult.setValue(true)

        }
    }
}