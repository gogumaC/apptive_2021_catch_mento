package com.example.catch_mentor.mentoringRecord

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.dataClass.LearningRecord
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.Progress
import com.example.catch_mentor.dataClass.Task
import com.example.catch_mentor.interactors.*
import com.example.catch_mentor.utils.DateUtility
import com.example.catch_mentor.utils.DateUtility.changeDate
import com.example.catch_mentor.utils.UserDataManager
import java.util.*

class CalendarRecordViewModel(mentoring:Mentoring):BaseViewModel() {
    //달별로 가져옴,
    //가져올것은 과제, 학습기록
    //사용 데이터는 해당 월 내의 날짜, 멘티 아이디, 멘토아이디
    //해당 월 내의 멘토,멘티가 같은 데이터 가져와야함
    //디비 상에 멘토멘티는 레퍼로 들어가 있으므로 path에 멘토,멘티의 id가 있는지 확인이 필요

    private var mentoring: Mentoring?=null
    private val learningRecrodInteractor=LearningRecordInteractor2(mentoring,true)
    private val taskInteractor:TaskInteractor2=TaskInteractor2(mentoring.id!!)
    private val progressInteractor=ProgressInteractor(mentoring.id.toString())

    val publishLearningRecords= MutableLiveData<List<LearningRecord>>()
    val publishTasks=MutableLiveData<List<Task>>()
    private var dateCursor:Date?=null
    private var mentee_id:String?=null
    get() {
        return if(field.isNullOrBlank())UserDataManager.id else field
    }
    private var mentor_id:String?=null
        get() {
            return if(field.isNullOrBlank())UserDataManager.id else field
        }
    init{

        compositeDisposable.addAll(
            learningRecrodInteractor.publishData.subscribe {
                Log.d("learning",it.toString())
                publishLearningRecords.setValue(it)
                getTask()
            },
            taskInteractor.publishData.subscribe {
                Log.d("tasktask","calendar task : \n"+it.toString())
                publishTasks.setValue(it)
            }

        )


    }



    fun getLearningRecord(startDate: Date){
        dateCursor=startDate
       val finishDate=startDate.changeDate(Calendar.MONTH,1)
       val query=learningRecrodInteractor.ref
           .whereGreaterThanOrEqualTo("date",startDate)
           .whereLessThan("date",finishDate)

        learningRecrodInteractor.getData(query)

    }
    private fun getTask(){
        val startDate=dateCursor?:Date()
        val finishDate=startDate.changeDate(Calendar.MONTH,1)
        val query=taskInteractor.ref
            .whereGreaterThanOrEqualTo("startDate",startDate)
            .whereLessThan("startDate",finishDate)
            //.whereGreaterThanOrEqualTo("start_date",startDate)
            //.whereLessThan("finish_date",finishDate)

        taskInteractor.getData(query)
    }
    fun setMenteeId(id:String?){
        mentee_id=id
    }
    fun setMentorId(id:String?){
        mentor_id=id
    }

    fun setProgress(progress:Progress,date: Date){

        progressInteractor.createDataWithId(DateUtility.formatDate(date,"yyyyMM"),progress)
    }
//    fun setMentoring(mentoring:Mentoring){
//        this.mentoring=mentoring
//        learningRecrodInteractor=LearningRecordInteractor2(mentoring.id!!)
//        taskInteractor= TaskInteractor2(mentoring.id)
//
//    }




}