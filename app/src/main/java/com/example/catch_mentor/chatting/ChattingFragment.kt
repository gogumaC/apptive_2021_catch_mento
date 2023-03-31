package com.example.catch_mentor.chatting

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.chatting.ChattingFragment.chatting.userID
import com.example.catch_mentor.dataClass.Mentoring
import com.example.catch_mentor.databinding.FragmentChattingBinding
import com.example.catch_mentor.interactors.MenteeInteractor
import com.example.catch_mentor.interactors.MentorInteractor
import com.example.catch_mentor.interactors.MentoringInteractor
import com.example.catch_mentor.mypage.ProfileDialog
import com.example.catch_mentor.post.post
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.write.WriteInfoS
import com.example.catch_mentor.write.postID.documentID
import com.example.catch_mentor.write.postID.documentName
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

interface chattingListener{
    fun chatUpload(data : MutableList<Chat>)
}

class ChattingFragment : BaseFragment(), chattingListener{
    private lateinit var binding: FragmentChattingBinding
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    var data : MutableList<Chat> = mutableListOf()
    private var livedata: MutableLiveData<String> = MutableLiveData()
    val mentoringInteractor=MentoringInteractor()

    var userName = ""
    var myName = ""
    var isInitial = false

    object chatting{
        var userID = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChattingBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    lateinit var chattingAdapter : ChattingAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chattingAdapter = ChattingAdapter(requireContext())
        binding.chattingRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.chattingRecycler.adapter = chattingAdapter
        val myID = UserDataManager.id!!
        chatDB(myID, userID, this)

        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationIcon(R.drawable.icon_back)

        val reportbtn = toolbar.findViewById<ImageButton>(R.id.toolbar_btn_report)

        reportbtn.visibility = View.VISIBLE

        reportbtn.setOnClickListener {
            reportChat(userID, myID)
        }

        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        livedata.observe(requireActivity(), {
            toolbar.findViewById<TextView>(R.id.toolbar_title).text = it
        })

        binding.chattingBtnFail.setOnClickListener {
            binding.chattingBtnFail.toggle()
            val dialog = ChatDialog(requireContext())
            dialog.showDialog("매칭 실패")
            dialog.setOnClickListener(object : ChatDialog.OnDialogClickListener {
                override fun onClicked(name: String)
                {
                    matchSet("매칭 실패")
                    db.collection("chatting").document(myID).collection(userID).document("info").update(
                        "matching", name
                    ).addOnFailureListener {
                        Log.d("chat", it.toString())
                    }
                    Toast.makeText(requireContext(), "매칭 실패로 변경되었어요.", Toast.LENGTH_SHORT).show()
                }

            })
        }

