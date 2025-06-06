package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityHelpSupportBinding

class HelpSupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupClickListeners()
        setupFAQClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupFAQClickListeners() {
        val questions = arrayOf(
            binding.question1, binding.question2, binding.question3,
            binding.question4, binding.question5, binding.question6
        )

        val answers = arrayOf(
            binding.answer1, binding.answer2, binding.answer3,
            binding.answer4, binding.answer5, binding.answer6
        )

        for (i in questions.indices) {
            questions[i].setOnClickListener {
                val isVisible = answers[i].visibility == View.VISIBLE
                answers[i].visibility = if (isVisible) View.GONE else View.VISIBLE
                val icon = if (isVisible) R.drawable.ic_expand else R.drawable.ic_collapse
                questions[i].setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
            }
        }
    }
}
