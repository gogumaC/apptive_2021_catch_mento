package com.example.catch_mentor.chatting

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import androidx.lifecycle.Observer
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentChattingListBinding
import com.example.catch_mentor.search.SearchFragment
import com.example.catch_mentor.search.SearchViewModel
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.write.WriteInfoS
import com.example.catch_mentor.write.postID.documentName
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class ChattingListFragment: BaseFragment() {

    private lateinit var binding: FragmentChattingListBinding
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    var userList: MutableList<String> = mutableListOf()
    var data: MutableList<ChatList> = mutableListOf()
    val user = Firebase.auth.currentUser
    private lateinit var chattingListAdapter : ChattingListAdapter
    private val model: ChattingListViewModel by activityViewModels()


    //채팅 인포
    var time: String = ""
    var post: String = ""
    var isChecked: Boolean = false
    var fromMe: Boolean = false
    var chat: String = ""
    var userName = ""
    var userID = ""
    var match = ""



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingListBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chattingListAdapter = ChattingListAdapter(requireContext())

        binding.chattingListItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.chattingListItems.adapter = chattingListAdapter

        var ID = UserDataManager.id!!

        val chatListObserver = Observer<MutableList<ChatList>> {
            data = it
            chattingListAdapter.setData(it)
        }

        model.itemList.observe(viewLifecycleOwner, chatListObserver)

        binding.chattingListSpinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.matchingList,
            R.layout.item_spinner)

        binding.chattingListSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    // 모든 매칭
                    0 -> {
                        data.clear()
                        model.itemList.value = data
                        chatUpload(ID)
                    }
                    // 매칭 중
                    1 -> {
                        data.clear()
                        model.itemList.value = data
                        chatUploadMatch(ID, "매칭 중")
                    }
                    // 매칭 완료
                    2 -> {
                        data.clear()
                        model.itemList.value = data
                        chatUploadMatch(ID, "매칭 완료")
                    }
                    // 매칭 실패
                    3 ->{
                        data.clear()
                        model.itemList.value = data
                        chatUploadMatch(ID, "매칭 실패")
                    }
                    //일치하는게 없는 경우
                    else -> {
                    }
                }
            }
        }


        chattingListAdapter.setOnItemClickListener(object : ChattingListAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                ChattingFragment.chatting.userID = data[pos].userID
                documentName = post
                Log.d("chatList", "$ID / ${data[pos].userID}")
                //TODO nav
                view.findNavController().navigate(R.id.action_chattingListFragment_to_chattingFragment)
            }
        }
        )



    }

    private fun chatUpload(userID: String){
        db.collection("chatting").document(userID).addSnapshotListener {document, e ->
                userList = document?.get("User") as MutableList<String>
                for(i in userList) {
                    var search = "mentor_user"
                    if (UserDataManager.isMentor()) {
                        search = "mentee_user"
                    }
                    var ui = i
                    db.collection(search).document(i).get().addOnSuccessListener { user ->
                        userName = user["name"] as String
                    }
                    db.collection("chatting").document(userID).collection(i)
                        .orderBy("time", Query.Direction.DESCENDING).limit(2).addSnapshotListener{ cc, e ->
                            data.clear()
                            for (c in cc!!.documents) {
                                if (c.id == "info") {
                                    post = c["postName"] as String
                                    match = c["matching"] as String
                                } else {

                                    val tt = c["time"] as Timestamp
                                    val sf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA)
                                    sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                                    time = sf.format(tt.toDate())


                                    chat = c["content"] as String
                                    if(chat.length > 20){
                                        chat = chat.substring(0,19) + "..."
                                    }
                                    isChecked = c["isChecked"] as Boolean
                                    fromMe = c["fromMe"] as Boolean
                                }
                                Log.d("chatList", "$data $ui")
                            }


                            data.add(
                                ChatList(
                                    chat,
                                    post,
                                    isChecked,
                                    fromMe,
                                    userName,
                                    ui,
                                    time,
                                    match
                                )
                            )
                            model.itemList.value = data
                            Log.d("chatList", "$data $ui")
                        }
                }
            }
    }
    private fun chatUploadMatch(userID: String, matching: String){
        Log.d("chatList", "업로드")
        var ui = ""




        db.collection("chatting").document(userID).addSnapshotListener {document, e ->
            userList = document?.get("User") as MutableList<String>
            val userListB = mutableListOf<String>()
            Log.d("chatList", "야호 $userListB")
            for(i in userList) {
                var search = "mentor_user"
                if (UserDataManager.isMentor()) {
                    search = "mentee_user"
                }
                ui = i
                db.collection(search).document(i).get().addOnSuccessListener { user ->
                    userName = user["name"] as String
                }

                db.collection("chatting").document(userID).collection(i).document("info")
                    .addSnapshotListener { value, error ->
                        if (value != null) {
                            if ((value["matching"] as String) == matching) {
                                userListB.add(i)
                                Log.d("chatList", "매치 ${userListB}")
                            }
                        }
                        if(i == userList[userList.size - 1] && userListB.size > 0){
                            for(i in userListB) {
                                db.collection("chatting").document(userID).collection(i)
                                    .orderBy("time", Query.Direction.DESCENDING).limit(2).addSnapshotListener{ cc, e ->
                                        for (c in cc!!.documents) {
                                            if (c.id == "info") {
                                                post = c["postName"] as String
                                                match = c["matching"] as String
                                            } else {
                                                val tt = c["time"] as Timestamp
                                                val sf = SimpleDateFormat("yyyy. MM. dd HH:mm a", Locale.KOREA)
                                                sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                                                time = sf.format(tt.toDate())

                                                chat = c["content"] as String
                                                if(chat.length > 20){
                                                    chat = chat.substring(0,19) + "..."
                                                }
                                                isChecked = c["isChecked"] as Boolean
                                                fromMe = c["fromMe"] as Boolean
                                            }
                                        }


                                        data.add(
                                            ChatList(
                                                chat,
                                                post,
                                                isChecked,
                                                fromMe,
                                                userName,
                                                ui,
                                                time,
                                                match
                                            )
                                        )
                                        if(i == userListB[userListB.size - 1]){
                                            Log.d("chatList", "$data 마지막222")
                                            model.itemList.value = data
                                        }
                                    }
                            }
                        }
                    }

            }

        }
    }

}