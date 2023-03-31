package com.example.catch_mentor.baseClass

import android.util.Log
import com.example.catch_mentor.dataClass.Err
import com.google.firebase.firestore.*
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseInteractor<RES_T> {



    abstract val collectionPath:String
    val publishData=BehaviorSubject.create<List<RES_T>>()
    val publishErr=BehaviorSubject.create<Err>()
    val publishIsSuccess=BehaviorSubject.create<Boolean>()
    val publishCreateSuccess=BehaviorSubject.create<RES_T>()
    val publishUpdateSuccess=BehaviorSubject.create<Boolean>()
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    open val ref=db.collection(collectionPath)



    open fun getData(query:Query=ref){
        val list= mutableListOf<RES_T>()
        query // 작업할 컬렉션
            .get()// 문서 가져오기
            .addOnSuccessListener { result ->
                // 성공할 경우
                Log.d("checkfor","ref : "+ref.path )
                for (document in result) {
                    Log.d("checkfor",document.toString())
                    val item=parseData(document)
                    list.add(item)

                }
                onRequestSuccessed(list)

            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                onRequestFailed(exception)
                Log.e("checkfor","failure : "+exception.toString())

            }
    }

    open fun getDocumentData(query:DocumentReference){
        val list= mutableListOf<RES_T>()
        query // 작업할 컬렉션
            .get()// 문서 가져오기
            .addOnSuccessListener { document ->
                // 성공할 경우
                if(document!=null){
                    val item=parseData(document)
                    list.add(item)
                    onRequestSuccessed(list)
                }else{

                }



            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                onRequestFailed(exception)

            }
    }



    abstract fun parseData(document: DocumentSnapshot):RES_T
//    fun parseData(document: DocumentSnapshot){
//        parseData(QueryDocumentSnapshot)
//    }

    open fun onRequestSuccessed(list:MutableList<RES_T>){
        Log.d("checkfor",list.toString())
        publishData.onNext(list)
    }

    open fun onRequestFailed(e:Exception){
        Log.w("MainActivity", "Error getting documents: $e")
    }

    open fun createData(data:RES_T){
        ref.document()
            .set(data!!)
            .addOnSuccessListener {
                Log.d("baseInteractor","$this : createData : $data || ${ref.path}")

                publishCreateSuccess.onNext(data)
            }
            .addOnFailureListener {  }

    }

    open fun createDataWithId(id:String,data:RES_T){
        ref.document(id)
            .set(data!!)
            .addOnSuccessListener {
                Log.d("baseInteractor","$this : createData : $data  || ${ref.path}")

                publishCreateSuccess.onNext(data)
            }
            .addOnFailureListener {  }

    }

    open fun updateData(documentId:String,field:String,newData:Any?){
        ref.document(documentId)
            .update(field,newData)
            .addOnSuccessListener {

                publishUpdateSuccess.onNext(true)
            }
            .addOnFailureListener {  }
    }

    open fun deleteDocument(documentId: String){
        ref.document(documentId).delete()
    }


    open fun getDocumentRef(documentId: String?):DocumentReference?{
        return if(documentId==null) null else ref.document(documentId)
    }

}