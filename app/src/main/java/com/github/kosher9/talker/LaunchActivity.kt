package com.github.kosher9.talker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val intent = Intent(this, Registration::class.java)
        startActivity(intent)
        finish()

        /*if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
            finish()
        }*/

    }
}