        binding.chattingBtnComplete.setOnClickListener {
            binding.chattingBtnComplete.toggle()
            val dialog = ChatDialog(requireContext())
            dialog.showDialog("매칭 완료")
            dialog.setOnClickListener(object : ChatDialog.OnDialogClickListener {
                override fun onClicked(name: String)
                {
                    matchSet("매칭 완료")
                    db.collection("chatting").document(myID).collection(userID).document("info").update(
                        "matching", name
                    )
                    Toast.makeText(requireContext(), "매칭 완료로 변경되었어요.", Toast.LENGTH_SHORT).show()


                    //TODO 여기서 멘토링 데이터 DB에 올려주시면 됩니다!!
                    var mentorID=""
                    var menteeID=""
                    if(UserDataManager.isMentor()){
                        mentorID=myID
                        menteeID=userID
                    }else{
                        mentorID=userID
                        menteeID=myID
                    }
                    val mentoring= Mentoring(
                        title="멘토링",
                        mentorId = mentorID,
                        menteeId = menteeID,
                        mentor=MentorInteractor().ref.document(mentorID),
                        mentee = MenteeInteractor().ref.document(menteeID),
                        startDate = Date()
                    )
                    mentoringInteractor.createData(mentoring)

                }

            })
        }
        binding.chattingBtnIng.setOnClickListener {
            binding.chattingBtnIng.toggle()
            val dialog = ChatDialog(requireContext())
            dialog.showDialog("매칭 중")
            dialog.setOnClickListener(object : ChatDialog.OnDialogClickListener {
                override fun onClicked(name: String)
                {
                    matchSet("매칭 중")

                    db.collection("chatting").document(myID).collection(userID).document("info").update(
                        "matching", name
                    )
                    Toast.makeText(requireContext(), "매칭 중으로 변경되었어요.", Toast.LENGTH_SHORT).show()
                }

            })
        }
        binding.chattingSendButton.setOnClickListener {


            //알고리즘 : 누르면 채팅리스트에 해당 메시지를 추가하고 그걸 덮어씌운다...

            var txt = binding.chattingEditText.text.toString()
            binding.chattingEditText.text = null
            if(txt.isNotEmpty()){
                var tt = Timestamp.now()
                val sf = SimpleDateFormat("HH:mm", Locale.KOREA)
                sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                val time = sf.format(tt.toDate())

                db.collection("chatting").document(myID).collection(userID).add(
                    hashMapOf(
                        "content" to txt,
                        "fromMe" to true,
                        "isChecked" to false,
                        "time" to tt,
                        "name" to userID
                    )
                )

                data.add(
                    Chat(
                    true, txt,  time, false, myName
                    )
                )

                chatUpload(data)
                if(isInitial) initialChat(UserDataManager.id!!)

                db.collection("chatting").document(userID).collection(myID).add(
                    hashMapOf(
                        "content" to txt,
                        "fromMe" to false,
                        "isChecked" to false,
                        "time" to tt,
                        "name" to myID
                    )
                )

                db.collection("chatting").document(myID).collection(userID).document("info").update(
                    "time", tt
                )

            }

        }
    }
    private fun chatDB(myID: String, userID: String, listener: chattingListener){
        var mCallback = listener

        var m = "mentor_user"
        var u = "mentee_user"
        if(!UserDataManager.isMentor()){
            m = "mentee_user"
            u = "mentor_user"
        }
        db.collection(u).document(userID).get().addOnSuccessListener {
            userName = it["name"] as String
            livedata.value = userName
        }
        db.collection(m).document(myID).get().addOnSuccessListener {
            myName = it["name"] as String
        }

        db.collection("chatting").document(myID).collection(userID).orderBy("time")
            .get().addOnSuccessListener{document->
                data.clear()

                if (document!!.documents.size > 0) {

                    for (i in document.documents){
                        if(i["matching"] != null){
                            matchSet(i["matching"] as String)
                        } else {
                            val t = i["time"] as Timestamp
                            // 타임스탬프를 한국 시간, 문자열로 바꿈
                            val sf = SimpleDateFormat("HH:mm a", Locale.KOREA)
                            sf.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                            val time = sf.format(t.toDate())

                            var name = ""

                            name = if(i["fromMe"] as Boolean){
                                myName
                            }else{
                                userName
                            }

                            data.add(
                                Chat(
                                    i["fromMe"] as Boolean,
                                    i["content"] as String,
                                    time,
                                    i["isChecked"] as Boolean,
                                    name
                                )
                            )
                        }
                    }
                    db.collection("chatting").document(myID).collection(userID).document("info").get(
                    ).addOnSuccessListener {
                        binding.chattingTitle.text = it["postName"] as String
                    }
                    mCallback.chatUpload(data)
                    checkChat()
                }else{
                    binding.chattingTitle.text = documentName
                    matchSet("매칭 중")
                    isInitial = true
                }
            }
    }
    override fun chatUpload(data : MutableList<Chat>){
        binding.chattingRecycler.removeAllViewsInLayout()
        chattingAdapter.setData(data)
        Log.d("채팅", "완료2 $data")
    }

