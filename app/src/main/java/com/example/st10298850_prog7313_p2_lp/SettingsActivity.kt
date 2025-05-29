package com.example.st10298850_prog7313_p2_lp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivitySettingsBinding

/**
 * SettingsActivity provides user interface for app settings and navigation to other activities.
 */
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
     * Sets up the user interface components, including the bottom navigation.
     */
    private fun setupUI() {
        // Setup bottom navigation
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
                    // Already on settings
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Sets up click listeners for various settings options.
     */
    private fun setupClickListeners() {
        // Navigate to Manage Categories
        binding.btnTransactionCategories.setOnClickListener {
            startActivity(Intent(this, ManageCategoriesActivity::class.java))
        }

        // Navigate to Manage Accounts
        binding.btnManageAccounts.setOnClickListener {
            startActivity(Intent(this, ManageAccountsActivity::class.java))
        }

        // Navigate to Achievements
        binding.btnAchievements.setOnClickListener {
            startActivity(Intent(this, AchievementsActivity::class.java))
        }
    }
}
