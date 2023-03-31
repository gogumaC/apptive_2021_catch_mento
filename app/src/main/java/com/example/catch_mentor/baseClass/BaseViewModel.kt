package com.example.catch_mentor.baseClass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catch_mentor.ect.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel: ViewModel() {

    val compositeDisposable=CompositeDisposable()


    val _publishViewChange=SingleLiveEvent<Any>()
    val publishViewChange:MutableLiveData<Any>  get() = _publishViewChange
}