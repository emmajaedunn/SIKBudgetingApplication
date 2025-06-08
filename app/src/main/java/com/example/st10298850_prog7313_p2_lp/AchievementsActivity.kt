package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityAchievementsBinding
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.content.Intent


class AchievementsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAchievementsBinding
    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val userId = getCurrentUserId()
        lifecycleScope.launch {
            val count = withContext(Dispatchers.IO) {
                database.transactionDao().getTransactionCountForUser(userId)
            }
            val progress = (count * 25).coerceAtMost(100)
            updateProgress(progress)
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

    private fun updateProgress(progress: Int) {
        binding.progressBarAchievement.progress = progress

        val levelLabel = when {
            progress >= 100 -> "Platinum"
            progress >= 75 -> "Gold"
            progress >= 50 -> "Silver"
            progress >= 25 -> "Bronze"
            else -> "Starter"
        }
        binding.tvLevelLabel.text = "Level: $levelLabel"

        updateBadge(binding.badgeStarter, progress >= 0)
        updateBadge(binding.badgeBronze, progress >= 25)
        updateBadge(binding.badgeSilver, progress >= 50)
        updateBadge(binding.badgeGold, progress >= 75)
        updateBadge(binding.badgePlatinum, progress >= 100)
    }

    private fun updateBadge(badge: ImageView, unlocked: Boolean) {
        badge.alpha = if (unlocked) 1.0f else 0.3f
    }
}
