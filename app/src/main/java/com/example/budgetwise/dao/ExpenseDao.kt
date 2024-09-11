package com.example.budgetwise.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.Income

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpenses(expense: Expense)

    @Query("SELECT * FROM expense_table")
    fun getAllExpense(): LiveData<List<Expense>>

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("UPDATE expense_table SET amount = :amount, date = :date, expCategoryId = :categoryId, accountId = :accountId, note = :note WHERE id = :id")
    suspend fun updateExpense(id: Int, amount: Double?, date: Long, categoryId: Int, accountId: Int, note: String)

    @Query("SELECT * FROM expense_table WHERE id = :expenseId LIMIT 1")
    suspend fun getExpenseById(expenseId: Int): Expense?
}