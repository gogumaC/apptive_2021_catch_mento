package com.example.catch_mentor.write

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView

//TODO 들어가야하는 것 : 제목, 한줄내용, 설정과목, 올린 사람의 학교학과, 이름, 문서의 아이디, 유저프로필사진, 별점
data class WriteInfoS(
    val title:String,
    val subTitle:String,
    val subject:String,
    val userUniv:String,
    val userName:String,
    val docuID: String,
    val userID:String,
    var isFav:Boolean,
    val rating:String)