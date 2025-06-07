package com.example.st10298850_prog7313_p2_lp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivitySettingsBinding
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {

    // View binding for accessing layout elements
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the settings layout using ViewBinding
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup bottom navigation bar and button click listeners
        setupUI()
        setupClickListeners()
    }

     // Handles bottom navigation bar interactions.
     // Redirects user to selected screen.
    private fun setupUI() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java)) // Navigate to Home
                    true
                }
                R.id.navigation_stats -> {
                    startActivity(Intent(this, StatsActivity::class.java)) // Navigate to Stats
                    true
                }
                R.id.navigation_add -> {
                    startActivity(Intent(this, AddTransactionActivity::class.java)) // Navigate to Add Transaction
                    true
                }
                R.id.navigation_budget -> {
                    startActivity(Intent(this, TransactionHistoryActivity::class.java)) // Navigate to Budget/History
                    true
                }
                R.id.navigation_settings -> {
                    // Already on Settings screen, do nothing
                    true
                }
                else -> false
            }
        }
    }

    // Attaches click listeners to each settings option button.
    private fun setupClickListeners() {
        // Navigate to Profile screen
        val profileButton = findViewById<ImageButton>(R.id.btnProfile)
        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Navigate to Manage Categories screen
        binding.btnTransactionCategories.setOnClickListener {
            startActivity(Intent(this, ManageCategoriesActivity::class.java))
        }

        // Navigate to Manage Accounts screen
        binding.btnManageAccounts.setOnClickListener {
            startActivity(Intent(this, ManageAccountsActivity::class.java))
        }

        // Navigate to Achievements screen
        binding.btnAchievements.setOnClickListener {
            startActivity(Intent(this, AchievementsActivity::class.java))
        }

        // Navigate to Help & Support screen
        binding.btnHelpSupport.setOnClickListener {
            startActivity(Intent(this, HelpSupportActivity::class.java))
        }

        // Log out and go back to the Main/Login screen
        binding.btnSignOut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear backstack
            startActivity(intent)
            finish()
        }
    }
}
