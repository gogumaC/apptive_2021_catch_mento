package com.example.catch_mentor.mypage

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.chatting.ChattingFragment
import com.example.catch_mentor.databinding.FragmentWriteMenteeBinding
import com.example.catch_mentor.databinding.FragmentWriteMentorBinding
import com.example.catch_mentor.post.PostAdapter
import com.example.catch_mentor.post.post
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.write.postID
import com.example.catch_mentor.write.postID.documentID
import com.example.catch_mentor.write.write
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MypageMypostEditMenteeFragment(): BaseFragment() {

    private lateinit var binding: FragmentWriteMenteeBinding
    private lateinit var offlineAdapter: PostAdapter
    val db = Firebase.firestore

    private var isWriteReady = false
    val user = Firebase.auth.currentUser
    var frequency: Int = 0
    var subject: String = ""

    var docid = documentID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWriteMenteeBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offlineAdapter = PostAdapter()

        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }
        
        

        //  database = Firebase.database.reference
        if(write.writeLocal.size > 0){
            binding.menteeWriteOfflineWarning.visibility = View.GONE
        }
        if(write.writeSubject.isNotBlank()){
            binding.menteeWriteCategoryWarning.visibility = View.GONE
            binding.menteeWriteCategoryWarning2.visibility = View.GONE
        }

        binding.menteeWriteFrequencySpinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.frequencyList,
            R.layout.item_spinner)

        binding.menteeWriteFrequencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                frequency = position+1
            }
        }
        var docRef = db.collection("mentee_post").document(documentID)
        if(post.isTemp){
            docRef = db.collection("mentee_user").document(user!!.uid).collection("temporary_post").document(documentID)
            post.isTemp = false
        }

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    ChattingFragment.chatting.userID = document["userID"] as String

                    binding.menteeWriteTitle.setText(document["title"] as String)
                    binding.menteeWriteSubtitle.setText(document["subTitle"] as String)
                    binding.menteeWriteEducont.setText(document["content"] as String)
                    binding.menteeWriteCategorySpinner.text = (document["subject"] as String)
                    binding.menteeWriteFrequencySpinner.setSelection((document["frequency"] as Long).toInt() - 1)
                    binding.menteeWriteGroupButton.isChecked = (document["group"] as Boolean)
                    binding.menteeWriteOnlineButton.isChecked = (document["online"] as Boolean)

                    binding.menteeWriteOfflineList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.menteeWriteOfflineList.adapter = offlineAdapter
                    write.writeLocal = document["offline"] as MutableList<String>
                    offlineAdapter.setData(write.writeLocal)

                    if(!(document["isMe"] as Boolean)) binding.menteeWriteNotMe.toggle()
                    else binding.menteeWriteMe.toggle()

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }


        binding.menteeWriteButton.setOnClickListener {
            checkpost()
            savepost()
        }


        binding.menteeWriteOfflineButton.setOnClickListener {
            postID.writeMode = true
            view.findNavController().navigate(R.id.action_writeMenteeFragment_to_searchAddress)
        }

        binding.menteeWriteCategorySpinner.setOnClickListener{
            postID.writeMode = true
            view.findNavController().navigate(R.id.action_writeMenteeFragment_to_searchSubjectFragment)
        }



        binding.menteeWriteMe.setOnClickListener {
            if(binding.menteeWriteNotMe.isChecked) binding.menteeWriteNotMe.toggle()
        }
        binding.menteeWriteNotMe.setOnClickListener {
            if(binding.menteeWriteMe.isChecked) binding.menteeWriteMe.toggle()
        }



    }

    //제목과 내용이 잘 적혀 있는지 확인을 합니다.
    @SuppressLint("ResourceAsColor")
    private fun checkpost() {
        //TODO 멘토링 정보 경고창 마저 완성하기


        val title = binding.menteeWriteTitle.text.toString()
        val subtitle = binding.menteeWriteSubtitle.text.toString()
        val educontent = binding.menteeWriteEducont.text.toString()

        if (title.isEmpty()) {
            binding.menteeWriteTitle.hint = "내용을 채워주세요"
            binding.menteeWriteTitle.setHintTextColor(R.color.red)
        }
        if (subtitle.isEmpty()) {
            binding.menteeWriteSubtitle.hint = "내용을 채워주세요"
            binding.menteeWriteSubtitle.setHintTextColor(R.color.red)
        }
        if (educontent.isEmpty()) {
            binding.menteeWriteEducont.hint = "내용을 채워주세요"
            binding.menteeWriteEducont.setHintTextColor(R.color.red)
        }
        if(!(binding.menteeWriteNotMe.isChecked) && !(binding.menteeWriteMe.isChecked)){
            binding.menteeWriteCheckTxv.visibility = View.VISIBLE
        }
        if(write.writeLocal.size == 0){
            binding.menteeWriteOfflineWarning.visibility = View.VISIBLE
        }

        //TODO 횟수 스피너 쑤셔넣기...

        if(write.writeSubject.isEmpty()){
            binding.menteeWriteCategoryWarning.visibility = View.VISIBLE
            binding.menteeWriteCategoryWarning2.visibility = View.VISIBLE
        }

        if(title.isNotEmpty() && subtitle.isNotEmpty() && educontent.isNotEmpty() && write.writeSubject.isNotEmpty() && write.writeLocal.size != 0){
            isWriteReady = true
        }
    }

    //제목 내용이 적혀 있으면 해당 내용을 파이어베이스에 올립니다.
    private fun savepost() {
        if (isWriteReady) {
            var id: String = "testID"
            var userName: String = "test"
            var userGrade: String = "user"

            if (user != null) {
                id = user.uid
            } else {
                Toast.makeText(requireContext(), "로그인 정보 로드 실패", Toast.LENGTH_SHORT).show()
            }

            val docRef = db.collection("mentee_user").document(id)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        var doc: DocumentSnapshot = document
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data} " + id)
                        userGrade = doc.get("school") as String
                        userName = doc.get("name") as String
                        val searchmentee = hashMapOf( //전달할 필드
                            "title" to binding.menteeWriteTitle.text.toString(),
                            "subTitle" to binding.menteeWriteSubtitle.text.toString(),
                            "content" to binding.menteeWriteEducont.text.toString(),
                            "userName" to userName,
                            "userGrade" to userGrade,
                            "userID" to id,
                            "subject" to subject,
                            "frequency" to frequency,
                            "group" to binding.menteeWriteGroupButton.isChecked,
                            "online" to binding.menteeWriteOnlineButton.isChecked,
                            "isMe" to binding.menteeWriteMe.isChecked,
                            "offline" to write.writeLocal,
                            "sex" to doc.get("sex") as String,
                            "time" to Timestamp.now()
                        )

                        // Add a new document with a generated ID
                        db.collection("mentee_post").document(docid)
                            .update(searchmentee)
                            .addOnSuccessListener { documentReference ->
                                write.writeLocal.clear()
                                write.writeSubject = ""
                                postID.writeMode = false
                                Toast.makeText(context, "수정 성공", Toast.LENGTH_SHORT).show()
                                documentID = ""
                                findNavController().popBackStack()
                            }
                            .addOnFailureListener { e ->
                                Log.w("", "Error adding document", e)
                                Toast.makeText(context, "수정 실패, 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }
    }

    override fun onResume() {
        super.onResume()
        if(write.writeSubject != "Empty"){
            binding.menteeWriteCategorySpinner.text = write.writeSubject
            subject = write.writeSubject
        }
        if(write.writeLocal.isNotEmpty()){
            binding.menteeWriteOfflineList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.menteeWriteOfflineList.adapter = offlineAdapter
            offlineAdapter.setData(write.writeLocal)

        }
    }

}