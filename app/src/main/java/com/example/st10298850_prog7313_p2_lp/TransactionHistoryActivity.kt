package com.example.st10298850_prog7313_p2_lp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityTransactionHistoryBinding
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.example.st10298850_prog7313_p2_lp.viewmodels.TransactionHistoryViewModelFactory
import android.app.Dialog
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager
import android.widget.Toast
import java.io.File
import androidx.core.content.FileProvider
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.repositories.AccountRepository
import com.example.st10298850_prog7313_p2_lp.repositories.TransactionRepository

/**
 * Activity for displaying and managing transaction history.
 * This activity allows users to view their transactions, filter them by date and category,
 * and view receipt images associated with transactions.
 */
class TransactionHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionHistoryBinding
    private lateinit var viewModel: TransactionHistoryViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    /**
     * Initializes the activity, sets up the UI, and checks for user authentication.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is logged in, redirect to login if not
        val userId = UserSessionManager.getUserId(this)
        if (userId == -1L) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setupViewModel()
        setupRecyclerView()
        setupUI()
    }

    /**
     * Logs the start of the activity for debugging purposes.
     */
    override fun onStart() {
        super.onStart()
        Log.d("ImagePopup", "Glide load started")
    }

    /**
     * Sets up the ViewModel for this activity.
     * @param userId The ID of the currently logged-in user.
     */

    // ADDED THIS (NEW)
    private fun setupViewModel() {
        val userId = UserSessionManager.getUserId(this)
        val database = AppDatabase.getDatabase(applicationContext)
        val accountRepository = AccountRepository(database.accountDao(), database.userDao())
        val transactionRepository = TransactionRepository(database.transactionDao())

        val factory = TransactionHistoryViewModelFactory(
            application,
            transactionRepository,
            accountRepository,
            userId
        )

        viewModel = ViewModelProvider(this, factory)[TransactionHistoryViewModel::class.java]
    }


    /**
     * Sets up the RecyclerView to display transactions.
     * Configures the adapter and sets up a click listener for viewing receipt images.
     */
    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter { transaction ->
            if (!transaction.receiptPath.isNullOrEmpty()) {
                showImagePopup(transaction.receiptPath)
            }
        }

        binding.rvTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(this@TransactionHistoryActivity)
        }

        // Observe changes in filtered transactions and update the adapter
        viewModel.filteredTransactions.observe(this) { transactions ->
            transactionAdapter.submitList(transactions)
        }
    }

    /**
     * Sets up the UI components including toolbar, bottom navigation, date filter, and category chips.
     */
    private fun setupUI() {
        // Set up the custom toolbar without system back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false) // <-- REMOVE system back icon

        // Handle your own custom back button
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupBottomNavigation()
        setupDateFilter()
        setupCategoryChips()

        binding.btnSelectDateRange.setOnClickListener {
            showDateRangePicker()
        }

        binding.btnClearDateFilter.setOnClickListener {
            viewModel.clearDateFilter()
        }
    }


    /**
     * Sets up the bottom navigation menu.
     */
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.navigation_stats -> startActivity(Intent(this, StatsActivity::class.java))
                R.id.navigation_add -> startActivity(Intent(this, AddTransactionActivity::class.java))
                R.id.navigation_budget -> startActivity(Intent(this, TransactionHistoryActivity::class.java))
                R.id.navigation_settings -> startActivity(Intent(this, SettingsActivity::class.java))
                else -> return@setOnItemSelectedListener false
            }
            true
        }
    }

    /**
     * Sets up the date filter chip for filtering transactions by the current month.
     */
    private fun setupDateFilter() {
        binding.chipThisMonth.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.setThisMonthFilter()
            } else {
                viewModel.clearDateFilter()
            }
        }
    }

    /**
     * Sets up category chips for filtering transactions by category.
     */
    private fun setupCategoryChips() {
        viewModel.categories.observe(this) { categories ->
            binding.chipGroupCategories.removeAllViews()

            // Add "All Categories" chip
            val allChip = Chip(this).apply {
                id = View.generateViewId()
                text = "All Categories"
                isCheckable = true
                isChecked = true
            }
            binding.chipGroupCategories.addView(allChip)

            // Add chips for each category
            categories.forEach { category ->
                val chip = Chip(this).apply {
                    id = View.generateViewId()
                    text = category
                    isCheckable = true
                }
                binding.chipGroupCategories.addView(chip)
            }

            // Set up listener for category selection
            binding.chipGroupCategories.setOnCheckedChangeListener { _, checkedId ->
                val selectedChip = binding.chipGroupCategories.findViewById<Chip>(checkedId)
                val selectedCategory = selectedChip?.text?.toString()
                viewModel.setCategoryFilter(selectedCategory)
            }
        }
    }

    /**
     * Shows a date range picker dialog for selecting a custom date range.
     */
    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select date range")
            .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second
            viewModel.setDateFilter(startDate, endDate)
            binding.chipThisMonth.isChecked = false
        }

        dateRangePicker.show(supportFragmentManager, "DATE_RANGE_PICKER")
    }



    /**
     * Shows a popup dialog displaying the receipt image for a transaction.
     * @param receiptPath The file path of the receipt image.
     */

    //// ADDED THIS
    private fun showImagePopup(receiptPath: String) {
        Log.d("ImagePopup", "Attempting to show image from path: $receiptPath")

        val file = File(receiptPath)
        if (!file.exists()) {
            Log.e("ImagePopup", "File does not exist: $receiptPath")
            Toast.makeText(this, "Receipt image not found", Toast.LENGTH_SHORT).show()
            return
        }

        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_image_popup)

        val imageView = dialog.findViewById<ImageView>(R.id.popupImageView)

        Glide.with(this)
            .load(uri)
            .error(R.drawable.close_ic) // fallback icon
            .into(imageView)

        dialog.show()
        Log.d("ImagePopup", "Dialog shown with image")
    }}

