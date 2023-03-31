package com.example.catch_mentor.baseClass

import android.widget.Toast
import androidx.fragment.app.DialogFragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject

open class BaseDialogFragment:DialogFragment() {

    val compostieDisposable= CompositeDisposable()
    val publishDialogDismissed=BehaviorSubject.create<Any>()

    fun makeShortToast(msg:String){
        Toast.makeText(requireContext(),msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!publishDialogDismissed.hasValue()) publishDialogDismissed.onNext(true)
        compostieDisposable.clear()
    }
}