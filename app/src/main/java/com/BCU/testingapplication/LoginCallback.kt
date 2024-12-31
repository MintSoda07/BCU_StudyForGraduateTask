package com.BCU.testingapplication

import com.google.firebase.auth.FirebaseUser

interface LoginCallback {
    fun onSuccess(user: FirebaseUser?)
    fun onFailure(exception: Exception?)
}