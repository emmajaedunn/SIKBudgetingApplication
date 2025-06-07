package com.example.st10298850_prog7313_p2_lp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.st10298850_prog7313_p2_lp.databinding.ActivitySettingsBinding
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {

    // View binding for accessing layout elements
    private lateinit var binding: ActivitySettingsBinding
    private val notificationChannelId = "default_channel"
    private val notificationPermissionCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the settings layout using ViewBinding
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()
        // Setup bottom navigation bar and button click listeners
        setupUI()
        setupClickListeners()

        // Switch starts off
        binding.switchNotifications.isChecked = false

        setupNotificationSwitch()
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
                R.id.navigation_settings -> true
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
        // Budgeting Goals
        binding.btnBudgetingGoals.setOnClickListener {
            startActivity(Intent(this, BudgetGoalActivity::class.java))
        }
        binding.btnMultiCurrency.setOnClickListener {
            startActivity(Intent(this, MultiCurrencyActivity::class.java))


        // Sign out
        binding.btnSignOut?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear backstack
            startActivity(intent)
            finish()
        }
    }

    /**
     * Sets up the Notifications switch .
     */
    private fun setupNotificationSwitch() {
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    sendSystemNotification("Daily Reminder", "Don't forget to track your spending today!")
                } else {
                    Toast.makeText(
                        this,
                        "Please grant notification permission to enable this feature.",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.switchNotifications.isChecked = false
                }
            } else {
                Toast.makeText(this, "Notifications turned off", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Sends a system-level notification.
     */
    private fun sendSystemNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "SIK Budgeting", // updated channel name
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setSmallIcon(R.drawable.notifications_ic)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }

    /**
     * Requests notification permission.
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    notificationPermissionCode
                )
            }
        }
    }

    /**
     * Handles the result.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied. Notifications will be disabled.", Toast.LENGTH_LONG).show()
                binding.switchNotifications.isChecked = false
            }
        }
    }
}
    }}
