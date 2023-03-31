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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.chatting.ChattingFragment
import com.example.catch_mentor.databinding.FragmentWriteMentorBinding
import com.example.catch_mentor.post.PostAdapter
import com.example.catch_mentor.post.post
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.write.postID
import com.example.catch_mentor.write.write
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MypageMypostEditMentorFragment: BaseFragment() {
    private lateinit var binding: FragmentWriteMentorBinding
    private lateinit var offlineAdapter: PostAdapter
    val db = Firebase.firestore

    private var isWriteReady = false
    val user = Firebase.auth.currentUser
    var frequency: Int = 0
    var subject: String = ""

    var docid = postID.documentID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentWriteMentorBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offlineAdapter = PostAdapter()
        //  database = Firebase.database.reference
        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        if(write.writeLocal.size > 0){
            binding.mentorWriteOfflineWarning.visibility = View.GONE
        }
        if(write.writeSubject.isNotBlank()){
            binding.mentorWriteCategoryWarning.visibility = View.GONE
            binding.mentorWriteCategoryWarning2.visibility = View.GONE
        }

        var docRef = db.collection("mentor_post").document(postID.documentID)
        if(post.isTemp) {docRef = db.collection("mentor_user").document(UserDataManager.id!!).collection("temporary_post").document(
            postID.documentID
        )
            post.isTemp = false
        }

        Log.d("포스트", "$docRef ${post.isTemp}")



        binding.mentorWriteFrequencySpinner.adapter = ArrayAdapter.createFromResource(requireContext(), R.array.frequencyList,
            R.layout.item_spinner)

        binding.mentorWriteFrequencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
                binding.mentorWriteFrequencyWarning.visibility = View.GONE
                binding.mentorWriteFrequencyWarning2.visibility = View.GONE
            }
        }


        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    ChattingFragment.chatting.userID = document["userID"] as String

                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data} " + postID.documentID)
                    binding.mentorWriteTitle.setText(document["title"] as String)
                    binding.mentorWriteSubtitle.setText(document["subTitle"] as String)
                    document["subject"] as String
                    binding.mentorWriteAppeal.setText(document["appeal"] as String)
                    binding.mentorWriteCurr.setText(document["curri"] as String)
                    binding.mentorWriteEducont.setText(document["educontent"] as String)
                    binding.mentorWriteCategorySpinner.text = (document["subject"] as String)
                    binding.mentorWriteFrequencySpinner.setSelection((document["frequency"] as Long).toInt() - 1)
                    binding.mentorWriteGroupButton.isChecked = (document["group"] as Boolean)
                    binding.mentorWriteOnlineButton.isChecked = (document["online"] as Boolean)

                    binding.mentorWriteOfflineList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.mentorWriteOfflineList.adapter = offlineAdapter
                    write.writeLocal = document["offline"] as MutableList<String>
                    offlineAdapter.setData(write.writeLocal)


                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        binding.mentorWriteButton.setOnClickListener {
            checkpost()
            savepost()
        }

        binding.mentorWriteCategorySpinner.setOnClickListener{
            postID.writeMode = true
            view.findNavController().navigate(R.id.action_writeMentorFragment_to_searchSubjectFragment)
        }

        binding.mentorWriteOfflineButton.setOnClickListener {
            postID.writeMode = true
            view.findNavController().navigate(R.id.action_writeMentorFragment_to_searchAddress)
        }






    }
    //제목과 내용이 잘 적혀 있는지 확인을 합니다.
    @SuppressLint("ResourceAsColor")


    private fun checkpost() {
        val title = binding.mentorWriteTitle.text.toString()
        val subtitle = binding.mentorWriteSubtitle.text.toString()
        val curri = binding.mentorWriteCurr.text.toString()
        val educontent = binding.mentorWriteEducont.text.toString()
        val appeal = binding.mentorWriteAppeal.text.toString()

        if (title.isEmpty()){
            binding.mentorWriteTitle.setHintTextColor(R.color.red)
        }
        if (subtitle.isEmpty()){
            binding.mentorWriteSubtitle.setHintTextColor(R.color.red)
        }
        if (curri.isEmpty()){
            binding.mentorWriteCurr.hint = "해당 부분의 내용을 채워주세요"
            binding.mentorWriteCurr.setHintTextColor(R.color.red)
        }
        if (educontent.isEmpty()){
            binding.mentorWriteEducont.hint = "해당 부분의 내용을 채워주세요"
            binding.mentorWriteEducont.setHintTextColor(R.color.red)
        }
        if (appeal.isEmpty()){
            binding.mentorWriteAppeal.hint = "해당 부분의 내용을 채워주세요"
            binding.mentorWriteAppeal.setHintTextColor(R.color.red)
        }

        if(write.writeLocal.size == 0){
            binding.mentorWriteOfflineWarning.visibility = View.VISIBLE
        }

        //TODO 횟수 스피너 쑤셔넣기...

        if(write.writeSubject.isEmpty()){
            binding.mentorWriteCategoryWarning.visibility = View.VISIBLE
            binding.mentorWriteCategoryWarning2.visibility = View.VISIBLE
        }

        if(title.isNotEmpty() && subtitle.isNotEmpty() && curri.isNotEmpty() && educontent.isNotEmpty() && appeal.isNotEmpty() && write.writeSubject.isNotEmpty() && write.writeLocal.size != 0){
            isWriteReady = true
        }
    }

    //제목 내용이 적혀 있으면 해당 내용을 파이어베이스에 올립니다.
    private fun savepost() {
        if (isWriteReady){
            var id: String = "testID"
            var userName: String = "test"
            var userGrade: String = "user"

            if (user != null) {
                id = user.uid
            } else {
                Toast.makeText(requireContext(), "로그인 정보 로드 실패", Toast.LENGTH_SHORT).show()
            }

            val docRef = db.collection("mentor_user").document(id)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        var doc : DocumentSnapshot = document
                        userName = doc.get("name") as String
                        userGrade = doc.get("univ") as String + " " + doc.get("major") as String
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                        val searchMentor = hashMapOf( //전달할 필드
                            "title" to binding.mentorWriteTitle.text.toString(),
                            "subTitle" to binding.mentorWriteSubtitle.text.toString(),
                            "curri" to binding.mentorWriteCurr.text.toString(),
                            "educontent" to binding.mentorWriteEducont.text.toString(),
                            "appeal" to binding.mentorWriteAppeal.text.toString(),
                            "userName" to userName,
                            "userGrade" to userGrade,
                            "userID" to id,
                            "subject" to subject,
                            "frequency" to frequency,
                            "group" to binding.mentorWriteGroupButton.isChecked,
                            "online" to binding.mentorWriteOnlineButton.isChecked,
                            "offline" to write.writeLocal.toList(),
                            "sex" to document["sex"] as String,
                            "time" to Timestamp.now(),
                            "star_rating" to (document["star_rating"] as Long).toString()
                        )

                        // Add a new document with a generated ID
                        db.collection("mentor_post").document(docid)
                            .update(searchMentor)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(context,"수정 성공", Toast.LENGTH_SHORT).show()
                                write.writeLocal.clear()
                                write.writeSubject = ""
                                postID.writeMode = false
                                postID.documentID = ""
                                findNavController().popBackStack()
                            }
                            .addOnFailureListener { e ->
                                Log.w("", "Error adding document", e)
                                Toast.makeText(context,"수정 실패, 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
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
        if(!write.writeSubject.equals("Empty")){
            binding.mentorWriteCategorySpinner.text = write.writeSubject
            subject = write.writeSubject
        }
        if(write.writeLocal.isNotEmpty()){
            binding.mentorWriteOfflineList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.mentorWriteOfflineList.adapter = offlineAdapter
            offlineAdapter.setData(write.writeLocal)
        }
    }
}