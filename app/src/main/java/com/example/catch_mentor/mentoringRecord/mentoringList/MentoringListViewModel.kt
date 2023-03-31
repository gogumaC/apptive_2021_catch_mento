package com.example.catch_mentor.mentoringRecord.mentoringList

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.interactors.MenteeInteractor
import com.example.catch_mentor.interactors.MentorInteractor
import com.example.catch_mentor.interactors.MentoringInteractor
import com.example.catch_mentor.utils.UserDataManager
import com.google.firebase.firestore.Query

class MentoringListViewModel: BaseViewModel() {

    private val mentee= mutableListOf<Mentee>()
    private val mentors= mutableListOf<Mentor>()
    val publishMentoring=MutableLiveData<List<Mentoring>>()
    val publishMentees=MutableLiveData<List<Mentee>>()
    val publishMentors=MutableLiveData<List<Mentor>>()
    val menteeInteractor=MenteeInteractor()
    val mentorInteractor=MentorInteractor()


    private val mentoringInteractor= MentoringInteractor()

    init{
        getMentoringData()
        compositeDisposable.addAll(
//            UserDataManager.userData.subscribe {
//                when(it){
//                    is Mentor ->{
//                        val menteeDocumentReferences=it.mentees
//                        menteeDocumentReferences?.forEachIndexed { index, documentReference ->
//                            documentReference.get()
//                                .addOnSuccessListener {
//                                    mentee.add(menteeInteractor.parseData(it))
//                                    if(index==menteeDocumentReferences.size-1){ publishMentees.setValue(mentee)}
//
//                                }
//                        }
//                    }
//                    is Mentee->{
//                        val mentorDocumentReferences=it.mentors
//                        mentorDocumentReferences?.forEachIndexed { index, documentReference ->
//                            documentReference.get()
//                                .addOnSuccessListener {
//                                    Log.d("checkfor","멘티유저의 멘토 ${it.id}")
//                                    mentors.add(mentorInteractor.parseData(it))
//                                    if(index==mentorDocumentReferences.size-1){ publishMentors.setValue(mentors)}
//
//                                }
//                        }
//                    }
//                }
//            },
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
                .whereEqualTo("mentor_id",UserDataManager.id)
        }else{
            query=mentoringInteractor.ref
                .whereEqualTo("mentee_id",UserDataManager.id)
        }

        mentoringInteractor.getData(query)
    }
}