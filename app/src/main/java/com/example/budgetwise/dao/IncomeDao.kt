package com.example.budgetwise.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory

@Dao
interface IncomeDao {

    @Insert
    suspend fun insertIncome(income: Income)

    @Query("SELECT * FROM income_table")
    fun getAllIncome(): LiveData<List<Income>>
}