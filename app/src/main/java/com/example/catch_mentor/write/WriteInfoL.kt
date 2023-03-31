package com.example.catch_mentor.write

//TODO 들어가야하는 것 : 제목, 한줄내용, 설정과목, 올린 사람의 학교학과, 이름, 유저아이디, 유저프로필사진, 커리큘럼, 어필, 수업내용
data class WriteInfoLMentor(
    val title:String,
    val subTitle:String,
    val subject:String,
    val userUniv:String,
    val userName:String,
    val userID:String,
    val educont:String,
    val curr:String,
    val appeal:String,
    val frequncy: Int,
    val online: Boolean,
    val group: Boolean,
    val offline: MutableList<String>)

data class WriteInfoLMentee(
    val title:String,
    val subTitle:String,
    val subject:String,
    val userUniv:String,
    val userName:String,
    val userID:String,
    val content:String,
    val frequncy: Int,
    val online: Boolean,
    val group: Boolean,
    val offline: MutableList<String>)

object postID{
    lateinit var documentID: String
    lateinit var documentName: String
    var writeMode = false
    var documentFav = false
}
