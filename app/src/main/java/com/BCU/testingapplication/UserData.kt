package com.BCU.testingapplication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserData() {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()



    //Firestore에서 사용자 데이터를 가져오는 방법
    fun getUserData(callback: (User?) -> Unit) {
        val userUUIDData = auth.currentUser?.uid ?: return callback(null)
        Log.i("FB_AUTH","UserUID DATE IS :" + userUUIDData)
        db.collection("User")
            .document(userUUIDData)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = User(
                        uid = userUUIDData,
                        email = document.getString("email"),
                        displayName = document.getString("displayName"),
                        profileImageUrl = document.getDouble("profileImageUrl"),
                        createdAt = document.getTimestamp("createdAt"),
                        byte = document.getDouble("byte")
                    )
                    callback(user)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                callback(null)
            }
    }
}