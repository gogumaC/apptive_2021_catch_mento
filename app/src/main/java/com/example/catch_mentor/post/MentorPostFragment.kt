package com.example.catch_mentor.post

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catch_mentor.R
import com.example.catch_mentor.baseClass.BaseFragment
import com.example.catch_mentor.chatting.ChattingFragment
import com.example.catch_mentor.chatting.ChattingFragment.chatting.userID
import com.example.catch_mentor.databinding.FragmentPostMentorBinding
import com.example.catch_mentor.databinding.TestFragmentBinding
import com.example.catch_mentor.post.post.isTemp
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.write.WriteInfoS
import com.example.catch_mentor.write.postID
import com.example.catch_mentor.write.postID.documentID
import com.example.catch_mentor.write.write
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MentorPostFragment: BaseFragment() {

    private lateinit var binding: FragmentPostMentorBinding
    private lateinit var postAdapter1: PostAdapter
    private lateinit var postAdapter2: PostAdapter
    var user = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostMentorBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postAdapter1 = PostAdapter()
        postAdapter2 = PostAdapter()


        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "멘토 프로필"
        toolbar.findViewById<TextView>(R.id.toolbar_title).setTextColor(resources.getColor(R.color.text_dark, resources.newTheme()))

        val favbtn = toolbar.findViewById<ToggleButton>(R.id.toolbar_btn)

        if(!UserDataManager.isMentor()){
            favbtn.visibility = View.VISIBLE
            if(postID.documentFav) favbtn.toggle()
            favbtn.setOnClickListener {
                if (favbtn.isChecked) FavoriteListInteractorPost().favadd(user!!.uid, documentID)
                else FavoriteListInteractorPost().favdel(user!!.uid, documentID)
            }
        }
        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }


        val db = Firebase.firestore
        var docRef = db.collection("mentor_post").document(documentID)
        if(isTemp) {docRef = db.collection("mentor_user").document(UserDataManager.id!!).collection("temporary_post").document(documentID)
        isTemp = false
            }

        Log.d("포스트", "$docRef $isTemp")

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    ChattingFragment.chatting.userID = document["userID"] as String

                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data} " + documentID)
                    binding.mentorPostTitle.setText(document["title"] as String)
                    binding.mentorPostSubtitle.setText(document["subTitle"] as String)
                    document["subject"] as String
                    binding.mentorPostGrade.text = document["userGrade"] as String
                    binding.mentorPostAppeal.text = (document["appeal"] as String)
                    binding.mentorPostCurr.text = (document["curri"] as String)
                    binding.mentorPostEducont.text = (document["educontent"] as String)
                    binding.mentorPostCategorySpinner.text = (document["subject"] as String)
                    binding.mentorPostFrequencySpinner.text = (document["frequency"] as Long).toString()
                    binding.mentorPostGroupButton.text = if (document["group"] as Boolean) "가능" else "불가능"
                    binding.mentorPostOnlineButton.text = if(document["online"] as Boolean)"가능" else "불가능"

                    binding.mentorPostOfflineButton.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.mentorPostOfflineButton.adapter = postAdapter1
                    postAdapter1.setData(document["offline"] as MutableList<String>)


                    db.collection("mentor_user").document(userID).get()
                        .addOnSuccessListener {
                            binding.mentorPostGradeTextview2.text = it["univ"] as String + " " + it["major"] as String
                            binding.mentorPostNicknameTextview2.text = it["name"] as String
                            binding.mentorPostNickname.text = it["name"] as String
                            binding.mentorPostRegionTextview2.text = it["region"] as String
                            binding.mentorPostStar.text = (it["star_rating"] as Long).toString()
                            binding.mentorPostCareerTextview2.text = it["career"] as String
                        }
                    ServerProfile().imageDownload(requireContext(), userID, binding.mentorPostImage)

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }


        if(!UserDataManager.isMentor()){
            binding.mentorPostFloattingBtn.visibility = View.VISIBLE
        }

        binding.mentorPostFloattingBtn.setOnClickListener { //채팅으로 변환
            view.findNavController().navigate(R.id.action_postMentorFragment_to_chattingFragment)
        }


    }
}