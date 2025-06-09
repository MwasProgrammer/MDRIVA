package com.example.mdriva_v1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mdriva_v1.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("users")

        // Set up signup button click listener
        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val username = binding.signupUsername.text.toString()
            val password = binding.signupPassword.text.toString()

            // Validate input and invoke signupUser function
            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                signupUser(email, username, password)
            } else {
                Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up login redirection
        binding.signupRedirection.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
            finish()
        }


    }

    //signup user function
    private fun signupUser(email: String, username: String, password: String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    val id = databaseReference.push().key
                    val userData = UserData(id, username, email, password)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@SignupActivity, "Signup successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SignupActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@SignupActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}