package com.github.kosher9.talker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.arch.core.executor.TaskExecutor
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class CodeCheck : AppCompatActivity() {

    private lateinit var verificationCode: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var codeEdit: EditText
    private lateinit var valiButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_check)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Code Check"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        codeEdit = findViewById(R.id.edit_code)
        valiButton = findViewById(R.id.button_valider)

        mAuth = FirebaseAuth.getInstance()
        val numTel = intent.getStringExtra("numTel")
        sendVerification(numTel)

        valiButton.setOnClickListener {
            if (codeEdit.text.isEmpty() || codeEdit.text.length < 6){
                codeEdit.error = "Enter code"
                codeEdit.requestFocus()
                return@setOnClickListener
            }
            verifyCode(codeEdit.text.toString())
        }

    }

    private val mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

        override fun onCodeSent(s: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(s, token)
            verificationCode = s
        }

        override fun onVerificationCompleted(auth: PhoneAuthCredential) {
            val code = auth.smsCode
            if(code != null){
                codeEdit.setText(code)
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun sendVerification(number: String){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallBack
        )

    }

    private fun verifyCode(code: String){
        val credential = PhoneAuthProvider.getCredential(verificationCode, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener {

            if (it.isSuccessful){
                val intent = Intent(this@CodeCheck, UserPersInfoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext,it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}
