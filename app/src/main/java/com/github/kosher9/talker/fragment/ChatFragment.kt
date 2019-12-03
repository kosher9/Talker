package com.github.kosher9.talker.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.github.kosher9.talker.R
import com.github.kosher9.talker.adapter.UserAdapter
import com.github.kosher9.talker.model.Chat
import com.github.kosher9.talker.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: MutableList<String>
    private lateinit var users: MutableList<User>

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_chat, container, false)
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        recyclerView = view!!.findViewById(R.id.recycler_view_chat)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        userList = mutableListOf()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                return
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()

                for (snapshot in dataSnapshot.children){
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat?.sender.equals(firebaseUser.uid)){
                        userList.add(chat!!.receiver)
                    }
                    if (chat?.receiver.equals(firebaseUser.uid)){
                        userList.add(chat!!.sender)
                    }
                }

                readChats()

            }

        })
        return view
    }

    private fun readChats() {
        users = mutableListOf()

        reference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                return
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()

                for (snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(User::class.java)!!
                    for (id in userList){
                        if (user.id == id && !users.contains(user)){
                            /*if (users.size != 0){
                                for (user1 in users){
                                    if (!user.id.equals(user1.id)){
                                        users.add(user)
                                    }
                                }
                            } else {*/

                            users.add(user)
//                            }
                        }
                    }
                }
                userAdapter = UserAdapter(context!!, users)
                recyclerView.adapter = userAdapter
            }

        })
    }

}
