package com.BCU.testingapplication

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BuildCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.BuildConfig
import org.w3c.dom.Text


class GPTSimpleAnswerActivity : AppCompatActivity() {

    // 아래 함수에서 사용하기 위해 먼저 선언. 초기화는 onCreate 이후 진행한다.
    private lateinit var chatAdapter: ChatListAdaptor
    private lateinit var userPromptInput: TextInputEditText
    private lateinit var userPromptInputHintBox: TextInputLayout
    private lateinit var chatListView: RecyclerView
    private lateinit var buttonSend : Button
    private var isWaiting = false

    // 오픈AI 클라스 선언, GPT 실행 준비
    val openAIClient = OpenAI(com.BCU.testingapplication.BuildConfig.GPT_API_KEY)

    // 유저의 프로필 정보
    val userProfileImage = R.drawable.bcu_project_icon
    val userProfileName = "감자전"


    override fun onCreate(savedInstanceState: Bundle?) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gptsimple_answer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecyclerView 설정 및 기타 xml 요소들
        chatListView = findViewById(R.id.ChatRecycleView)
        buttonSend = findViewById<Button>(R.id.send_button)
        userPromptInput = findViewById(R.id.text_input)
        userPromptInputHintBox = findViewById(R.id.text_inputParent)

        val goBackBtn = findViewById<Button>(R.id.back_button)

        // 오픈AI 클라스 선언
        val openAIClient = OpenAI(com.BCU.testingapplication.BuildConfig.GPT_API_KEY)


        // 리스너 목록 // 버튼의 동작 처리
        // 뒤로가기 버튼
        goBackBtn.setOnClickListener {
            finish()
        }

        // 전송 버튼
        buttonSend.setOnClickListener{
            sendInputToGPT()
        }



        // LinearLayoutManager로 채팅 화면 설정
        chatListView.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true // 최신 메시지가 맨 아래로 표시되도록 설정
        }

        // 채팅 메시지 데이터 준비
        val chatMessages = mutableListOf(
            message(
                chatAvatar = R.drawable.gpt_title,
                chatName = "ChatGPT",
                chatText =getString(R.string.gpt_init_str),
                isUser = false
            )
        )

        // 어댑터 연결
        chatAdapter = ChatListAdaptor(chatMessages)
        chatListView.adapter = chatAdapter


    }
    fun sendInputToGPT(){
        if(isWaiting){
            userPromptInputHintBox.requestFocus()
            Toast.makeText(applicationContext,getString(R.string.gpt_waiting_str),Toast.LENGTH_SHORT).show()
        }else{
            if(userPromptInput.text.toString() == ""){
                userPromptInputHintBox.requestFocus()
                userPromptInputHintBox.hint = getString(R.string.gpt_input_empty_warn)
            }else{
                userPromptInput.setText("")
                // 유저의 질문을 메시지로 생성하는 부분
                isWaiting = true
                buttonSend.translationZ = 0f
                ViewCompat.setBackgroundTintList(buttonSend, ColorStateList.valueOf(Color.parseColor("#737373")))
                val userMessage = message(
                    chatAvatar = userProfileImage,
                    chatName = userProfileName,
                    chatText = userPromptInput.text.toString(),
                    isUser = true
                )
                chatAdapter.addMessage(userMessage)
                chatListView.scrollToPosition(chatAdapter.itemCount - 1)

                // 모든 메시지를 문자열로 모음
                val fullConversation = chatAdapter.getAllMessagesAsString()

                // GPT_API 실행부. 프롬프트를 전송하여 응답을 받아옵니다.
                openAIClient.GPT_excuteCommand(fullConversation,
                    onSuccess = { response ->
                        // 성공 시 처리 로직
                        runOnUiThread {
                            val responseMessage = message(
                                chatAvatar = R.drawable.gpt_title,
                                chatName = "ChatGPT",
                                chatText = response,
                                isUser = false
                            )
                            chatAdapter.addMessage(responseMessage)

                            // RecyclerView 스크롤을 최신 메시지로 이동
                            chatListView.scrollToPosition(chatAdapter.itemCount - 1)
                            isWaiting = false
                            buttonSend.translationZ = 6f
                            ViewCompat.setBackgroundTintList(buttonSend, ColorStateList.valueOf(Color.parseColor("#03A9F4")))
                        } },
                    onError = { error ->
                        // 실패 시 처리 로직
                        runOnUiThread {
                            Toast.makeText(applicationContext,getString(R.string.gpt_error),Toast.LENGTH_LONG).show()
                        }
                        isWaiting = false
                        buttonSend.translationZ = 6f
                        ViewCompat.setBackgroundTintList(buttonSend, ColorStateList.valueOf(Color.parseColor("#03A9F4")))
                    })
            }
        }
    }
}