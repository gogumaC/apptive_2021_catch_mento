package com.example.catch_mentor.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catch_mentor.write.WriteInfoS

class ChattingViewModel: ViewModel() {
    private val _chat = MutableLiveData<MutableList<WriteInfoS>>()
    val chat : LiveData<MutableList<WriteInfoS>>
        get() = _chat

    private var items = mutableListOf<WriteInfoS>()

    init{
        items = mutableListOf()
        _chat.value = items
    }
}