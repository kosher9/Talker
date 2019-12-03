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
import com.github.kosher9.talker.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ContactsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var userAdapter: UserAdapter
    private lateinit var mUsers: MutableList<User>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contacts, container, false)
        recyclerView = view.findViewById(R.id.recycleview_contact)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        mUsers = mutableListOf()
        readContacts()
        return view
    }

    private fun readContacts(){
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val reference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                    return
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUsers.clear()
                for (snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(User::class.java)!!
                    if (user.id != firebaseUser.uid){
                        mUsers.add(user)
                    }
                }
                userAdapter = UserAdapter(context!!, mUsers)
                recyclerView.adapter = userAdapter
            }

        })
    }


}
