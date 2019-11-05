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
import com.github.kosher9.talker.model.User
import kotlinx.android.synthetic.main.user_item.view.*

class UserAdapter(val context: Context, val users: List<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userImageView = itemView.findViewById<ImageView>(R.id.profile_image)
        val usernameView = itemView.findViewById<TextView>(R.id.username)
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
        if (user.imageUrl.equals("default")){
            holder.userImageView.setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(context).load(user.imageUrl).into(holder.userImageView)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("userid", user.id)
            context.startActivity(intent)
        }

    }

}
