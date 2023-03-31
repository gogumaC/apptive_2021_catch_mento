package com.example.catch_mentor.mentoringRecord

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.Task
import com.example.catch_mentor.interactors.TaskInteractor
import com.example.catch_mentor.interactors.TaskInteractor2
import com.example.catch_mentor.utils.DateUtility.clearTime
import com.example.catch_mentor.utils.UserDataManager
import java.util.*

class AddTaskViewModel(val mentoring: Mentoring):BaseViewModel() {

    private val taskInteractor= TaskInteractor()
    private val taskInteractor2=TaskInteractor2(mentoring.id!!)
    var startDate=MutableLiveData(Date())
    var finishDate=MutableLiveData(Date())
    private var menteeId:String?=null

    fun setStartDate(date:Date){startDate.setValue(date)}
    fun setFinishDate(date:Date){finishDate.setValue(date)}
    fun setMenteeId(id:String?){menteeId=id}

    init{

        compositeDisposable.addAll(
            taskInteractor.publishIsSuccess.subscribe {
                if(it){
                    _publishViewChange.setValue(it)
                }
            },
            taskInteractor2.publishCreateSuccess.subscribe {
                _publishViewChange.setValue(it)
            }
        )
    }

    fun complete(content:String){

Log.d("checkfor","!!!!@@"+startDate.value.toString())
        if(UserDataManager.isMentor()){

            val task= Task(
                title="",
                content=content,
                startDate = startDate.value?.clearTime(),
                finishDate = finishDate.value?.clearTime(),
                menteeId = menteeId,
                mentorId = UserDataManager.id
            )

            //taskInteractor.createData(task)
            taskInteractor2.createData(task)
        }

    }
}