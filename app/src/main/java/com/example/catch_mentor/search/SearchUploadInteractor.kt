package com.example.catch_mentor.search

import android.content.ContentValues.TAG
import android.util.Log
import androidx.databinding.ObservableArrayList
import com.example.catch_mentor.serverClass.UserClassificationInteractor
import com.example.catch_mentor.write.WriteInfoS
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.core.Single


open class SearchUploadInteractor(listener: searchListener, m: SearchViewModel) {

    var mCallback = listener
    var model = m

    open fun SearchUpload(col: String, u: String){

        var favPost: MutableList<String> = mutableListOf()
        var starRating: String = ""
        val db = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser
        var itemList : MutableList<WriteInfoS> = mutableListOf()


            db.collection(u).document(user!!.uid).get().addOnSuccessListener { snapshot ->

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: ${snapshot.data}")
                    favPost = snapshot["favorite_post"] as MutableList<String>
                    if(u == "mentor_user") {
                        starRating = (snapshot["star_rating"] as Long).toString() //TODO 별점 관련 수정하기
                    }
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
         db.collection(col).get().addOnSuccessListener { snapshot ->

                for (document in snapshot!!) {  // 가져온 문서들은 result에 들어감
                    val userID = document["userID"] as String
                    var isFav = false
                    if (favPost.contains(document.id)) {
                        isFav = true
                    }
                    val item = WriteInfoS(
                        document["title"] as String,
                        document["subTitle"] as String,
                        document["subject"] as String,
                        document["userGrade"] as String,
                        document["userName"] as String,
                        document.id,
                        userID,
                        isFav,
                        starRating //TODO 마이페이지 작업 후 반드시 수정하기
                    )
                    itemList.add(item)
                    model.itemList.setValue(itemList)
                }
                Log.d("SUI 테스트","$itemList")
            }

    }



}