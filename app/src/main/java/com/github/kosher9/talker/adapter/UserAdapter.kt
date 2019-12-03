package com.github.kosher9.talker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.kosher9.talker.MessageActivity
import com.github.kosher9.talker.R
import com.github.kosher9.talker.model.Chat
import com.github.kosher9.talker.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

//private lateinit var theLastMessage: String

class UserAdapter(val context: Context, val users: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImageView = itemView.findViewById<ImageView>(R.id.profile_image)
        val usernameView = itemView.findViewById<TextView>(R.id.username)
//        val lastMsg = itemView.findViewById<TextView>(R.id.last_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(context)
                .inflate(R.layout.user_item, parent, false)
        return ViewHolder(viewItem)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.usernameView.text = user.username
        Glide.with(context).load(user.profilUrl).into(holder.userImageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("userid", user.id)
            context.startActivity(intent)
        }

    }

/*
    private fun lastMessage(userId: String, lastMsg: TextView){
        theLastMessage = "default"
        val fuser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Chats")

        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                return
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children){
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat?.receiver.equals(fuser?.uid) && chat?.sender.equals(userId) ||
                            chat?.receiver.equals(userId) && chat?.sender.equals(fuser?.uid)){
                        theLastMessage = chat!!.message
                    }
                }

                when (theLastMessage){
                    "default" -> lastMsg.text = "....."
                    else -> lastMsg.text = theLastMessage
                }
                theLastMessage = "default"
            }

        })
    }
*/

}
