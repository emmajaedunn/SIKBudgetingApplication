// AccountRepository.kt
package com.example.st10298850_prog7313_p2_lp.repositories

import com.example.st10298850_prog7313_p2_lp.data.Account
import com.example.st10298850_prog7313_p2_lp.data.AccountDao
import com.example.st10298850_prog7313_p2_lp.data.UserDao
import com.example.st10298850_prog7313_p2_lp.data.User

class AccountRepository(
    private val accountDao: AccountDao,
    private val userDao: UserDao
) {
    suspend fun getAccountsForUser(userId: Long): List<Account> {
        return accountDao.getAccountsForUser(userId)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
}
/*package com.example.st10298850_prog7313_p2_lp.repositories

import com.example.st10298850_prog7313_p2_lp.data.Account
import com.example.st10298850_prog7313_p2_lp.data.AccountDao
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.data.UserDao
import com.example.st10298850_prog7313_p2_lp.data.User

/// ADDED THIS
val database = AppDatabase.getDatabase(this)

val accountRepository = AccountRepository(
    database.accountDao(),
    database.userDao()
)

class AccountRepository(
    private val accountDao: AccountDao,
    private val userDao: UserDao
)

{
    suspend fun getAccountsForUser(userId: Long): List<Account> {
        return accountDao.getAccountsForUser(userId)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
}











/*package com.example.st10298850_prog7313_p2_lp.repositories

import com.example.st10298850_prog7313_p2_lp.data.Account
import com.example.st10298850_prog7313_p2_lp.data.AccountDao
import com.example.st10298850_prog7313_p2_lp.data.UserDao
import com.example.st10298850_prog7313_p2_lp.data.User

class AccountRepository(private val accountDao: AccountDao) {
    suspend fun getAccountsForUser(userId: Long): List<Account> {
        return accountDao.getAccountsForUser(userId)

        suspend fun getUserById(userId: Long): User? {
            return UserDao.getUserById(userId)
        }

    }
} */