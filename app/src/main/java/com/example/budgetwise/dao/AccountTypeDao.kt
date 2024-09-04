package com.example.budgetwise.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetwise.data.local.model.AccountType

@Dao
interface AccountTypeDao {

    @Insert
    suspend fun insertAccountTypes(accountTypes: List<AccountType>)

    @Query("SELECT * FROM account_table")
    suspend fun getAllAccountTypes(): List<AccountType>
}