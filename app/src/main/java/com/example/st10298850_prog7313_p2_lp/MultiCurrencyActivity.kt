package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityMultiCurrencyBinding
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager

class MultiCurrencyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMultiCurrencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currencies = listOf("ZAR - South African Rand", "USD - US Dollar", "EUR - Euro", "GBP - British Pound", "NGN - Nigerian Naira")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)
        binding.currencySpinner.adapter = adapter

        // Set saved currency if available
        val savedCurrency = UserSessionManager.getCurrency(this)
        val idx = currencies.indexOfFirst { it.startsWith(savedCurrency) }
        if (idx >= 0) binding.currencySpinner.setSelection(idx)

        binding.btnSaveCurrency.setOnClickListener {
            val selectedCurrency = currencies[binding.currencySpinner.selectedItemPosition].substring(0, 3)
            UserSessionManager.saveCurrency(this, selectedCurrency)
            Toast.makeText(this, "Currency set to $selectedCurrency", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
