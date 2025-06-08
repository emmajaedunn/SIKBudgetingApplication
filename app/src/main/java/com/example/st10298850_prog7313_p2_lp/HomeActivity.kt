package com.example.st10298850_prog7313_p2_lp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityHomeBinding
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val database by lazy { AppDatabase.getDatabase(this) }
    private var currentUserId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = getCurrentUserId()
        if (currentUserId == -1L) return

        // Load user name once
        loadUserName(currentUserId)

        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        // Refresh achievement progress on each resume
        if (currentUserId != -1L) {
            lifecycleScope.launch {
                val transactionCount = withContext(Dispatchers.IO) {
                    database.transactionDao().getTransactionCountForUser(currentUserId)
                }
                val progress = (transactionCount * 25).coerceAtMost(100)
                updateAchievementUI(progress)
            }
        }
    }

    private fun getCurrentUserId(): Long {
        val userId = UserSessionManager.getUserId(this)
        if (userId == -1L) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return userId
    }

    private fun loadUserName(userId: Long) {
        lifecycleScope.launch {
            val userName = withContext(Dispatchers.IO) {
                database.userDao().getUserById(userId)?.username ?: "User"
            }
            binding.tvWelcome.text = "Welcome, $userName"
        }
    }

    private fun updateAchievementUI(progress: Int) {
        binding.progressAchievement.progress = progress

        val levelLabel = when {
            progress >= 100 -> "Platinum"
            progress >= 75 -> "Gold"
            progress >= 50 -> "Silver"
            progress >= 25 -> "Bronze"
            else -> "Starter"
        }

        binding.tvUserTitle.text = levelLabel

        val badgeResId = when (levelLabel) {
            "Platinum" -> R.drawable.badge_5
            "Gold" -> R.drawable.badge_4
            "Silver" -> R.drawable.badge_3
            "Bronze" -> R.drawable.badge_2
            else -> R.drawable.badge_1
        }
        binding.imgRecentBadge.setImageResource(badgeResId)
    }

    private fun setupClickListeners() {
        binding.btnAddTransaction.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        binding.btnManageBudgets.setOnClickListener {
            startActivity(Intent(this, TransactionHistoryActivity::class.java))
        }

        binding.btnAnalytics.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
