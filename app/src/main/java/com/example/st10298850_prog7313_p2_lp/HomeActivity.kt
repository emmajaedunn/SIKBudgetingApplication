package com.example.st10298850_prog7313_p2_lp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityHomeBinding
import com.example.st10298850_prog7313_p2_lp.repositories.AccountRepository
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager
import com.example.st10298850_prog7313_p2_lp.viewmodels.HomeViewModel
import com.example.st10298850_prog7313_p2_lp.viewmodels.HomeViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = UserSessionManager.getUserId(this)
        if (userId == -1L) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val userRepository = AccountRepository(
            AppDatabase.getDatabase(this).accountDao(),
            AppDatabase.getDatabase(this).userDao()
        )
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(application, userRepository)
        )[HomeViewModel::class.java]

        viewModel.userName.observe(this) { name ->
            binding.tvWelcome.text = "Welcome, $name"
        }

        viewModel.loadUserName(userId)

        // Set click listeners for navigation buttons
        binding.btnAddTransaction.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        binding.btnManageBudgets.setOnClickListener {
            startActivity(Intent(this, TransactionHistoryActivity::class.java)) // Budget page
        }

        binding.btnAnalytics.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Achievement level placeholder
        binding.tvAchievementLevel.text = "Your achievement level: [Coming Soon]"

        // ***REMOVED BOTTOM NAVIGATION LOGIC***
        // No more binding.bottomNavigation or nav bar listeners in this activity!
    }
}
