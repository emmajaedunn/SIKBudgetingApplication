package com.example.st10298850_prog7313_p2_lp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.data.BudgetGoal
import com.example.st10298850_prog7313_p2_lp.data.BudgetGoalDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import android.widget.ImageButton
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager

class BudgetGoalActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RoomGoalAdapter
    private lateinit var btnAddGoal: Button
    private lateinit var dao: BudgetGoalDao
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_goal)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        recyclerView = findViewById(R.id.recyclerView)
        btnAddGoal = findViewById(R.id.btnAddGoal)

        adapter = RoomGoalAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        userId = UserSessionManager.getUserId(this)
        dao = AppDatabase.getDatabase(this).budgetGoalDao()

        loadGoals()

        btnAddGoal.setOnClickListener {
            showAddGoalDialog()

        }
    }

    private fun loadGoals() {
        CoroutineScope(Dispatchers.IO).launch {
            val goals = dao.getBudgetGoalsForUser(userId)
            withContext(Dispatchers.Main) {
                adapter.submitList(goals)
            }
        }
    }

    private fun showAddGoalDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_goal, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.etGoalName)
        val amountInput = dialogView.findViewById<EditText>(R.id.etGoalAmount)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString()
                val amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insertOrUpdate(
                        BudgetGoal(
                            userId = userId,
                            name = name,
                            goalAmount = amount
                        )
                    )
                    loadGoals()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()

    }
}
