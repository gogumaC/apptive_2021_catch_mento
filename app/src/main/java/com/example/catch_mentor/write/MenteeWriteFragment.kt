package com.example.catch_mentor.write

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentWriteMenteeBinding
import com.example.catch_mentor.post.PostAdapter
import com.example.catch_mentor.search.SearchFragment
import com.example.catch_mentor.write.postID.writeMode
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenteeWriteFragment:BaseFragment() {

    private lateinit var binding: FragmentWriteMenteeBinding
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

        toolbar.findViewById<TextView>(R.id.toolbar_temp_btn).visibility = View.VISIBLE
        toolbar.findViewById<TextView>(R.id.toolbar_temp_btn).setOnClickListener {
            TempSave()
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




        binding.menteeWriteButton.setOnClickListener {
            checkpost()
            savepost()
        }


        binding.menteeWriteOfflineButton.setOnClickListener {
            writeMode = true
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
            val db = Firebase.firestore
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
                        db.collection("mentee_post")
                            .add(searchmentee)
                            .addOnSuccessListener { documentReference ->
                                Log.d("", "DocumentSnapshot added with ID: ${documentReference.id}")
                                write.writeLocal.clear()
                                write.writeSubject = ""
                                writeMode = false
                                Toast.makeText(context, "작성 성공", Toast.LENGTH_SHORT).show()
                                requireView().findNavController()
                                    .navigate(R.id.action_writeMenteeFragment_to_searchFragment)
                            }
                            .addOnFailureListener { e ->
                                Log.w("", "Error adding document", e)
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

    private fun TempSave(){
        val db = Firebase.firestore
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
                    )

                    // Add a new document with a generated ID
                    db.collection("mentee_user").document(id).collection("temporary_post")
                        .add(searchmentee)
                        .addOnSuccessListener { documentReference ->
                            Log.d("", "DocumentSnapshot added with ID: ${documentReference.id}")
                            Toast.makeText(context,"게시글이 보관함에 저장되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.w("", "Error adding document", e)
                        }
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
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