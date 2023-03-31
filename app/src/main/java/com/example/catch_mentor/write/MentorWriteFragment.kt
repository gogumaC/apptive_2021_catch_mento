package com.example.catch_mentor.write

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
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
import com.example.catch_mentor.databinding.FragmentWriteMentorBinding
import com.example.catch_mentor.post.PostAdapter
import com.example.catch_mentor.write.postID.writeMode
import com.example.catch_mentor.write.write.writeLocal
import com.example.catch_mentor.write.write.writeSubject
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MentorWriteFragment: BaseFragment() {

    private lateinit var binding: FragmentWriteMentorBinding
    private lateinit var offlineAdapter: PostAdapter

    private var isWriteReady = false
    val user = Firebase.auth.currentUser
    var frequency: Int = 0
    var subject: String = ""


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
        toolbar.findViewById<TextView>(R.id.toolbar_temp_btn).visibility = View.VISIBLE
        toolbar.findViewById<TextView>(R.id.toolbar_temp_btn).setOnClickListener {
            TempSave()
        }

        if(writeLocal.size > 0){
            binding.mentorWriteOfflineWarning.visibility = View.GONE
        }
        if(writeSubject.isNotBlank()){
            binding.mentorWriteCategoryWarning.visibility = View.GONE
            binding.mentorWriteCategoryWarning2.visibility = View.GONE
        }


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



        binding.mentorWriteButton.setOnClickListener {
            checkpost()
            savepost()
        }

        binding.mentorWriteCategorySpinner.setOnClickListener{
            writeMode = true
            view.findNavController().navigate(R.id.action_writeMentorFragment_to_searchSubjectFragment)
        }

        binding.mentorWriteOfflineButton.setOnClickListener {
            writeMode = true
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

        if(writeLocal.size == 0){
            binding.mentorWriteOfflineWarning.visibility = View.VISIBLE
        }

        //TODO 횟수 스피너 쑤셔넣기...

        if(writeSubject.isEmpty()){
            binding.mentorWriteCategoryWarning.visibility = View.VISIBLE
            binding.mentorWriteCategoryWarning2.visibility = View.VISIBLE
        }

        if(title.isNotEmpty() && subtitle.isNotEmpty() && curri.isNotEmpty() && educontent.isNotEmpty() && appeal.isNotEmpty() && writeSubject.isNotEmpty() && writeLocal.size != 0){
            isWriteReady = true
        }
    }

    //제목 내용이 적혀 있으면 해당 내용을 파이어베이스에 올립니다.
    private fun savepost() {
        if (isWriteReady){
            val db = Firebase.firestore
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
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
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
                            "offline" to writeLocal.toList(),
                            "sex" to document["sex"] as String,
                            "time" to Timestamp.now(),
                            "star_rating" to (document["star_rating"] as Long).toString()
                        )

                        // Add a new document with a generated ID
                        db.collection("mentor_post")
                            .add(searchMentor)
                            .addOnSuccessListener { documentReference ->
                                Log.d("게시글쓰기", "DocumentSnapshot added with ID: $writeLocal")
                                Toast.makeText(context,"작성 성공",Toast.LENGTH_SHORT).show()
                                writeLocal.clear()
                                writeSubject = ""
                                writeMode = false
                                requireView().findNavController().navigate(R.id.action_writeMentorFragment_to_searchFragment)
                            }
                            .addOnFailureListener { e ->
                                Log.w("", "Error adding document", e)
                            }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }



        }
    }

    private fun TempSave(){
        val db = Firebase.firestore
        var id = "testID"
        var userName = "test"
        var userGrade = "user"

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
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
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
                        "offline" to writeLocal,
                        "time" to Timestamp.now()
                    )

                    // Add a new document with a generated ID
                    db.collection("mentor_user").document(id).collection("temporary_post")
                        .add(searchMentor)
                        .addOnSuccessListener { documentReference ->
                            Log.d("", "DocumentSnapshot added with ID: ${documentReference.id}")
                            Toast.makeText(context,"게시글이 보관함에 저장되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.w("", "Error adding document", e)
                        }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    override fun onResume() {
        super.onResume()
        if(!writeSubject.equals("Empty")){
            binding.mentorWriteCategorySpinner.text = writeSubject
            subject = writeSubject
        }
        if(writeLocal.isNotEmpty()){
            binding.mentorWriteOfflineList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.mentorWriteOfflineList.adapter = offlineAdapter
            offlineAdapter.setData(writeLocal)
        }
    }
}

