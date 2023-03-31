package com.example.catch_mentor.mentoringRecord

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.dataClass.LearningRecord
import com.example.catch_mentor.dataClass.Memo
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.Task
import com.example.catch_mentor.interactors.CalendarMemoInteractor
import com.example.catch_mentor.interactors.LearningRecordInteractor2
import com.example.catch_mentor.interactors.MentoringInteractor
import com.example.catch_mentor.interactors.TaskInteractor2
import com.example.catch_mentor.utils.DateUtility.changeDate
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.ktx.Firebase
import java.util.*

class SetMentoringRecordViewModel(val mentoring:Mentoring,val learningRecord: LearningRecord?,val date: Date):BaseViewModel() {

    private val learningRecordInteractor=LearningRecordInteractor2(mentoring)
    private val taskInteractor=TaskInteractor2(mentoring.id!!)
    private val memoInteractor=CalendarMemoInteractor(mentoring.id!!)

    val publishLearningRecord= MutableLiveData<LearningRecord>()
    val memo=MutableLiveData<Memo>()
    val tasks=MutableLiveData<List<Task>>()


    init{
        compositeDisposable.addAll(
            learningRecordInteractor.publishData.subscribe {
                if(!it.isNullOrEmpty()){
                    publishLearningRecord.setValue(it[0])
                }
            },
            taskInteractor.publishData.subscribe {
                Log.d("tasktask","dialog tasks : \n"+it.toString())
                tasks.setValue(it)
            },
            memoInteractor.publishData.subscribe {
                if(!it.isNullOrEmpty()) memo.setValue(it[0])
            }
        )
    }

    fun getDatas(){
        getLearningRecord()
        getTasks()
        getMemo()
    }
    fun changeCompleteState(state:Boolean){
        Log.d("checkfor",learningRecord?.id.toString()+state.toString())
        if(!state&&learningRecord?.id!=null)learningRecordInteractor.deleteDocument(learningRecord.id)
        else if(state){
            if(learningRecord?.id!=null) {
                learningRecordInteractor.updateData(learningRecord.id.toString(), "isDone", state)
            }
            else learningRecordInteractor.createData(LearningRecord(date=date,isDone =true,mentoring=MentoringInteractor().getDocumentRef(mentoring.id)))
        }

    }

    fun changeTaskCompleteState(task:Task){
        taskInteractor.updateData(task.id.toString(),"isDone",task.isDone)
    }

    fun saveState(memo:String){
            if (this.memo.value != null) memoInteractor.updateData(
                this.memo.value?.id.toString(),
                "memo",
                memo
            )
            else if(memo.isNotBlank()) memoInteractor.createData(Memo(date = date, memo = memo))
        }



    private fun getTasks(){
        taskInteractor.getData(taskInteractor.ref.whereGreaterThanOrEqualTo("startDate",date)
            .whereLessThan("startDate",date.changeDate(num=1)))
    }
    private fun getMemo(){
        memoInteractor.getData(memoInteractor.ref.whereEqualTo("date",date))
    }
    private fun getLearningRecord(){
        learningRecordInteractor.getDocumentData(learningRecordInteractor.ref.document(learningRecord?.id.toString()))

    }

}