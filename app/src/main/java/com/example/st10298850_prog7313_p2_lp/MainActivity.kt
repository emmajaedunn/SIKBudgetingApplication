package com.example.st10298850_prog7313_p2_lp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityMainBinding
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(this)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.signUpTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginUser() {
        val email = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // User logged in through Firebase
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // Now fetch the Room user by email
                    lifecycleScope.launch {
                        val user = database.userDao().getUserByEmail(email)
                        if (user != null) {
                            UserSessionManager.saveUserId(this@MainActivity, user.userId)

                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "User not found locally", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

}


//--------------------------------------REFERENCES----------------------------------------
//1.	Android Developers. (n.d.). Activities – Overview of activity lifecycle and usage.
//•	Source: Android Developers (developer.android.com) - https://developer.android.com/guide/components/activities/intro
//2.	Android Developers. (n.d.). View Binding – Efficient way to access views in code.
//•	Source: Android Developers - https://developer.android.com/topic/binding/view-binding
//3.	Google. (n.d.). Jetpack Navigation Component – Handling in-app navigation and fragments.
//•	Source: Android Developers - https://developer.android.com/guide/navigation/navigation-getting-started
//4.	Android Developers. (n.d.). Saved Instance State – Preserving UI state during configuration changes.
//•	Source: Android Developers - https://developer.android.com/topic/libraries/architecture/saving-state
//5.	Google. (n.d.). Room Persistence Library – Database interaction in Android apps.
//8.	MindOrks. (2023). Android Activity Lifecycle Explained – Deep dive into lifecycle callbacks and best practices.
//•	Source: MindOrks Blog - https://medium.com/mindorks/android-activity-lifecycle-explained-4265754e2584
//9.	Medium. (2022). Understanding ViewModels in Android – Using ViewModels to retain UI-related data.
//•	Source: Android Developers on Medium - https://stackoverflow.com/questions/59855621/understanding-viewmodels-in-android
//10.	Stack Overflow. (varied years). Threads on common MainActivity issues, such as navigation crashes, null pointer exceptions in binding, or lifecycle bugs.
//•	Example search: “NullPointerException in MainActivity Android Kotlin site:stackoverflow.com”
//--------------------------------------------------------------------------------------