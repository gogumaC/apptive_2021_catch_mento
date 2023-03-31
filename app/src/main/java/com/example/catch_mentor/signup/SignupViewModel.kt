package com.example.catch_mentor.signup

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel: ViewModel() {

    val signNum: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val emailStr: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val pwdStr: MutableLiveData<String> by lazy { MutableLiveData<String>() }

}