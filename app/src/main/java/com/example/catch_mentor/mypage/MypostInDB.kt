package com.example.catch_mentor.mypage

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.catch_mentor.write.WriteInfoS
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class MypostInDB(listener: mypageMypostListener) {
    var mCallback = listener
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언

    //TODO isFav는 여기서 보관된건지, 쓴 건지 의미함(내가 쓴 게시글 -> true 보관한 게시글 -> false)


    //TODO
    // 채팅 전송 오류 , 과목확인버튼(디자인완료) / 카테고리 키워드 리스트 / 멘티 글쓰기 본인 체크

    open fun MypostUpload(col: String, i: Boolean, itemList: MutableList<WriteInfoS>){ //u는 유저아이디, col은 멘토/멘티, i는 작성/보관

        var starRating: String = ""
        val db = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser
        var userid = user!!.uid
        Log.d("마이페이지", "$userid 어댑터2")

        if(!i) {
            db.collection(col).whereEqualTo("userID", userid).orderBy("time", Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if (document.exists()) {
                        val userID = document["userID"] as String
                        val item = WriteInfoS(
                            document["title"] as String,
                            document["subTitle"] as String,
                            document["subject"] as String,
                            document["userGrade"] as String,
                            document["userName"] as String,
                            document.id,
                            userID,
                            true,
                            starRating
                        )
                        itemList.add(item)
                    }
                    Log.d("마이페이지", "${itemList} $userid 어댑터3")
                    mCallback.postLoad(itemList)
                }
            }.addOnFailureListener {
                Log.d("마페", it.toString())
            }
        }else { //보관한 게시글
            if (col == "mentor_post") {
                db.collection("mentor_user").document(userid).collection("temporary_post")
                    .get() //멘토일 경우
                    .addOnSuccessListener { result ->
                        for (document in result) {  // 가져온 문서들은 result에 들어감
                            if (document.exists()) {
                                val userID = document["userID"] as String
                                val item = WriteInfoS(
                                    document["title"] as String,
                                    document["subTitle"] as String,
                                    document["subject"] as String,
                                    document["userGrade"] as String,
                                    document["userName"] as String,
                                    document.id,
                                    userID,
                                    true,
                                    starRating
                                )
                                itemList.add(item)
                            }
                        }
                        Log.d("마이페이지", "${itemList} 어댑터")
                        mCallback.postLoad(itemList)
                    }
            }else{
                db.collection("mentee_user").document(userid).collection("temporary_post")
                    .get() //멘토일 경우
                    .addOnSuccessListener { result ->
                        for (document in result) {  // 가져온 문서들은 result에 들어감
                            if (document.exists()) {
                                val userID = document["userID"] as String
                                val item = WriteInfoS(
                                    document["title"] as String,
                                    document["subTitle"] as String,
                                    document["subject"] as String,
                                    document["userGrade"] as String,
                                    document["userName"] as String,
                                    document.id,
                                    userID,
                                    true,
                                    starRating
                                )
                                itemList.add(item)
                            }
                        }
                        Log.d("마이페이지", "${itemList} 어댑터")
                        mCallback.postLoad(itemList)

                    }
        }
        }

    }}
