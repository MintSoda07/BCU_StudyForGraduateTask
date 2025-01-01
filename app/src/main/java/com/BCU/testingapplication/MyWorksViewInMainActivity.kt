package com.BCU.testingapplication

import android.os.Bundle
import android.widget.GridView
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
            GridItem(R.drawable.api_test_icon, "API text","text"),
            GridItem(R.drawable.bcu_project_icon, "Item 2","dsadsa"),
            GridItem(R.drawable.api_test_icon, "API text","text"),
            GridItem(R.drawable.bcu_project_icon, "Item 2","dsadsa"),
            GridItem(R.drawable.api_test_icon, "API text","text"),
            GridItem(R.drawable.bcu_project_icon, "Item 2","dsadsa"),
        )

        // 어댑터 설정
        val adapter = GridAdapter(this, items)
        gridView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}