package com.example.catch_mentor.utils

import android.util.Log
import com.example.catch_mentor.dataClass.Mentee
import com.example.catch_mentor.dataClass.Mentor
import com.example.catch_mentor.interactors.MenteeInteractor
import com.example.catch_mentor.interactors.MentorInteractor
import com.example.catch_mentor.serverClass.UserClassificationInteractor
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

object UserDataManager {

    val MENTOR=0
    val MENTEE=1

    val user=Firebase.auth.currentUser

    private val testMenteeId="5iSymVnOY493lWoorr1F"
    private val testMentorId="Eg2ThvQdd1YgIBw2hD4yFiWp4CG3"

    val id:String?
        get()= user?.uid

    var userType=99
        private set
    var _mentorData=BehaviorSubject.create<Mentor>()
    var _menteeData=BehaviorSubject.create<Mentee>()

    var userData=BehaviorSubject.create<Any>()
    var mentorData:Mentor?=null
        private set
        get() = _mentorData.value

    var menteeData:Mentee?=null
        private set
        get() = _menteeData.value

    val userName:String
        get() {
            return when(userType){
                MENTOR->mentorData?.name.toString()
                MENTEE->menteeData?.name.toString()
                else ->""
            }
        }


    private val getTypeInteractor=UserClassificationInteractor()
    private val mentorInteractor=MentorInteractor()
    private val menteeInteractor=MenteeInteractor()
    private val compositeDisposable=CompositeDisposable()

    init{
        compositeDisposable.addAll(
            mentorInteractor.publishData.subscribe {
                Log.e("checkfor","멘토결과 : "+it.toString())
                userData.onNext(it.get(0))
                mentorData=it.get(0)

            },
            menteeInteractor.publishData.subscribe{
                Log.e("checkfor","멘티결과 : "+it.toString())
                userData.onNext(it.get(0))
                menteeData=it.get(0)
            }
        )

        id?.let{
            Log.d("checkfor", getTypeInteractor.userClass(it).toString())
            getTypeInteractor.userClass(it).subscribe {isMento->
                Log.d("checkfor", "$isMento")
                if(isMento){
                    userType=MENTOR
                    mentorInteractor.getDocumentData(mentorInteractor.ref.document(it))
                }else{
                    userType=MENTEE
                    menteeInteractor.getDocumentData(menteeInteractor.ref.document(it))
                }
            }

        }


    }

    fun loadUserData(){
        Log.d("checfor", id.toString())
        id?.let {
            Log.d("checkfor", getTypeInteractor.userClass(it).toString())
            getTypeInteractor.userClass(it).subscribe { isMento ->
                Log.d("checkfor", "$isMento")
                if (isMento) {
                    userType = MENTOR
                    mentorInteractor.getDocumentData(mentorInteractor.ref.document(it))
                } else {
                    userType = MENTEE
                    menteeInteractor.getDocumentData(menteeInteractor.ref.document(it))
                }
            }
        }
    }


    fun isMentor():Boolean=(userType== MENTOR)

    fun getUserData():Any= userData.value
}