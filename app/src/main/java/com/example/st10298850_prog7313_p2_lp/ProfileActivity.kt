package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    // ViewBinding to access layout views
    private lateinit var binding: ActivityProfileBinding

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using ViewBinding
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the back button to return to the previous screen
        binding.btnBack.setOnClickListener {
            finish() // Close this activity and go back
        }

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Get currently logged-in user's UID
        val uid = auth.currentUser?.uid
        Log.d("ProfileActivity", "Firebase UID: $uid")

        if (uid != null) {
            // Get a reference to the user's node in Firebase Realtime Database
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)

            // Fetch the user data from Firebase
            userRef.get().addOnSuccessListener { snapshot ->
                // Retrieve values safely from snapshot
                val name = snapshot.child("name").value?.toString() ?: "N/A"
                val username = snapshot.child("username").value?.toString() ?: "N/A"
                val email = snapshot.child("email").value?.toString() ?: "N/A"

                // Populate the UI fields
                binding.tvName.text = name
                binding.tvUsername.text = "@$username"
                binding.tvEmail.text = email

                // Show placeholder for current password (for security, it's not retrieved)
                binding.tvCurrentPassword.text = "********"

            }.addOnFailureListener {
                // Handle Firebase read error
                Log.e("ProfileActivity", "Firebase data fetch failed", it)
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        } else {
            // If no user is logged in, notify the user
            Log.w("ProfileActivity", "No Firebase user signed in.")
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        // Toggle visibility of the new password input field and button
        binding.btnTogglePasswordInput.setOnClickListener {
            val isHidden = binding.etNewPassword.visibility == android.view.View.GONE

            binding.etNewPassword.visibility = if (isHidden) android.view.View.VISIBLE else android.view.View.GONE
            binding.btnChangePassword.visibility = if (isHidden) android.view.View.VISIBLE else android.view.View.GONE
        }

        // Handle password change when user presses "Confirm Password Change"
        binding.btnChangePassword.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString().trim()

            if (newPassword.length >= 6) {
                // Update the Firebase user's password
                auth.currentUser?.updatePassword(newPassword)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()

                            // Clear and hide the input
                            binding.etNewPassword.setText("")
                            binding.etNewPassword.visibility = android.view.View.GONE
                            binding.btnChangePassword.visibility = android.view.View.GONE
                        } else {
                            // Show error if update fails
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
