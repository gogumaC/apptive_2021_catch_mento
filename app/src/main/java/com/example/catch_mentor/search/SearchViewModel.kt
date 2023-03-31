package com.example.catch_mentor.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.write.WriteInfoS

class SearchViewModel: BaseViewModel() {

    val keywordList: MutableLiveData<SearchInfo> by lazy { MutableLiveData<SearchInfo>() }
    val itemList: MutableLiveData<MutableList<WriteInfoS>> by lazy { MutableLiveData<MutableList<WriteInfoS>>() }

}