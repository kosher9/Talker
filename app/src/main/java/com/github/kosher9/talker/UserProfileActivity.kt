package com.github.kosher9.talker

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.github.kosher9.talker.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class UserProfileActivity : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var fuser: FirebaseUser
    private lateinit var userRel: RelativeLayout
    private lateinit var usertelRel: RelativeLayout
    private lateinit var userProfilPhoto: CircleImageView
    private lateinit var usernameText: TextView
    private lateinit var userTelText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        userRel = findViewById(R.id.username_rel)
        usertelRel = findViewById(R.id.usertel_rel)
        usernameText = findViewById(R.id.username_p)
        userTelText = findViewById(R.id.usertel)
        userProfilPhoto = findViewById(R.id.user_profile_photo)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_profil)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        userRel.setOnClickListener {
            startActivity(Intent(this, ModifUsernameActivity::class.java))
        }

        usertelRel.setOnClickListener {
            Toast.makeText(this, "Vous ne pouvez pas encore modifier votre numero de téléphone", Toast.LENGTH_LONG).show()
        }

        userProfilPhoto.setOnClickListener {

        }

        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        fuser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.uid)

        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                usernameText.text = user?.username
                userTelText.text = user?.phone
                Glide.with(this@UserProfileActivity).load(user?.profilUrl).into(userProfilPhoto)
            }

        })


    }
}
