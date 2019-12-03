package com.github.kosher9.talker

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import de.hdodenhof.circleimageview.CircleImageView

class UserPersInfoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var username: TextView
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri
    private lateinit var imageView: CircleImageView
    private lateinit var uploadTask: StorageTask<*>
    private lateinit var fuser: FirebaseUser
    private lateinit var userId: String
    private lateinit var num: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_pers_info)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Compte"
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)

        username = findViewById(R.id.pseudo)
        imageView = findViewById(R.id.imageView)
        num = intent.getStringExtra("numTel")
//        num = "+22890488895"

        fuser = FirebaseAuth.getInstance().currentUser!!
        userId = fuser.uid
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference("images")

        val startButton = findViewById<Button>(R.id.button_start)

        startButton.setOnClickListener {
            uploadImage()
        }

        imageView.setOnClickListener {
            openImage()
        }

    }

    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    private fun getFileExtension(uri: Uri): String {
        val contentResolver = applicationContext.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))!!
    }

    private fun uploadImage(){

        val progessDial = ProgressDialog(this)
        progessDial.setMessage("Traitement en cours...")
        progessDial.show()
        val metadata = StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build()

        val ref = storageReference.child("profile/${imageUri.lastPathSegment}")
        val uploadTask = ref.putFile(imageUri, metadata)

        uploadTask.addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
            println("Upload is $progress% done")
        }.addOnPausedListener {
            println("Upload is paused")
        }.addOnPausedListener {
            //Handle it
        }.addOnSuccessListener {
            uploadTask.continueWithTask {
                if (!it.isSuccessful){
                    it.exception?.let { ex ->
                        throw ex
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful){
                    val profilUri = it.result
                    createUser(username.text.toString(), num, profilUri.toString())
                    progessDial.dismiss()
                }
            }
        }

        /*if (imageUri != null){
            val fileReference = storageReference.child(""+System.currentTimeMillis()+"."+getFileExtension(imageUri))
            uploadTask = fileReference.getFile(imageUri)
            uploadTask.continueWith {
                if (!it.isSuccessful){
                    throw it.exception!!
                }
                return@continueWith fileReference.downloadUrl
            }.addOnSuccessListener {
                if (it.isSuccessful){
                    val downloadUri = it.result
                    val mUri = downloadUri.toString()
                    val map = mutableMapOf<String, Any>()
                    map["imagUri"] = mUri
                    reference.setValue(map)

                }
            }
        }*/
    }

    /*private fun register(username: String, imageUrl: String, number: String){

    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!

            imageView.setImageURI(imageUri)

        }

    }

    private fun createUser(username: String, number: String, imageUri: String) {

        val hashMap = hashMapOf<String, String>()
        hashMap["id"] = userId
        hashMap["username"] = username
        hashMap["phone"] = number
        hashMap["profilUrl"] = imageUri
        reference.setValue(hashMap).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    companion object {
        const val IMAGE_REQUEST = 1
    }

}
