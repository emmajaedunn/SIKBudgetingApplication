package com.example.st10298850_prog7313_p2_lp

import android.app.*
import android.content.*
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.example.st10298850_prog7313_p2_lp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val notificationChannelId = "default_channel"
    private val notificationPermissionCode = 1001
    private val prefsName = "notification_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()
        setupUI()
        setupClickListeners()
        setupNotificationSwitch()
    }

    private fun setupUI() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val destination = when (item.itemId) {
                R.id.navigation_home -> HomeActivity::class.java
                R.id.navigation_stats -> StatsActivity::class.java
                R.id.navigation_add -> AddTransactionActivity::class.java
                R.id.navigation_budget -> TransactionHistoryActivity::class.java
                R.id.navigation_settings -> SettingsActivity::class.java
                else -> null
            }
            destination?.let {
                startActivity(Intent(this, it))
                true
            } ?: false
        }
    }

    private fun setupClickListeners() {
        findViewById<ImageButton>(R.id.btnProfile)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.btnTransactionCategories.setOnClickListener {
            startActivity(Intent(this, ManageCategoriesActivity::class.java))
        }
        binding.btnManageAccounts.setOnClickListener {
            startActivity(Intent(this, ManageAccountsActivity::class.java))
        }
        binding.btnAchievements.setOnClickListener {
            startActivity(Intent(this, AchievementsActivity::class.java))
        }
        binding.btnHelpSupport.setOnClickListener {
            startActivity(Intent(this, HelpSupportActivity::class.java))
        }
        binding.btnBudgetingGoals.setOnClickListener {
            startActivity(Intent(this, BudgetGoalActivity::class.java))
        }
        binding.btnMultiCurrency.setOnClickListener {
            startActivity(Intent(this, MultiCurrencyActivity::class.java))
        }
        binding.btnSignOut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        binding.btnViewProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun setupNotificationSwitch() {
        val prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val savedState = prefs.getBoolean("notifications_enabled", false)
        binding.switchNotifications.isChecked = savedState

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            with(prefs.edit()) {
                putBoolean("notifications_enabled", isChecked)
                apply()
            }

            if (isChecked) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    scheduleRepeatingNotification()
                    Toast.makeText(this, "Notifications enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Grant notification permission to enable this feature.", Toast.LENGTH_LONG).show()
                    binding.switchNotifications.isChecked = false
                }
            } else {
                cancelRepeatingNotification()
                Toast.makeText(this, "Notifications turned off", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scheduleRepeatingNotification() {
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intervalMillis = 6 * 60 * 60 * 1000L // 6 hours
        val startTime = System.currentTimeMillis() + 5000L

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startTime,
            intervalMillis,
            pendingIntent
        )
    }

    private fun cancelRepeatingNotification() {
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

        private fun requestNotificationPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                        notificationPermissionCode
                    )
                }
            }
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied. Notifications disabled.", Toast.LENGTH_LONG).show()
                binding.switchNotifications.isChecked = false
            }
        }
    }
}
