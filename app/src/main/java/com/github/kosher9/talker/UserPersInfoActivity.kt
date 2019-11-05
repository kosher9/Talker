package com.github.kosher9.talker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class UserPersInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pers_info)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Compte"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)

        val startButton = findViewById<Button>(R.id.button_start)

        startButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
