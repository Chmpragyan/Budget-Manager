package com.example.budgetwise.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.Income

@Dao
interface ExpenseDao {

    @Insert
    fun insertExpenses(expense: Expense)

    @Query("SELECT * FROM expense_table")
    fun getAllExpense(): LiveData<List<Expense>>
}