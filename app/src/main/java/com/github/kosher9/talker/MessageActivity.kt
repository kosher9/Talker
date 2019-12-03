package com.github.kosher9.talker

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.kosher9.talker.adapter.MessageAdapter
import com.github.kosher9.talker.fragment.ContactsFragment
import com.github.kosher9.talker.model.Chat
import com.github.kosher9.talker.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class MessageActivity : AppCompatActivity() {

    private lateinit var profileImage: CircleImageView
    private lateinit var userNameText: TextView
    private lateinit var fuser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var btnSend: ImageButton
    private lateinit var message: EditText
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var mChat: MutableList<Chat>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recycler_view_chat)
        recyclerView.setHasFixedSize(true)
        val linearLayout = LinearLayoutManager(applicationContext)
        linearLayout.stackFromEnd = true
        recyclerView.layoutManager = linearLayout

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        profileImage = findViewById(R.id.profile_image_bar)
        userNameText = findViewById(R.id.username)
        message = findViewById(R.id.text_send)
        btnSend = findViewById(R.id.btn_send)
        val intent = intent
        val userId = intent.getStringExtra("userid")

        fuser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        btnSend.setOnClickListener {
            val theMessage = message.text.toString()
            if (theMessage != ""){
                sendMessage(fuser.uid, userId, theMessage)
                message.text.clear()
            }
        }

        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(dataSnapshot: DatabaseError) {
                return
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                userNameText.text = user?.username
                Glide.with(this@MessageActivity).load(user?.profilUrl).into(profileImage)
                readMessage(fuser.uid, userId, user!!.profilUrl)
            }

        })

    }

    private fun sendMessage(sender: String, receiver: String, message: String){
        val reference = FirebaseDatabase.getInstance().reference
        val hashMap = hashMapOf<String, Any>()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["message"] = message

        reference.child("Chats").push().setValue(hashMap)
    }

    private fun readMessage(myid: String, userId: String, imageUrl: String){
        mChat = mutableListOf()
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mChat.clear()
                for (snapshot in dataSnapshot.children){
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat!!.receiver == myid && chat.sender == userId ||
                            chat.receiver == userId && chat.sender == myid){
                        mChat.add(chat)
                    }

                    messageAdapter = MessageAdapter(this@MessageActivity, mChat, imageUrl)
                    recyclerView.adapter = messageAdapter
                }
            }

        })
    }

}
