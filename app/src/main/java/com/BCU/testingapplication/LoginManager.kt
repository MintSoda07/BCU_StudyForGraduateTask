package com.BCU.testingapplication
import android.util.Log

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginManager() {

    val AuthServer: FirebaseAuth = FirebaseAuth.getInstance()
    // 비동기 로그인 시도 (콜백 사용)
    fun Login(email: String, password: String, callback:LoginCallback) {
        AuthServer.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    val user = AuthServer.currentUser
                    Log.d("FB_Auth", "Login:success :: User = $user ::")
                    callback.onSuccess(user)
                } else {
                    // 로그인 실패
                    Log.w("FB_Auth", "Login:failure", task.exception)
                    callback.onFailure(task.exception)
                }
            }
    }
}