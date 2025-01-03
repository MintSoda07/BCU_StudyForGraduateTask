package com.BCU.testingapplication

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class SignUpPage : AppCompatActivity() {

    lateinit var pageSignUpEmailText: TextInputEditText
    lateinit var pageSignUpPasswordText: TextInputEditText
    lateinit var pageSignUpName: TextInputEditText
    lateinit var pageSignUpDataSubmitButton: Button
    var is_processing = false

    lateinit var pageEmailViewHint: TextInputLayout
    lateinit var pagePasswordTextViewHint: TextInputLayout
    lateinit var pageNameTextViewHint: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // lateinit var 값 할당
        // 입력 필드 힌트 할당
        pageEmailViewHint = findViewById(R.id.emailLayout)
        pagePasswordTextViewHint = findViewById(R.id.pwdLayout)
        pageNameTextViewHint=findViewById(R.id.nameLayout)

        // 텍스트 입력 필드 할당
        pageSignUpEmailText=findViewById(R.id.emailInputField)
        pageSignUpPasswordText=findViewById(R.id.pwdInputField)
        pageSignUpName=findViewById(R.id.nameInputField)

        // Button 할당
        pageSignUpDataSubmitButton=findViewById(R.id.submitButtonSignUp)




        pageSignUpDataSubmitButton.setOnClickListener{
            if(is_processing == true){
                Toast.makeText(
                    baseContext, getString(R.string.wait_please),
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                auth_create()
            }

        }
    }

    // 기본적인 사용자 정보를 fireAuth에 추가한다
    fun auth_create() {

        val email = pageSignUpEmailText.text.toString()
        val password = pageSignUpPasswordText.text.toString()
        val name = pageSignUpName.text.toString()

        val SignUpPwdPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@#\$%^&+=!]{8,32}$"

        val auth = FirebaseAuth.getInstance()

        if (email == "") {
            pageSignUpEmailText.requestFocus()
            pageEmailViewHint.hint = getString(R.string.no_email)
        } else if (password == "") {
            pageSignUpPasswordText.requestFocus()
            pagePasswordTextViewHint.hint = getString(R.string.no_password)
        } else if (name == "") {
            pageSignUpName.requestFocus()
            pageNameTextViewHint.hint = getString(R.string.no_name)
        } else if(name.length > 8) {
            pageSignUpName.requestFocus()
            pageNameTextViewHint.hint = getString(R.string.name_over_limit)
        }else if(password.length >24 || password.length<8 ) {
            pageSignUpPasswordText.requestFocus()
            pagePasswordTextViewHint.hint = getString(R.string.pwd_over_limit)
        }else if(Pattern.matches(SignUpPwdPattern, password) == false ){
            pageSignUpPasswordText.requestFocus()
            pagePasswordTextViewHint.hint = getString(R.string.pwd_validate)
    }else {
            is_processing=true
            ViewCompat.setBackgroundTintList(pageSignUpDataSubmitButton, ColorStateList.valueOf(Color.parseColor("#737373")))

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 사용자 생성 성공
                        val user = auth.currentUser
                        addUserToFirestore(user)
                    } else {
                        // 실패 시 오류 처리
                        Log.e("Auth_SignUp", task.exception.toString())
                        Toast.makeText(
                            baseContext, getString(R.string.fail_sign_up),
                            Toast.LENGTH_SHORT
                        ).show()
                        ViewCompat.setBackgroundTintList(pageSignUpDataSubmitButton, ColorStateList.valueOf(Color.parseColor("#03A9F4")))
                    }
                }
        }

    }

    // 추가적인 사용자 정보 (이름, 사진, 가입일자 등)
    fun addUserToFirestore(user: FirebaseUser?) {
        val db = FirebaseFirestore.getInstance()

        // 사용자 정보 (예: 이름, 프로필 사진 URL 등)
        val userData = hashMapOf(
            "uid" to user?.uid,
            "email" to user?.email,
            "displayName" to pageSignUpName.text.toString(),  // 사용자가 입력한 이름
            "profileImageUrl" to R.drawable.bcu_project_icon,
            "createdAt" to FieldValue.serverTimestamp(),  // 가입 시간
            "byte" to 500
        )

        // Firestore에 사용자 데이터 추가
        user?.uid?.let {
            db.collection("User")
                .document(it)
                .set(userData)
                .addOnSuccessListener {
                    // 데이터 추가 성공
                    Toast.makeText(
                        baseContext,
                        getString(R.string.sign_up_completed),
                        Toast.LENGTH_SHORT
                    ).show()
                    val mainPageForAct = Intent(this@SignUpPage, MyWorksViewInMainActivity::class.java)
                    startActivity(mainPageForAct)
                    finish()
                }
                .addOnFailureListener { exception ->
                    // 실패 시 오류 처리
                    Log.e("Auth_SignUp", exception.toString())
                    Toast.makeText(
                        baseContext, getString(R.string.fail_sign_up),
                        Toast.LENGTH_SHORT
                    ).show()
                    ViewCompat.setBackgroundTintList(pageSignUpDataSubmitButton, ColorStateList.valueOf(Color.parseColor("#03A9F4")))
                }
        }
    }
}