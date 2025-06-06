package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish() // Takes user back to the previous screen
        }

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Get the currently logged-in user's UID
        val uid = auth.currentUser?.uid
        Log.d("ProfileActivity", "Firebase UID: $uid")

        if (uid != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)

            userRef.get().addOnSuccessListener { snapshot ->
                val name = snapshot.child("name").value?.toString() ?: "N/A"
                val username = snapshot.child("username").value?.toString() ?: "N/A"
                val email = snapshot.child("email").value?.toString() ?: "N/A"

                // Set user information on screen
                binding.tvName.text = name
                binding.tvUsername.text = "@$username"
                binding.tvEmail.text = email

                // Masked placeholder for current password
                binding.tvCurrentPassword.text = "********"

            }.addOnFailureListener {
                Log.e("ProfileActivity", "Firebase data fetch failed", it)
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.w("ProfileActivity", "No Firebase user signed in.")
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        // Toggle visibility of new password input
        binding.btnTogglePasswordInput.setOnClickListener {
            val isHidden = binding.etNewPassword.visibility == android.view.View.GONE

            binding.etNewPassword.visibility = if (isHidden) android.view.View.VISIBLE else android.view.View.GONE
            binding.btnChangePassword.visibility = if (isHidden) android.view.View.VISIBLE else android.view.View.GONE
        }

        // Handle password change
        binding.btnChangePassword.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString().trim()

            if (newPassword.length >= 6) {
                auth.currentUser?.updatePassword(newPassword)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                            binding.etNewPassword.setText("")
                            binding.etNewPassword.visibility = android.view.View.GONE
                            binding.btnChangePassword.visibility = android.view.View.GONE
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            Log.e("ProfileActivity", "Password update failed", task.exception)
                        }
                    }
            } else {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
