package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivitySettingsBinding
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent

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
        // TODO: Set current date if needed

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
                    // Already on settings, do nothing
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
        // Navigate to ManageCategoriesActivity when Transaction Categories button is clicked
        binding.btnTransactionCategories.setOnClickListener {
            val intent = Intent(this, ManageCategoriesActivity::class.java)
            startActivity(intent)
        }

        // Navigate to ManageAccountsActivity when Manage Accounts button is clicked
        binding.btnManageAccounts.setOnClickListener {
            val intent = Intent(this, ManageAccountsActivity::class.java)
            startActivity(intent)
        }

        // TODO: Add more click listeners for other settings options
    }
}