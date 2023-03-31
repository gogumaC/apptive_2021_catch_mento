package com.example.catch_mentor.signup

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.catch_mentor.signup.SignupFragmentMain.signup.email
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
data class Signup(
    var name: String,
    var school: String,
    var grade: String,
    var sex: String,
    var certificate: String,
    var career: String,
    var birth: String,
)

open class SignupDB(d : Signup){

    val data = d
    open fun singupAuth(){
        //TODO auth 등록
        val auth = FirebaseAuth.getInstance()
        //파이어베이스에 신규계정 등록하기
        auth.createUserWithEmailAndPassword(
            email,
            SignupFragmentMain.signup.pwd
        )
            .addOnCompleteListener {
                if(it.isSuccessful){
                    singupDB(auth.currentUser!!.uid)
                }else{
                }
            }.addOnFailureListener {
                Log.d("회원가입", it.toString())
            }
        //TODO db업로드
    }

    open fun singupDB(s: String){
        val db = Firebase.firestore

        if(SignupFragmentMain.signup.mUser == "mentor_user") {
            db.collection(SignupFragmentMain.signup.mUser).document(s).set(
                hashMapOf(
                    "name" to data.name,
                    "univ" to data.school,
                    "sex" to data.sex,
                    "major" to data.grade,
                    "certificate" to data.certificate,
                    "region" to "",
                    "email" to email,
                    "career" to data.career,
                    "favorite_post" to mutableListOf<String>(),
                    "star_rating" to 0,
                    "birth" to data.birth,
                    "mentees" to mutableListOf<String>(),
                    )
            )
        }else{
            db.collection(SignupFragmentMain.signup.mUser).add(
                hashMapOf(
                    "name" to data.name,
                    "school" to data.school,
                    "sex" to data.sex,
                    "grade" to data.grade,
                    "region" to "",
                    "email" to email,
                    "career" to data.career,
                    "favorite_post" to mutableListOf<String>(),
                    "birth" to data.birth,
                    "mentors" to mutableListOf<String>(),

                    )
            )
        }

    }
}