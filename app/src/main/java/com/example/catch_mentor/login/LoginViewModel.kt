package com.example.catch_mentor.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.utils.UserDataManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel: BaseViewModel() {
    private lateinit var auth:FirebaseAuth
    val loginResult= MutableLiveData<String>()


    init{
        auth= Firebase.auth
        val currentUser=auth.currentUser

        if(currentUser!=null){
            //이거 나중에 런치 화면에서 체크해서 넘기기로
            Log.d("checfor","이미 로그인된 사용자 : ${currentUser.uid}")

        }
    }
    fun login(activity: Activity,email:String,password:String){
        Log.d("checkfor",email +"  " +password)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth.currentUser
                    Log.d("checkfor", "signInWithEmail:success  | user : ${user?.uid}")
                    loginResult.setValue(user?.uid?:"")
                    UserDataManager.loadUserData()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("checkfor", "signInWithEmail:failure", task.exception)
                    loginResult.setValue("")
                }
            }
    }
}