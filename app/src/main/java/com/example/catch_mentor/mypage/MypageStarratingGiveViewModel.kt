package com.example.catch_mentor.mypage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.dataClass.starRatingUnitData
import com.example.catch_mentor.interactors.MentorInteractor
import com.example.catch_mentor.interactors.MentoringInteractor

import com.example.catch_mentor.interactors.StarRatingsInteractor
import com.example.catch_mentor.utils.DateUtility.changeDate
import com.example.catch_mentor.utils.DateUtility.clearDate
import com.example.catch_mentor.utils.DateUtility.clearTime
import java.util.*

class MypageStarratingGiveViewModel(val mentoring: Mentoring): BaseViewModel() {

    val publishStarRatingList= MutableLiveData<List<starRatingUnitData>>()
    val publishMentorName=MutableLiveData<String>()
    val starRatingInteractor=StarRatingsInteractor(mentoring.mentorId.toString())

    init{
        //별점 정보 가져와서 뿌리링
        mentoring.mentor?.get()?.addOnSuccessListener {
            val mentor= MentorInteractor().parseData(it)
            publishMentorName.setValue(mentor.name.toString())
            getStarRating()
        }
        starRatingInteractor.publishData.subscribe {
            Log.d("checkcheck","온리스트 : $it")
            var monthCursor=mentoring.startDate?.clearDate()
            val dataList=mutableListOf<starRatingUnitData>()
            label@ while(monthCursor?.before(Date())?:false){
                for(i in it.indices){
                    if(it[i].time?.equals(monthCursor)?:false){
                        dataList.add(it[i])
                        monthCursor=monthCursor?.changeDate(Calendar.MONTH,1)
                        continue@label
                    }
                }
                dataList.add(starRatingUnitData(time=monthCursor,mentoringId = mentoring.id))
                monthCursor=monthCursor?.changeDate(Calendar.MONTH,1)
            }
            Log.d("checkforfor",dataList.toString())
            publishStarRatingList.setValue(dataList.reversed())

        }
        Log.d("checkCheck",mentoring.id.toString())
        val a=mentoring.id.toString()

    }

    fun getStarRating(){
        starRatingInteractor.getData(starRatingInteractor.ref.whereEqualTo("mentoringId",mentoring.id))
    }

    fun updateRating(newData: starRatingUnitData){
        Log.d("checkcheckf","!!!!!!!!!!!!!"+newData.toString())
        starRatingInteractor.updateData(newData.id.toString(),"starRating",newData.starRating)

    }
    fun createRating(newData: starRatingUnitData){
        starRatingInteractor.createData(newData)
    }
}