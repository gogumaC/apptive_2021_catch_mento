package com.example.catch_mentor.chatting

import androidx.lifecycle.MutableLiveData
import com.example.catch_mentor.baseClass.BaseViewModel
import com.example.catch_mentor.write.WriteInfoS

class ChattingListViewModel: BaseViewModel() {

    val itemList: MutableLiveData<MutableList<ChatList>> by lazy { MutableLiveData<MutableList<ChatList>>() }
    val userList: MutableLiveData<MutableList<String>> by lazy { MutableLiveData<MutableList<String>>() }

}