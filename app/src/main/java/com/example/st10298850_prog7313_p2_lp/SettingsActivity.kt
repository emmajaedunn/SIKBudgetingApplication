package com.example.st10298850_prog7313_p2_lp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivitySettingsBinding
import com.example.st10298850_prog7313_p2_lp.HelpSupportActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupClickListeners()
    }

    /**
     * Sets up the bottom navigation bar.
     */
    private fun setupUI() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_stats -> {
                    startActivity(Intent(this, StatsActivity::class.java))
                    true
                }
                R.id.navigation_add -> {
                    startActivity(Intent(this, AddTransactionActivity::class.java))
                    true
                }
                R.id.navigation_budget -> {
                    startActivity(Intent(this, TransactionHistoryActivity::class.java))
                    true
                }
                R.id.navigation_settings -> {
                    // Already on settings screen
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Sets up click listeners for settings buttons.
     */
    private fun setupClickListeners() {
        // Manage Categories
        binding.btnTransactionCategories.setOnClickListener {
            startActivity(Intent(this, ManageCategoriesActivity::class.java))
        }

        // Manage Accounts
        binding.btnManageAccounts.setOnClickListener {
            startActivity(Intent(this, ManageAccountsActivity::class.java))
        }

        // View Achievements
        binding.btnAchievements.setOnClickListener {
            startActivity(Intent(this, AchievementsActivity::class.java))
        }

        // Help & Support (if implemented)
        binding.btnHelpSupport.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, HelpSupportActivity::class.java)) // Correct
        }


        // Sign out
        binding.btnSignOut?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
