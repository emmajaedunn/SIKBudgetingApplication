package com.example.st10298850_prog7313_p2_lp.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.st10298850_prog7313_p2_lp.data.BudgetGoalRepository
import com.example.st10298850_prog7313_p2_lp.repositories.AccountRepository

/// ADDED THIS
class HomeViewModelFactory(
    private val application: Application,
    private val userRepository: AccountRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/*
class HomeViewModelFactory(
    private val application: Application,
    private val userRepository: AccountRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/







/*class HomeViewModelFactory(
    private val application: Application,
    private val budgetGoalRepository: BudgetGoalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(application, budgetGoalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/