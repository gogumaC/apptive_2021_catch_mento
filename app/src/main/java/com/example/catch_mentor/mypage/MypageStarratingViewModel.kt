package com.example.catch_mentor.mypage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.interactors.MentoringInteractor
import com.example.catch_mentor.utils.UserDataManager
import com.google.firebase.firestore.Query

class MypageStarratingViewModel: BaseViewModel() {

    val publishMentoring= MutableLiveData<List<Mentoring>>()



    private val mentoringInteractor= MentoringInteractor()

    init{
        getMentoringData()
        compositeDisposable.addAll(
            mentoringInteractor.publishData.subscribe {
                Log.d("mentoringData",it.toString())
                publishMentoring.setValue(it)
            }

        )

    }
    fun getMentoringData(){
        val query: Query
        if(UserDataManager.isMentor()){
            query=mentoringInteractor.ref
                .whereEqualTo("mentor_id", UserDataManager.id)
        }else{
            query=mentoringInteractor.ref
                .whereEqualTo("mentee_id", UserDataManager.id)
        }

        mentoringInteractor.getData(query)
    }
}