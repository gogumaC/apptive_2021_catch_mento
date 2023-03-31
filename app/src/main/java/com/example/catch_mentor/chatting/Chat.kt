package com.example.catch_mentor.chatting

import android.graphics.Bitmap
import com.google.firebase.Timestamp
import java.util.*


/*
 content: 채팅 내용 str
 time: 채팅 시간 Timestamp
 fromMe: 내가 보낸건지 bool
 ischecked: 상대방이 확인했는지 bool
 */

data class Chat (
    var fromMe: Boolean,
    var content: String,
    var time: String,
    var isChecked: Boolean,
    var name : String
    )

data class ChatList(
    var chat: String,
    var post: String,
    var isChecked: Boolean,
    var fromMe: Boolean,
    var userName: String,
    var userID: String,
    var time: String,
    var match: String
)