    private fun reportChat(userID: String, myID: String){
        var bottomSheetDialog = BottomSheetDialog(requireContext())
        val v: View = layoutInflater.inflate(R.layout.dialog_report, null)
        //다이얼로그에서 보여줄 View객체 생성

        bottomSheetDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bottomSheetDialog.setContentView(v)

        //다이얼로그 보이기

        bottomSheetDialog.show()
        val cancel = v.findViewById<Button>(R.id.report_cancel)
        val report = v.findViewById<Button>(R.id.report_complete)
        val content = v.findViewById<EditText>(R.id.report_txt)

        cancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        report.setOnClickListener {
            if(content.text.toString().isNotBlank()) {
                db.collection("report").add(
                    hashMapOf(
                        "reportID" to userID,
                        "userID" to myID,
                        "content" to content.text.toString(),
                        "time" to Timestamp.now()
                        )
                ).addOnSuccessListener {
                        Toast.makeText(requireContext(), "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    bottomSheetDialog.dismiss()
                }
            }
        }


    }
    private fun initialChat(myID: String){

        db.collection("chatting").document(myID).collection(userID).document("info").set(
            hashMapOf(
                "matching" to "매칭 중",
                "time" to Timestamp.now(),
                "postName" to documentName,
                "postID" to documentID
            )
        )

        db.collection("chatting").document(userID).collection(myID).document("info").set(
            hashMapOf(
                "matching" to "매칭 중",
                "time" to Timestamp.now(),
                "postName" to documentName,
                "postID" to documentID
            )
        )

        var userList1 = mutableListOf<String>()
        var userList2 = mutableListOf<String>()


        db.collection("chatting").document(myID).get()
            .addOnSuccessListener {
                if(it["User"] != null) {
                    userList1 = it["User"] as MutableList<String>
                }
                if(!userList1.contains(userID)) userList1.add(userID)
                db.collection("chatting").document(myID).set(
                hashMapOf("User" to userList1))

            }
        db.collection("chatting").document(userID).get()
            .addOnSuccessListener {
                if(it["User"] != null) {
                    userList2 = it["User"] as MutableList<String>
                }
                if(!userList2.contains(myID)) userList2.add(myID)
                db.collection("chatting").document(userID).set(
                    hashMapOf("User" to userList2))
            }

        isInitial = false
    }

    private fun checkChat(){
        db.collection("chatting").document(userID).collection(UserDataManager.id!!)
            .whereEqualTo("fromMe", true).whereEqualTo("isChecked", false).get().addOnSuccessListener { value ->
                if(value != null) {
                    for(i in value.documents){
                        var id = i.id
                        db.collection("chatting").document(userID).collection(UserDataManager.id!!).document(id).update(
                            "isChecked", true)
                    }
                }
            }
    }
    fun matchSet(m:String){
        when(m){
            "매칭 완료" -> {
                binding.chattingBtnIng.isChecked = false
                binding.chattingBtnIng.setTextColor(resources.getColor(R.color.match_default, resources.newTheme()))
                binding.chattingBtnFail.isChecked = false
                binding.chattingBtnFail.setTextColor(resources.getColor(R.color.match_default, resources.newTheme()))

                binding.chattingBtnIng.isClickable = false
                binding.chattingBtnFail.isClickable = false

                binding.chattingBtnComplete.toggle()
                binding.chattingBtnComplete.setTextColor(resources.getColor(R.color.match_com, resources.newTheme()))
            }
            "매칭 중" ->{
                binding.chattingBtnComplete.isChecked = false
                binding.chattingBtnComplete.setTextColor(resources.getColor(R.color.match_default, resources.newTheme()))

                binding.chattingBtnFail.isChecked = false
                binding.chattingBtnFail.setTextColor(resources.getColor(R.color.match_default, resources.newTheme()))

                binding.chattingBtnIng.toggle()
                binding.chattingBtnIng.setTextColor(resources.getColor(R.color.match_ing, resources.newTheme()))
            }
            "매칭 실패" ->{
                binding.chattingBtnIng.isChecked = false
                binding.chattingBtnIng.setTextColor(resources.getColor(R.color.match_default, resources.newTheme()))
                binding.chattingBtnComplete.isChecked = false
                binding.chattingBtnComplete.setTextColor(resources.getColor(R.color.match_default, resources.newTheme()))

                binding.chattingBtnFail.toggle()
                binding.chattingBtnFail.setTextColor(resources.getColor(R.color.match_fail, resources.newTheme()))
            }
        }
    }
}

