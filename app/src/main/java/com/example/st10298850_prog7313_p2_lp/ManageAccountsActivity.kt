package com.example.st10298850_prog7313_p2_lp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.st10298850_prog7313_p2_lp.data.Account
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityManageAccountsBinding
import com.example.st10298850_prog7313_p2_lp.databinding.DialogEditAccountBinding
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager
import kotlinx.coroutines.launch

class ManageAccountsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageAccountsBinding
    private lateinit var accountAdapter: AccountAdapter
    private lateinit var database: AppDatabase
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = UserSessionManager.getUserId(this)
        if (userId == -1L) {
            redirectToLogin()
            return
        }

        database = AppDatabase.getDatabase(this)
        setupRecyclerView()
        setupClickListeners()
        loadAccounts()
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setupRecyclerView() {
        accountAdapter = AccountAdapter()
        binding.rvAccounts.apply {
            layoutManager = LinearLayoutManager(this@ManageAccountsActivity)
            adapter = accountAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnClose.setOnClickListener { finish() }
        binding.btnAdd.setOnClickListener { showAccountDialog() }
        binding.fabAddAccount.setOnClickListener { showAccountDialog() }
    }

    private fun loadAccounts() {
        lifecycleScope.launch {
            val accounts = database.accountDao().getAccountsForUser(userId)
            accountAdapter.submitList(accounts)

            // Update total balance
            val total = accounts.sumOf { it.goalAmount }
            binding.tvTotalBalance.text = formatCurrency(total)
        }
    }

    private fun showAccountDialog() {
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }

        val dialogBinding = DialogEditAccountBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.btnClose.setOnClickListener { dialog.dismiss() }

        dialogBinding.btnSaveChanges.setOnClickListener {
            val name = dialogBinding.etAccountName.text.toString().trim()
            val type = dialogBinding.etAccountType.text.toString().trim()
            val amountText = dialogBinding.etAccountAmount.text.toString().trim()

            val amount = amountText.toDoubleOrNull()

            if (name.isBlank() || type.isBlank() || amount == null || amount < 0) {
                Toast.makeText(this, "Please enter valid account information.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newAccount = Account(
                userId = userId,
                name = name,
                type = type,
                goalAmount = amount
            )

            addAccount(newAccount)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun addAccount(account: Account) {
        lifecycleScope.launch {
            database.accountDao().insertAccount(account)
            loadAccounts()
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
