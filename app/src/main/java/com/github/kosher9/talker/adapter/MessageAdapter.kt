package com.github.kosher9.talker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.kosher9.talker.R
import com.github.kosher9.talker.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

private lateinit var firebaseUser: FirebaseUser

class MessageAdapter(val context: Context, val chats: List<Chat>, val imageUrl: String) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val showMessage = itemView.findViewById<TextView>(R.id.show_message)
        val imageProfil = itemView.findViewById<CircleImageView>(R.id.profile_image_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MSG_TYPE_LEFT) {
            val viewItem = LayoutInflater.from(context)
                    .inflate(R.layout.chat_item_left, parent, false)
            return ViewHolder(viewItem)
        } else {
            val viewItem = LayoutInflater.from(context)
                    .inflate(R.layout.chat_item_right, parent, false)
            return ViewHolder(viewItem)
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        holder.showMessage.text = chat.message
        Glide.with(context).load(imageUrl).into(holder.imageProfil)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        return if (chats[position].sender == firebaseUser.uid) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    companion object {
        const val MSG_TYPE_LEFT = 0
        const val MSG_TYPE_RIGHT = 1
    }

}