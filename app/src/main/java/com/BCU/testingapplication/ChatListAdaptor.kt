package com.BCU.testingapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateFormat
import java.util.Calendar

class ChatListAdaptor(private val items: MutableList<message>) : RecyclerView.Adapter<ChatListAdaptor.ChatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_RIGHT) {
            R.layout.chatting_bubble_right
        } else {
            R.layout.chatting_bubble_left
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = items[position]
        holder.headImageAvatar.setImageResource(item.chatAvatar)
        holder.titleNameOfUser.text = item.chatName
        holder.textOfUserChat.text = item.chatText
        
        // 현재 시간을 넣어줌.
        val currentTime = getCurrentTime()
        holder.textTime.text = currentTime
    }

    override fun getItemCount(): Int = items.size
    override fun getItemViewType(position: Int): Int {
        return if (items[position].isUser) VIEW_TYPE_RIGHT else VIEW_TYPE_LEFT
    }

    // 현재 시간 구하기
    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        return DateFormat.format("hh:mm a", calendar).toString() // 예: 12:30 PM
    }

    fun addMessage(newMessage: message) {
        items.add(newMessage)
        notifyItemInserted(items.size - 1)
    }
    fun getAllMessagesAsString(): String {
        return items.joinToString(separator = "\n") { message ->
            "${if (message.isUser) "User" else "ChatGPT"}: ${message.chatText}"
        }
    }

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headImageAvatar: ImageView = view.findViewById(R.id.itemImage)
        val titleNameOfUser: TextView = view.findViewById(R.id.itemTitle)
        val textOfUserChat: TextView = view.findViewById(R.id.itemSubTitle)
        val textTime: TextView = view.findViewById(R.id.timeDate)
    }
    companion object {
        const val VIEW_TYPE_LEFT = 0
        const val VIEW_TYPE_RIGHT = 1
    }
}

data class message(val chatAvatar: Int, val chatName: String,val chatText:String,val isUser: Boolean)

