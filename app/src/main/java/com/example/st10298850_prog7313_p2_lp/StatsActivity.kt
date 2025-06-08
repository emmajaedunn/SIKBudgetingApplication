package com.example.st10298850_prog7313_p2_lp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityStatsBinding
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieData
import kotlinx.coroutines.launch
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager

class StatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadStatistics()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_stats -> true
                R.id.navigation_add -> {
                    startActivity(Intent(this, AddTransactionActivity::class.java))
                    true
                }
                R.id.navigation_budget -> {
                    startActivity(Intent(this, TransactionHistoryActivity::class.java))
                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadStatistics() {
        val userId = UserSessionManager.getUserId(this)
        if (userId == -1L) return

        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(this@StatsActivity).transactionDao()

            val income = dao.getTotalIncome(userId) ?: 0.0
            val expenses = dao.getTotalExpenses(userId) ?: 0.0
            val net = income - expenses

            binding.tvIncome.text = formatCurrency(income)
            binding.tvExpenses.text = formatCurrency(expenses)
            binding.tvNetBalance.text = formatCurrency(net)

            val entries = mutableListOf<PieEntry>()
            if (income > 0f) entries.add(PieEntry(income.toFloat(), "Income"))
            if (expenses > 0f) entries.add(PieEntry(expenses.toFloat(), "Expenses"))

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = listOf(Color.parseColor("#4CAF50"), Color.parseColor("#F44336"))
            val pieData = PieData(dataSet)
            pieData.setDrawValues(true)
            pieData.setValueTextColor(Color.WHITE)
            pieData.setValueTextSize(14f)

            binding.pieChart.data = pieData
            binding.pieChart.description.isEnabled = false
            binding.pieChart.setUsePercentValues(true)
            binding.pieChart.setDrawEntryLabels(false)
            binding.pieChart.centerText = "Income vs Expenses"
            binding.pieChart.setCenterTextSize(14f)
            binding.pieChart.animateY(1000)
            binding.pieChart.invalidate()
        }
    }

    private fun formatCurrency(amount: Double): String {
        val currencyCode = UserSessionManager.getCurrency(this)
        val currency = java.util.Currency.getInstance(currencyCode)
        val format = java.text.NumberFormat.getCurrencyInstance().apply {
            this.currency = currency
        }
        return format.format(amount)
    }
}
