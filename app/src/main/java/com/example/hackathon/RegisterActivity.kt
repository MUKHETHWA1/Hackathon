package com.example.hackathon

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hackathon.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    lateinit var firstname: EditText
    lateinit var email: EditText
    lateinit var  username: EditText
    lateinit var password: EditText
    lateinit var btnRegisterNow: Button
    private lateinit var auth: FirebaseAuth // Firebase authentication
    private lateinit var database: FirebaseDatabase // Firebase Realtime Database*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnRegisterNow.setOnClickListener {
            val emailR=binding.etEmail.text.toString()
            val passR=binding.etPasswordRegister.text.toString()
            val username=binding.etUsernameRegister.text.toString()

            val name=binding.etFirstname.text.toString()

            if (emailR.isNotEmpty() && passR.isNotEmpty() && username.isNotEmpty() && name.isNotEmpty()) {
                registerUser(emailR, passR, name, username)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun registerUser(email: String, password: String, name: String, username: String) {
        // Firebase authentication to create a user
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        // Save user data in Firebase Realtime Database
                        val user = User(name, email, username,password)
                        database.reference.child("Users").child(it).setValue(user)
                            .addOnCompleteListener { dataTask ->
                                if (dataTask.isSuccessful) {
                                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                    // Redirect to login or menu screen after successful registration
                                    val intent = Intent(this, LoginActivity::class.java)
                                    intent.putExtra("userName", name)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}