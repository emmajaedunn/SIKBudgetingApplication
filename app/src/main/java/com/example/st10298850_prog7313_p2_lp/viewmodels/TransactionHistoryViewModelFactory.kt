package com.example.st10298850_prog7313_p2_lp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.st10298850_prog7313_p2_lp.TransactionHistoryViewModel
import com.example.st10298850_prog7313_p2_lp.repositories.AccountRepository
import com.example.st10298850_prog7313_p2_lp.repositories.TransactionRepository

class TransactionHistoryViewModelFactory(
    private val application: Application,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val userId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionHistoryViewModel(
                application,
                transactionRepository,
                accountRepository,
                userId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}











/*class TransactionHistoryViewModelFactory(
    private val application: Application,
    private val userRepository: AccountRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionHistoryViewModel::class.java)) {
            return TransactionHistoryViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/





/*class TransactionHistoryViewModelFactory(
    private val application: Application,
    private val userRepository: AccountRepository,
    // ADDED THIS
    private val userId: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/













/*package com.example.st10298850_prog7313_p2_lp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.st10298850_prog7313_p2_lp.TransactionHistoryViewModel
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.repositories.TransactionRepository

class TransactionHistoryViewModelFactory(
    private val application: Application,
    private val userId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionHistoryViewModel::class.java)) {
            val database = AppDatabase.getDatabase(application)
            val repository = TransactionRepository(database.transactionDao())
            @Suppress("UNCHECKED_CAST")
            return TransactionHistoryViewModel(application, repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/