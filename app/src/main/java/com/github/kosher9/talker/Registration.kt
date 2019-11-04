package com.github.kosher9.talker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_registration.*

class Registration : AppCompatActivity() {

    private lateinit var codeSpinner: Spinner
    private lateinit var numText: EditText
    private lateinit var validerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Code Check"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        codeSpinner = findViewById(R.id.cpp)
        numText = findViewById(R.id.edit_num)
        validerButton = findViewById(R.id.button_valider)

        ArrayAdapter.createFromResource(
            this,
            R.array.code_pays,
            android.R.layout.simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            codeSpinner.adapter = it
        }



        validerButton.setOnClickListener {
            val sp = codeSpinner.selectedItem.toString()
            val nut = numText.text.toString()

            val num = "$sp$nut"
            Log.i("RegistrationActivity", "sp : $sp , nut :$nut , num : $num")
            Toast.makeText(this, "$num", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CodeCheck::class.java)
            intent.putExtra("numTel", num)
            startActivity(intent)
        }

    }

    /*override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }*/
}
