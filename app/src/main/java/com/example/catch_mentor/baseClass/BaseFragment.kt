package com.example.catch_mentor.baseClass

import android.widget.Toast
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseFragment: Fragment() {

    val compositeDisposable=CompositeDisposable()

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.dispose()
    }

    fun makeShortToast(msg:String){
        Toast.makeText(requireContext(),msg,Toast.LENGTH_SHORT).show()
    }
}