package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.data.User
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    // ViewBinding for layout access
    private lateinit var binding: ActivityRegisterBinding

    // Local Room database instance
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Room database
        database = AppDatabase.getDatabase(this)

        // Navigate back to login if user clicks the login text
        binding.loginTextView.setOnClickListener {
            finish()
        }

        // Register button click listener
        binding.registerButton.setOnClickListener {
            registerUser()
        }
    }

    /**
     * Handles full user registration:
     * 1. Validates input
     * 2. Checks for existing username in Room DB
     * 3. Inserts user into Room DB
     * 4. Registers user in Firebase Auth
     * 5. Saves additional user info in Firebase Realtime DB
     */
    private fun registerUser() {
        val name = binding.nameEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        // Validate input first
        if (validateInput(name, username, email, password)) {
            lifecycleScope.launch {
                // Check if username already exists locally
                val existingUser = database.userDao().getUserByUsername(username)

                if (existingUser == null) {
                    val auth = FirebaseAuth.getInstance()
                    val firebaseDB = FirebaseDatabase.getInstance().getReference("users")

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val uid = auth.currentUser?.uid
                                val userData = mapOf(
                                    "name" to name,
                                    "username" to username,
                                    "email" to email
                                )
                                if (uid != null) {
                                    firebaseDB.child(uid).setValue(userData)
                                }

                                // NOW insert into Room
                                lifecycleScope.launch {
                                    val newUser = User(name = name, username = username, email = email, password = password)
                                    database.userDao().insertUser(newUser)
                                }

                                Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this@RegisterActivity, "Firebase error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Toast.makeText(this@RegisterActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    /**
     * Validates input fields before registration:
     * - Checks for empty fields
     * - Valid email format
     * - Strong password
     */
    private fun validateInput(name: String, username: String, email: String, password: String): Boolean {
        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isPasswordStrong(password)) {
            Toast.makeText(
                this,
                "Password must be at least 8 characters, include 1 uppercase, 1 number & 1 special character",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    /**
     * Ensures the password has:
     * - At least 8 characters
     * - At least 1 number
     * - At least 1 uppercase letter
     * - At least 1 special character
     */
    private fun isPasswordStrong(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$".toRegex()
        return passwordPattern.matches(password)
    }
}
