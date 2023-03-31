package com.example.catch_mentor.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.dataClass.LearningRecord
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.interactors.*
import com.example.catch_mentor.utils.DateUtility.clearTime
import com.example.catch_mentor.utils.DateUtility.getDayOfWeek
import com.example.catch_mentor.utils.DateUtility.getFirstDateOfMonth
import com.example.catch_mentor.utils.DateUtility.getLastDateOfMonth
import com.example.catch_mentor.utils.UserDataManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function
import java.util.*

class HomeViewModel:BaseViewModel() {

    val mMentor=MutableLiveData<Mentor>()
    val mMentee=MutableLiveData<Mentee>()
    val records=MutableLiveData<List<LearningRecord>>()

  //  private val myDataInteractor=MentorInteractor()
    private val getRecordsInteractor=LearningRecrodInteractor()



    private val mentoringInteractor=MentoringInteractor()
    private val mentorings= mutableListOf<Mentoring>()
    private val recordInteractors=mutableListOf<LearningRecordInteractor2>()





    init{

      //  myDataInteractor.getData(myDataInteractor.ref.whereEqualTo("major","물리학과"))
        compositeDisposable.addAll(

            mentoringInteractor.publishData.subscribe{
                mentorings.addAll(it)
                val recordInteractors=it.map{LearningRecordInteractor2(it,true)}
                Log.d("records",it.toString())
                val publishRecordResult=recordInteractors.map{it.publishData}
                recordInteractors.forEach{
                    //===========
                    compositeDisposable.add(
                        Observable.zip(publishRecordResult) { recordLists ->
                            Log.d("records","record Lists : "+recordLists.toList().toString())

                            val records = mutableListOf<LearningRecord>()
                                recordLists.forEach {
                                   records.addAll((it as List<*>).map { it as LearningRecord })
                               }

                            records
                        }
                            .subscribe {results->
                                //TODO 내일 멘토링이랑 학습기록이랑 같이 보내는 방법올리고 멘티버전체크하고 캘린더 과제 부분 체크하면 끝날듯
                                records.setValue(results)
                                Log.d("aaaaaa",results.toString())
                            }
                    )
                    //===================
                }
                this.recordInteractors.addAll(recordInteractors)
            },
            getRecordsInteractor.publishData.subscribe {
                records.setValue(it)

                Log.d("checkfor","process : "+it.toString())
            }

        )
        getLearningRecords(Date().clearTime())
    }



    fun getMentoring(){
        val userData=UserDataManager.getUserData()
        if(userData is Mentor) mentoringInteractor.getData(mentoringInteractor.ref.whereEqualTo("mentor_id",userData.id))
        else if(userData is Mentee) mentoringInteractor.getData(mentoringInteractor.ref.whereEqualTo("mentee_id",userData.id))
    }

    fun getLearningRecords(date: Date){
        recordInteractors.forEach{
            it.getData(it.ref.whereEqualTo("date",date),date)
        }
//        if(UserDataManager.isMentor()){
//
//            getRecordsInteractor.getData(getRecordsInteractor.ref
//                .whereEqualTo("date",date)
//                .whereEqualTo("mentor_id",UserDataManager.id)
//            )
//        }else{
//            getRecordsInteractor.getData(getRecordsInteractor.ref
//                .whereEqualTo("date",date)
//                .whereEqualTo("mentee_id",UserDataManager.id)
//            )
//
//        }

    }

    fun getProcess(mentoring:Mentoring , date:Date){

    }


}

