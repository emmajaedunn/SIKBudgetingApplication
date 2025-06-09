package com.example.st10298850_prog7313_p2_lp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.st10298850_prog7313_p2_lp.data.BudgetGoal

class RoomGoalAdapter : RecyclerView.Adapter<RoomGoalAdapter.GoalViewHolder>() {
    private var goals = listOf<BudgetGoal>()

    class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.etGoalName)
        val tvTarget: TextView = itemView.findViewById(R.id.etGoalAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_budget_goal, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goals[position]
        holder.tvCategory.text = goal.name
        holder.tvTarget.text = "Target: R${goal.goalAmount}"
    }

    override fun getItemCount(): Int = goals.size

    fun submitList(newGoals: List<BudgetGoal>) {
        goals = newGoals
        notifyDataSetChanged()
    }
}
