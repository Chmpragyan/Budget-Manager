package com.example.budgetwise.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.budgetwise.data.local.model.Expense

@Dao
interface ExpenseDao {

    @Insert
    fun insertExpenses(expense: Expense)
}