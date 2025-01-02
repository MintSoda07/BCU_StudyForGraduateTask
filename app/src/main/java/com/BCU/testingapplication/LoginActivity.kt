package com.BCU.testingapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.acitivity_login)

        val emailPlaceHolderForLogin = findViewById<TextView>(R.id.email_textView)
        val passwordPlaceHolderForLogin = findViewById<TextView>(R.id.passwordTextview)
        val submitButtonLogin = findViewById<Button>(R.id.logInButton)
        val loginFailedMessageTextView = findViewById<TextView>(R.id.warningText)
        val signUpButton = findViewById<TextView>(R.id.signUpText)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        signUpButton.setOnClickListener{
            val signIpPageRedirect = Intent(this@LoginActivity, SignUpPage::class.java)
            startActivity(signIpPageRedirect)
        }
        submitButtonLogin.setOnClickListener {
            val emailText = emailPlaceHolderForLogin.text.toString()
            val passwordText = passwordPlaceHolderForLogin.text.toString()
            val loginManager = LoginManager()

            if (emailText == "" || passwordText == "") {
                if (emailText == "") {
                    loginFailedMessageTextView.text = getString(R.string.no_email)
                } else {
                    loginFailedMessageTextView.text = getString(R.string.no_password)
                }
            } else {
                loginManager.Login(emailText, passwordText, object : LoginCallback {
                    override fun onSuccess(user: FirebaseUser?) {
                        Log.d("Login", "User logged in: ${user?.email}")

                        // 로그인 성공 시 실행할 함수
                        Toast.makeText(applicationContext, R.string.auth_Passed,Toast.LENGTH_LONG).show()
                        val mainPageForAct = Intent(this@LoginActivity, MyWorksViewInMainActivity::class.java)
                        startActivity(mainPageForAct)
                    }

                    override fun onFailure(exception: Exception?) {
                        Log.e("Login", "Login failed: ${exception?.message}")
                        emailPlaceHolderForLogin.requestFocus()
                        val inputMethodManager =
                            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.showSoftInput(
                            emailPlaceHolderForLogin,
                            InputMethodManager.SHOW_IMPLICIT
                        )
                        loginFailedMessageTextView.text = getString(R.string.wrong_Auth)
                    }
                })
            }
        }
    }
}