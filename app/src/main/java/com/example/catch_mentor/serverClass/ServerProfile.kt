package com.example.catch_mentor.serverClass

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.catch_mentor.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URL

open class ServerProfile{


    val storage = FirebaseStorage.getInstance()
    open fun imageload(userID: String, image: Drawable){
    // Create a storage reference from our app
        val storageRef = storage.reference

    // Create a reference to "mountains.jpg"
        val userImageRef = storageRef.child(userID+"/"+userID+"_profileImage.jpg")

    // Create a reference to 'images/mountains.jpg'
        val userImageImagesRef = storageRef.child(userID+"/"+userID+"_profileImage.jpg")



        // Get the data from an ImageView as bytes
        val bitmap = (image as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = userImageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d("이미지 업로드", it.toString())
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Log.d("이미지 업로드", "성공")
        }


    }

    open fun imageDownload(context: Context, userID: String, image: ImageView){
        // Create a storage reference from our app
        val storageRef = FirebaseStorage.getInstance().reference

        // Create a reference to "mountains.jpg"
        val userImageRef = storageRef.child("$userID/${userID}.jpeg")
        Log.d("profileDL", "$userID/$userID")
//             Download directly from StorageReference using Glide
//             (See MyAppGlideModule for Loader registration)

        userImageRef.downloadUrl
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(context /* context */)
                        .load(task.result)
                        .override(100, 100)
                        .into(image)
                    Log.d("profileDL", "image successfully updated!")
                } else {
                    Log.d("profileDL", "실패" + task.getException().toString())

                }
            }
    }


}