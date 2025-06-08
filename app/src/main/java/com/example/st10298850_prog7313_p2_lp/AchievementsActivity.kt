package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityAchievementsBinding

class AchievementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAchievementsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle back button click
        binding.btnBack.setOnClickListener {
            finish() // Closes the activity and returns to the previous screen
        }

        // TODO: Replace this with actual progress calculation logic
        val userProgress = 91 // example value out of 100
        updateProgress(userProgress)
    }

    private fun updateProgress(progress: Int) {
        binding.progressBarAchievement.progress = progress

        val levelLabel = when {
            progress >= 90 -> "Platinum"
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
        updateBadge(binding.badgePlatinum, progress >= 90)
    }

    private fun updateBadge(badge: ImageView, unlocked: Boolean) {
        badge.alpha = if (unlocked) 1.0f else 0.3f
    }
}
