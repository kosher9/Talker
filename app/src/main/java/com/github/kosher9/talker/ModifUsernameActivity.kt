package com.github.kosher9.talker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.kosher9.talker.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ModifUsernameActivity : AppCompatActivity() {

    private lateinit var usernameEdit: EditText
    private lateinit var validerButton: Button
    private lateinit var reference: DatabaseReference
    private lateinit var fuser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modif_username)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_modif_profil)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        usernameEdit = findViewById(R.id.username_p_modif)
        validerButton = findViewById(R.id.modif_valid_but)

        fuser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.uid)

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {
                return
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                usernameEdit.setText(user?.username)
            }

        })

        validerButton.setOnClickListener {
            val username = usernameEdit.text.toString()
            if (username == ""){
                Toast.makeText(this, "Ce champ ne peut être vide", Toast.LENGTH_LONG).show()
            } else {
                val map = hashMapOf<String, String>()
                map["username"] = username
                reference.updateChildren(map as Map<String, Any>).addOnCompleteListener {
                    startActivity(Intent(this, UserProfileActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
            }
        }

    }
}
