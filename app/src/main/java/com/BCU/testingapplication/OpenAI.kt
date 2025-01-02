package com.BCU.testingapplication

import android.widget.TextView
import okhttp3.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import java.util.concurrent.TimeUnit

class OpenAI (private val apiKey: String){
    val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS) //  타임아웃 설정
        .connectTimeout(30, TimeUnit.SECONDS) // 연결 타임아웃 설정
        .build() // HTTP 클라이언트 생성 실행
    private val gson = Gson()
    private val baseUrl = "https://api.openai.com/v1/chat/completions"

    fun sendMessage(
        prompt: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        // API 요청에 필요한 JSON 데이터
        val requestBody = mapOf(
            "model" to "gpt-4o",  // gpt-4o 혹은 gpt-4
            "messages" to listOf(
                mapOf("role" to "system", "content" to "당신은 사용자에게 도움을 주고 정서적 안정을 주는 친구입니다. 대화를 나누어 보세요."),
                mapOf("role" to "user", "content" to prompt)
            ),
            "max_tokens" to 8192
        )

        // RequestBody 생성 (OkHttp 4.x)
        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            gson.toJson(requestBody)
        )

        val request = Request.Builder()
            .url(baseUrl)
            .addHeader("Authorization", "Bearer $apiKey") // API 키
            .post(body) // POST 요청에 body 첨부
            .build()

        // 비동기 요청
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onError("Response not successful: ${response.code}")
                    return
                }

                val responseBody = response.body?.string()
                if (responseBody != null) {
                    try {
                        val jsonResponse = gson.fromJson(responseBody, Map::class.java)
                        val reply = (jsonResponse["choices"] as List<Map<String, Any>>)[0]["message"] as Map<String, String>
                        onSuccess(reply["content"] ?: "No content")
                    } catch (e: Exception) {
                        onError("Error parsing response: ${e.message}")
                    }
                } else {
                    onError("Empty response body")
                }
            }
        })
    }
    fun GPT_excuteCommand(
        userPromptInput: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val promptForGPT = userPromptInput
        this.sendMessage(
            prompt = promptForGPT, // 사용자 입력
            onSuccess = { response ->
                onSuccess(response) // 성공 시 콜백 호출
            },
            onError = { error ->
                onError(error) // 실패 시 콜백 호출
            }
        )
    }
}