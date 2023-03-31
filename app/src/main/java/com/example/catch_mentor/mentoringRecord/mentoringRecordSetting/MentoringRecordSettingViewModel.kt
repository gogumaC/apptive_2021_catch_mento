package com.example.catch_mentor.mentoringRecord.mentoringRecordSetting

import android.nfc.Tag
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.dataClass.MentoringSchedule
import com.example.catch_mentor.interactors.MentoringInteractor
import com.example.catch_mentor.utils.DateUtility
import java.time.DayOfWeek
import java.util.*

class MentoringRecordSettingViewModel(private val mentoringId:String):BaseViewModel() {

    val mentoringSchedule=MutableLiveData<MutableList<MentoringSchedule>>()
    private val interactor=MentoringInteractor()
    val title=MutableLiveData<String>()
    val subject=MutableLiveData<String>()
    val startDate=MutableLiveData<Date>()

init{
    compositeDisposable.addAll(
        interactor.publishData.subscribe{
            if(it.isNotEmpty()){
                it.get(0).let{mentoring->
                    title.setValue(mentoring.title.toString())
                    subject.setValue(mentoring.subject.toString())
                    mentoring.startDate?.let{
                        startDate.setValue(it)
                    }

//                    val days:MutableList<DayOfWeek?> = mentoring.schedule?.map{it.dayOfWeek}?.toMutableList()?: mutableListOf()
//                    days.remove(null)
//                    classDays.setValue(days.map{it!!}.toMutableList())
                   // classDays.setValue(mentoring.schedule?.toMutableList()?: mutableListOf())
                   restoreSchedule(mentoring.schedule?.toMutableList()?: mutableListOf())
                }

            }

        }
    )


    interactor.getDocumentData(interactor.ref.document(mentoringId))

}

    fun createNewSchedule(dow:DayOfWeek){
        val tempList=mentoringSchedule.value?: mutableListOf()
        val schedule=MentoringSchedule(dow)
        tempList.add(schedule)
        mentoringSchedule.setValue(tempList)
    }
    private fun restoreSchedule(data:MutableList<MentoringSchedule>){
        try{
            Log.d("checkfor",data.toString())
            if(data.isNotEmpty()) mentoringSchedule.setValue(data)
        }catch (e:Exception){
            Log.e("err","wrong dayofweek")
        }

    }
    fun deleteSchedule(schedule:MentoringSchedule){
        val list=mentoringSchedule.value?: mutableListOf()
        list.remove(schedule)
        mentoringSchedule.setValue(list)

    }

    fun setStartDate(startDate: Date){
            this.startDate.setValue(startDate)

    }
    fun setSubject(subject: String){
        this.subject.setValue(subject)

    }

//
//    fun deleteClassDay(dayOfWeek: DayOfWeek){
//        val list=classDays.getValue()
//        list?.let{
//            list.remove(dayOfWeek)
//            classDays.setValue(it)
//        }
//
//    }
//    fun addClassDay(dayOfWeek: DayOfWeek){
//        val lisct=classDays.getValue()?: mutableListOf()
//        list.add(dayOfWeek)
//        classDays.setValue(list)
//
//    }
//
//    fun checkClassDayAdded(dayOfWeek:DayOfWeek):Boolean{
//       return classDays.getValue()?.contains(dayOfWeek)?:false
//    }

    fun complete(title:String){
        Log.d("checkcheck",startDate.getValue().toString())
        interactor.updateData(mentoringId,"title",title)
        interactor.updateData(mentoringId,"schedule",mentoringSchedule.value)
        interactor.updateData(mentoringId,"startDate",startDate.getValue())
        interactor.updateData(mentoringId,"subject",subject.getValue())


    }

}
