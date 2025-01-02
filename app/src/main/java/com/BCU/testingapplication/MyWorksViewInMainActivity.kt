package com.BCU.testingapplication

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.BCU.testingapplication.databinding.MyworksCardBinding

class MyWorksViewInMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_works_view_in_main)

        val gridView: GridView = findViewById(R.id.gridCardStorage)

        // 샘플 데이터 생성
        val items = listOf(
            GridItem(R.drawable.gpt_title, getString(R.string.title_gpt),getString(R.string.subtitle_gpt)),
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
                getString(R.string.title_gpt)-> {
                    val intent = Intent(this, GPTSimpleAnswerActivity::class.java)
                    intent.putExtra("data", selectedItem.title)
                    startActivity(intent)
                }
            }
        }
    }

}