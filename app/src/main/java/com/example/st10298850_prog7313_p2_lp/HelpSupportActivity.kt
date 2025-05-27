package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HelpSupportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_support)

        // Array of question/answer pairs
        val questions = arrayOf(
            R.id.question1, R.id.question2, R.id.question3,
            R.id.question4, R.id.question5, R.id.question6
        )

        val answers = arrayOf(
            R.id.answer1, R.id.answer2, R.id.answer3,
            R.id.answer4, R.id.answer5, R.id.answer6
        )

        for (i in questions.indices) {
            val question = findViewById<TextView>(questions[i])
            val answer = findViewById<TextView>(answers[i])

            question.setOnClickListener {
                if (answer.visibility == View.GONE) {
                    answer.visibility = View.VISIBLE
                    question.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_collapse, 0)
                } else {
                    answer.visibility = View.GONE
                    question.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand, 0)
                }
            }
        }
    }
}
