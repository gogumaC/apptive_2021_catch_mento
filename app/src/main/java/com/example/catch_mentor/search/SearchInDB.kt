package com.example.catch_mentor.search

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.catch_mentor.search.SearchFragment.search.keywordList
import com.example.catch_mentor.write.WriteInfoS
import com.google.firebase.firestore.FirebaseFirestore

open class SearchInDB(listener: searchListener, model: SearchViewModel) {
    var mCallback = listener
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언

    var model = model
    open fun searchText(col: String, searchText: String, isM : Boolean) {

        var GorU : String = "userGrade"
        var itemList : MutableList<WriteInfoS> = mutableListOf()

        if(col.equals("mentor_post")){
            GorU = "userGrade"
        }

        db.collection(col)
            .get()
            .addOnSuccessListener { result ->
                // 성공할 경우
                for (document in result) {  // 가져온 문서들은 result에 들어감
                    if ((document["title"] as String).contains(searchText) || (document["subTitle"] as String).contains(
                            searchText
                        )
                    ) {
                            val item = WriteInfoS(
                                document["title"] as String,
                                document["subTitle"] as String,
                                document["subject"] as String,
                                document[GorU] as String,
                                document["userName"] as String,
                                document.id,
                                document["userID"] as String,
                                false,
                                "3.5"
                            )
                            itemList.add(item)
                        model.itemList.setValue(itemList)

                    } else if (isM && (document["curri"] as String).contains(searchText) || (document["appeal"] as String).contains(
                            searchText
                        ) || (document["educontent"] as String).contains(searchText) || (document["userGrade"] as String).contains(
                            searchText
                        )
                    ) {
                        val item = WriteInfoS(
                            document["title"] as String,
                            document["subTitle"] as String,
                            document["subject"] as String,
                            document["userGrade"] as String,
                            document["userName"] as String,
                            document.id,
                            document["userID"] as String,
                            false,
                            "3.5"
                        )
                        itemList.add(item)
                        model.itemList.setValue(itemList)

                    } else if (!isM && (document["content"] as String).contains(searchText)) {
                        val item = WriteInfoS(
                            document["title"] as String,
                            document["subTitle"] as String,
                            document["subject"] as String,
                            document["userGrade"] as String,
                            document["userName"] as String,
                            document.id,
                            document["userID"] as String,
                            true,
                            "3.5"
                        )
                        itemList.add(item)
                        model.itemList.value = itemList

                    }
                }
                Log.w("SearchFragment", "${itemList}")

            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("SearchFragment", "Error getting documents: $exception")
            }

    }

    open fun searchKeyword(col: String) {

        var itemList: MutableList<WriteInfoS> = mutableListOf()

        if (col == "mentor_post") {
            db.collection(col)
                .get()
                .addOnSuccessListener { result ->
                    // 성공할 경우
                    for (document in result) {  // 가져온 문서들은 result에 들어감
                        Log.w("SearchFragment", "${document} ${keywordList}")
                        if (((document["online"] as Boolean).toString() == keywordList.online || keywordList.online == "")
                            && ((document["group"] as Boolean).toString() == keywordList.group || keywordList.group == "")
                            && ((document["sex"] as String) == keywordList.sex || keywordList.sex == "")
                            && (keywordList.subject.isEmpty() || keywordList.subject.contains(document["subject"] as String))){


                                val off = document["offline"] as MutableList<String>
                            if(off.isNotEmpty()) {
                                Log.d("서치db", "$off 사이즈 1 이상")
                                if (keywordList.region.isNotEmpty()) {
                                    for (i in off) {
                                        for (j in 0 until keywordList.region.size) {
                                            if (i.contains(keywordList.region[j])) {
                                                val userID = document["userID"] as String
                                                var isFav = false
                                                val item = WriteInfoS(
                                                    document["title"] as String,
                                                    document["subTitle"] as String,
                                                    document["subject"] as String,
                                                    document["userGrade"] as String,
                                                    document["userName"] as String,
                                                    document.id,
                                                    userID,
                                                    isFav,
                                                    "2.5"
                                                )
                                                itemList.add(item)
                                                Log.d("서치db", "추가완료")
                                                model.itemList.setValue(itemList)
                                                break
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("서치db", "$off 사이즈 0")
                                    val userID = document["userID"] as String
                                    var isFav = false
                                    val item = WriteInfoS(
                                        document["title"] as String,
                                        document["subTitle"] as String,
                                        document["subject"] as String,
                                        document["userGrade"] as String,
                                        document["userName"] as String,
                                        document.id,
                                        userID,
                                        isFav,
                                        "2.5"
                                    )
                                    itemList.add(item)
                                    Log.d("서치db", "추가완료")
                                    model.itemList.setValue(itemList)
                                }
                            }
                        }
                    }
                    Log.w("SearchFragment", "$itemList $keywordList")
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("SearchFragment", "Error getting documents: $exception")
                }
        } else {
            db.collection(col)
                .get()
                .addOnSuccessListener { result ->
                    // 성공할 경우
                    for (document in result) {  // 가져온 문서들은 result에 들어감
                        if (((document["online"] as Boolean).toString() == keywordList.online || keywordList.online == "")
                            && ((document["group"] as Boolean).toString() == keywordList.group || keywordList.group == "")
                            && ((document["sex"] as String).equals(keywordList.sex) || keywordList.sex.equals(""))
                            && (keywordList.subject.isEmpty() || keywordList.subject.contains(document["subject"] as String))){

                            val off = document["offline"] as MutableList<String>
                            if(off.size > 0){
                            if(keywordList.region.isNotEmpty()) {
                                for (i in off) {
                                    for (j in 0 until keywordList.region.size) {
                                        if (i.contains(keywordList.region[j])) {
                                            val userID = document["userID"] as String
                                            var isFav = false
                                            val item = WriteInfoS(
                                                document["title"] as String,
                                                document["subTitle"] as String,
                                                document["subject"] as String,
                                                document["userGrade"] as String,
                                                document["userName"] as String,
                                                document.id,
                                                document["userID"] as String,
                                                true,
                                                "3.5"
                                            )
                                            itemList.add(item)
                                            model.itemList.setValue(itemList)
                                            break
                                        }
                                    }
                                }
                            }else{
                                val item = WriteInfoS(
                                    document["title"] as String,
                                    document["subTitle"] as String,
                                    document["subject"] as String,
                                    document["userGrade"] as String,
                                    document["userName"] as String,
                                    document.id,
                                    document["userID"] as String,
                                    true,
                                    "3.5"
                                )
                                itemList.add(item)
                                model.itemList.setValue(itemList)
                            }
                            }
                        }
                    }
                    Log.w("SearchFragment", "야호")
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("SearchFragment", "Error getting documents: $exception")

                }
        }
    }
}
