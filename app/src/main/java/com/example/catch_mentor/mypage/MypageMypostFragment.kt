package com.example.catch_mentor.mypage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.databinding.FragmentMypageMypostBinding
import com.example.catch_mentor.post.post.isTemp
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.write.WriteInfoLMentee
import com.example.catch_mentor.write.WriteInfoLMentor
import com.example.catch_mentor.write.WriteInfoS
import com.example.catch_mentor.write.postID.documentID
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

interface mypageMypostListener{
    fun postLoad(t: MutableList<WriteInfoS>)
}


class MypageMypostFragment: BaseFragment(), mypageMypostListener {
    private lateinit var binding: FragmentMypageMypostBinding
    val db = FirebaseFirestore.getInstance()    // Firestore 인스턴스 선언
    private lateinit var mypageMypostAdapter : MypageMypostAdapter

    var user = Firebase.auth.currentUser
    var catchmentor: Boolean = false
    var isM = ""

    var itemList: MutableList<WriteInfoS> = mutableListOf()

    val isMentor = UserDataManager.isMentor()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageMypostBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "나의 게시물"
        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        mypageMypostAdapter = MypageMypostAdapter(requireContext())
        binding.mypageMypostList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.mypageMypostList.adapter = mypageMypostAdapter

        if(isMentor) {
            isM = "mentor_post"
            catchmentor = true
        }else {
            isM = "mentee_post"
            catchmentor = false
        }

        openDB(isM, false)


        binding.mypageMypostTempBtn.setOnClickListener {
            if(binding.mypageMypostTempBtn.isChecked) {
                binding.mypageMypostTempBtn.setTextColor(resources.getColor(R.color.text_dark, resources.newTheme()))

                binding.mypageMypostUploadBtn.isChecked = false
                binding.mypageMypostUploadBtn.setTextColor(
                    resources.getColor(
                        R.color.text_light_dark,
                        resources.newTheme()
                    )
                )
                isTemp = true
                openDB(isM, isTemp)

            }else binding.mypageMypostTempBtn.toggle()
        }

        binding.mypageMypostUploadBtn.setOnClickListener {
            if(binding.mypageMypostUploadBtn.isChecked) {
                binding.mypageMypostUploadBtn.setTextColor(resources.getColor(R.color.text_dark, resources.newTheme()))

                binding.mypageMypostTempBtn.isChecked = false
                binding.mypageMypostTempBtn.setTextColor(
                    resources.getColor(
                        R.color.text_light_dark,
                        resources.newTheme()
                    )
                )
                isTemp = false
                openDB(isM, isTemp)

            }else binding.mypageMypostUploadBtn.toggle()
        }

        mypageMypostAdapter.setOnItemClickListener(object : MypageMypostAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                documentID = itemList[pos].docuID
                if(UserDataManager.isMentor()){
                    view.findNavController()
                        .navigate(R.id.action_mypageMypostFragment_to_postMentorFragment)
                }else{
                    view.findNavController()
                        .navigate(R.id.action_mypageMypostFragment_to_postMenteeFragment)

                }
            }

