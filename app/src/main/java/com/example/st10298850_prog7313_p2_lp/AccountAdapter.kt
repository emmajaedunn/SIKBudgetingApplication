package com.example.st10298850_prog7313_p2_lp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.st10298850_prog7313_p2_lp.data.Account
import com.example.st10298850_prog7313_p2_lp.databinding.ItemAccountBinding
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager

class AccountAdapter : ListAdapter<Account, AccountAdapter.AccountViewHolder>(AccountDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AccountViewHolder(private val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            val context = binding.root.context
            val currencyCode = UserSessionManager.getCurrency(context)
            val currency = java.util.Currency.getInstance(currencyCode)
            val format = java.text.NumberFormat.getCurrencyInstance().apply {
                this.currency = currency
            }

            binding.tvAccountName.text = account.name
            binding.tvAccountType.text = account.type
            binding.tvAccountBalance.text = format.format(account.goalAmount)
        }

    }

    class AccountDiffCallback : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.accountId == newItem.accountId
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }
    }
}