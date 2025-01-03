package com.BCU.testingapplication

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.BCU.testingapplication.databinding.MyworksCardBinding
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class MyWorksViewInMainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_works_view_in_main)

        val gridView: GridView = findViewById(R.id.gridCardStorage)
        val logOutButton = findViewById<Button>(R.id.logoutButton)

        val userNamespaceView = findViewById<TextView>(R.id.userName)
        val byteDatapaceView = findViewById<TextView>(R.id.remainMiles)
        val userData = UserData()

        // 유저 데이터를 받아오는 콜백
        userData.getUserData { user ->
            if (user != null) {
                userNamespaceView.text=user.displayName
                byteDatapaceView.text=String.format("%.0f", user.byte) + getString(R.string.byte_coin)
            } else {
                //Error
            }
        }

        // 카드 데이터 생성
        val items = listOf(
            GridItem(
                R.drawable.gpt_title,
                getString(R.string.title_gpt),
                getString(R.string.subtitle_gpt)
            ),
            GridItem(
                R.drawable.airplane,
                getString(R.string.localize_title),
                getString(R.string.localize_subtitle)
            ),
        )

        // 어댑터 설정
        val adapter = GridAdapter(this, items)
        gridView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // GridView 아이템 클릭 리스너 추가
        gridView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = items[position]

            // 다른 액티비티로 이동
            when (selectedItem.title) {
                getString(R.string.title_gpt) -> {
                    val intent = Intent(this, GPTSimpleAnswerActivity::class.java)
                    intent.putExtra("data", selectedItem.title)
                    startActivity(intent)
                }
                getString(R.string.localize_title) -> {
                    val intent = Intent(this, LocalizeShowActivity::class.java)
                    intent.putExtra("data", selectedItem.title)
                    startActivity(intent)
                }
            }
        }

        // LogOut 뷰 리스너 추가
        logOutButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.logout_str))
                .setMessage(getString(R.string.logout_warn))
                .setPositiveButton(getString(R.string.ans_yes),
                    DialogInterface.OnClickListener { dialog, id ->
                        FirebaseAuth.getInstance().signOut()

                        Toast.makeText(applicationContext, R.string.signout_successful, Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    })
                .setNegativeButton(getString(R.string.ans_no),
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            builder.show()
        }
    }
}