            override fun onMenuClick(v: View, pos: Int) {

                //다이얼로그 객체 생성
                var bottomSheetDialog = BottomSheetDialog(requireContext())
                val v: View = layoutInflater.inflate(R.layout.dialog_mypost, null)
                //다이얼로그에서 보여줄 View객체 생성

                bottomSheetDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                bottomSheetDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                bottomSheetDialog.setContentView(v)

                //다이얼로그 보이기

                bottomSheetDialog.show()

                val listView2 = v.findViewById<ListView>(R.id.mypost_dialog_list2)
                val listView = v.findViewById<ListView>(R.id.mypost_dialog_list)

                //다이얼로그 안에 있는 리스트뷰 참조하기
                if(!isTemp) {
                    listView2.visibility = View.GONE
                    listView.visibility = View.VISIBLE
                    listView.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, i, _ ->
                            when (i) {
                                0 -> updatePost(itemList[pos].docuID)
                                1 -> editPost(itemList[pos].docuID)
                                2 -> myposttoTempPost(itemList[pos].userID, itemList[pos].docuID)
                                3 -> deletePost(itemList[pos].docuID)
                            }
                            bottomSheetDialog.dismiss()
                        }
                }else{
                    listView2.visibility = View.VISIBLE
                    listView.visibility = View.GONE
                    listView2.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, i, _ ->
                            when (i) {
                                0 -> editTempPost(itemList[pos].docuID)
                                1 -> deleteTempPost(itemList[pos].userID, itemList[pos].docuID, i)
                            }
                            bottomSheetDialog.dismiss()
                        }
                }

            }
        })

    }


    override fun postLoad(t: MutableList<WriteInfoS>) {
        itemList = t
        Log.d("마이페이지", "${itemList}")
        mypageMypostAdapter.setData(itemList)
    }

    private fun deleteTempPost(id: String, idDoc: String, i: Int){
        if(isMentor) db.collection("mentor_user").document(id).collection("temporary_post").document(idDoc).delete()
        else db.collection("mentor_user").document(id).collection("temporary_post").document(idDoc).delete()
        openDB(isM, isTemp)
    }

    private fun myposttoTempPost(id: String, idDoc: String){
        if(isMentor){
            db.collection("mentor_post").document(idDoc).get().addOnSuccessListener {
                db.collection("mentor_user").document(id).collection("temporary_post").add(
                    hashMapOf(
                    "title" to it["title"],
                    "subTitle" to it["subTitle"],
                    "curri" to it["curri"],
                    "educontent" to it["educontent"],
                    "appeal" to it["appeal"],
                    "userName" to it["userName"],
                    "userGrade" to it["userGrade"],
                    "userID" to it["userID"],
                    "subject" to it["subject"],
                    "frequency" to it["frequency"],
                    "group" to it["group"],
                    "online" to it["online"],
                    "offline" to it["offline"],
                    "time" to Timestamp.now()
                )
                ).addOnSuccessListener {
                    db.collection("mentor_post").document(idDoc).delete()
                    openDB(isM, isTemp)
                }
            }
        }
        else{
            db.collection("mentee_post").document(idDoc).get().addOnSuccessListener {
                var add = hashMapOf(
                    "title" to it["title"],
                    "subTitle" to it["subTitle"],
                    "content" to it["educontent"],
                    "userName" to it["userName"],
                    "userGrade" to it["userGrade"],
                    "userID" to it["userID"],
                    "subject" to it["subject"],
                    "frequency" to it["frequency"],
                    "group" to it["group"],
                    "online" to it["online"],
                    "offline" to it["offline"],
                    "isMe" to it["isMe"],
                    "time" to Timestamp.now()
                )
                db.collection("mentee_user").document(id).collection("temporary_post").add(add).addOnSuccessListener {
                    db.collection("mentor_post").document(idDoc).delete()
                    openDB(isM, isTemp)
                }
            }
        }
    }

    private fun openDB(isM : String, i:Boolean){
        itemList.clear()
        Log.d("마페", "${isMentor} ${isTemp}")
        MypostInDB(this).MypostUpload(isM, i, itemList)
    }

    fun updatePost(id: String){
        if(isMentor) db.collection("mentor_post").document(id).update(
            "time", Timestamp.now()
        )
        else db.collection("mentee_post").document(id).update(
            "time", Timestamp.now()
        )
        openDB(isM, isTemp)
    }
    fun deletePost(id: String){
        if(isMentor) db.collection("mentor_post").document(id).delete().addOnSuccessListener { openDB(isM, isTemp) }
        else db.collection("mentee_post").document(id).delete().addOnSuccessListener { openDB(isM, isTemp) }

    }
    fun editPost(id: String){

        if(isMentor) {
            documentID = id
            requireView().findNavController().navigate(R.id.action_mypageMypostFragment_to_mypageMypostEditMentorFragment)

        }else{
            requireView().findNavController().navigate(R.id.action_mypageMypostFragment_to_mypageMypostEditMenteeFragment)
            documentID = id
        }

    }

    fun editTempPost(id: String){

        isTemp = true

        if(isMentor) {
            documentID = id
            requireView().findNavController().navigate(R.id.action_mypageMypostFragment_to_mypageMypostEditMentorFragment)

        }else{
            requireView().findNavController().navigate(R.id.action_mypageMypostFragment_to_mypageMypostEditMenteeFragment)
            documentID = id
        }


    }

    override fun onResume() {
        super.onResume()
        binding.mypageMypostUploadBtn.isChecked = true
        binding.mypageMypostTempBtn.isChecked = false
        binding.mypageMypostTempBtn.setTextColor(
            resources.getColor(
                R.color.text_light_dark,
                resources.newTheme()
            )
        )
        binding.mypageMypostUploadBtn.setTextColor(
            resources.getColor(
                R.color.text_dark,
                resources.newTheme()
            )
        )
    }
}