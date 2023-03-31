package com.example.catch_mentor.UserTypeTest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel

class UserTypeTestViewModel:BaseViewModel() {


    val questionList= MutableLiveData<List<Question>>()
    val testResult=MutableLiveData<String>()
    val test=List(20, { Question("질문스", "답1", "답2") })
    val ansList=MutableList<Int>(24,{0})


    init{
        questionList.setValue(test)
    }
    fun setQuestionResult(pos:Int,ans:Int){
        ansList[pos]=ans
    }
    fun send(){
    Log.d("ansResult",ansList.toString())
        //서버에 ansList보내기
        testResult.setValue("이거로 결과 보냄")
    }
}