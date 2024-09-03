package com.example.budgetwise.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.budgetwise.data.local.model.Income

@Dao
interface IncomeDao {

    @Insert
    fun insertIncome(income: Income)
}