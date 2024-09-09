package com.example.budgetwise.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetwise.data.local.model.ExpenseCategory

@Dao
interface ExpenseCategoryDao {
    @Insert
    suspend fun insertExpenseCategories(incomeCategories: List<ExpenseCategory>)

    @Query("SELECT * FROM expense_category")
    fun getAllExpenseCategories(): LiveData<List<ExpenseCategory>>
}