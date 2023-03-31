package com.example.catch_mentor.post

import android.annotation.SuppressLint
import android.content.ContentValues
import android.media.Image
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
import com.example.catch_mentor.chatting.ChattingFragment.chatting.userID
import com.example.catch_mentor.databinding.FragmentPostMenteeBinding
import com.example.catch_mentor.databinding.FragmentPostMentorBinding
import com.example.catch_mentor.databinding.TestFragmentBinding
import com.example.catch_mentor.post.post.isTemp
import com.example.catch_mentor.search.SearchFragment
import com.example.catch_mentor.serverClass.FavoriteListInteractor
import com.example.catch_mentor.serverClass.ServerProfile
import com.example.catch_mentor.utils.UserDataManager
import com.example.catch_mentor.write.WriteInfoS
import com.example.catch_mentor.write.postID.documentFav
import com.example.catch_mentor.write.postID.documentID
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

interface postListener{
}

object post{
    var isTemp = false
}

class MenteePostFragment: BaseFragment(), postListener {

    private lateinit var binding: FragmentPostMenteeBinding
    var user = Firebase.auth.currentUser
    private lateinit var postAdapter1: PostAdapter
    private lateinit var postAdapter2: PostAdapter
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostMenteeBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var docRef = db.collection("mentee_post").document(documentID)
        if(isTemp){
            docRef = db.collection("mentee_user").document(user!!.uid).collection("temporary_post").document(documentID)
            isTemp = false
            }
        postAdapter1 = PostAdapter()
        postAdapter2 = PostAdapter()

        val toolbar: androidx.appcompat.widget.Toolbar = requireView().findViewById(R.id.catchmentor_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar.findViewById<TextView>(R.id.toolbar_title).text = "멘토 프로필"
        toolbar.findViewById<TextView>(R.id.toolbar_title).setTextColor(resources.getColor(R.color.text_dark, resources.newTheme()))

        val favbtn = toolbar.findViewById<ToggleButton>(R.id.toolbar_btn)

        if(UserDataManager.isMentor()){
            favbtn.visibility = View.VISIBLE
            if(documentFav) favbtn.toggle()
            favbtn.setOnClickListener {
                if (favbtn.isChecked) FavoriteListInteractorPost().favadd(user!!.uid, documentID)
                else FavoriteListInteractorPost().favdel(user!!.uid, documentID)
            }
        }

        toolbar.setNavigationIcon(R.drawable.icon_back)

        toolbar.setNavigationOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
        }

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    userID = document["userID"] as String

                    binding.menteePostTitle.setText(document["title"] as String)
                    binding.menteePostSubtitle.setText(document["subTitle"] as String)
                    binding.menteePostGrade.text = document["userGrade"] as String
                    binding.menteePostEducont.setText(document["content"] as String)
                    binding.menteePostCategorySpinner.text = (document["subject"] as String)
                    binding.menteePostFrequencySpinner.text = (document["frequency"] as Long).toString()
                    binding.menteePostGroupButton.text = if (document["group"] as Boolean) "가능" else "불가능"
                    binding.menteePostOnlineButton.text = if(document["online"] as Boolean)"가능" else "불가능"
                    binding.menteePostOfflineButton.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.menteePostOfflineButton.adapter = postAdapter1
                    postAdapter1.setData(document["offline"] as MutableList<String>)

                    if(!(document["isMe"] as Boolean))
                    binding.menteePostIsMeTextview.text = "작성자는 멘티의 대리인입니다"
                    db.collection("mentee_user").document(userID).get()
                        .addOnSuccessListener {
                            binding.menteePostNickname.text = it["name"] as String
                            binding.menteePostGradeTextview2.text = it["school"] as String
                            binding.menteePostNicknameTextview2.text = it["name"] as String
                            binding.menteePostRegionTextview2.text = it["region"] as String
                        }
                    ServerProfile().imageDownload(requireContext(), userID, binding.menteePostImage)

                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }

        if(UserDataManager.isMentor()){
            binding.menteePostFloattingBtn.visibility = View.VISIBLE
        }
        binding.menteePostFloattingBtn.setOnClickListener {

            view.findNavController().navigate(R.id.action_postMenteeFragment_to_chattingFragment)
        }

    }


}