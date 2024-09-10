package com.example.budgetwise.data.local.repository

import androidx.lifecycle.LiveData
import com.example.budgetwise.dao.AccountTypeDao
import com.example.budgetwise.dao.ExpenseCategoryDao
import com.example.budgetwise.dao.ExpenseDao
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val expenseCategoryDao: ExpenseCategoryDao,
    private val accountTypeDao: AccountTypeDao
) {

    suspend fun insertExpense(expense: Expense): Result<Unit> {
        return try {
            expenseDao.insertExpenses(expense)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertExpenseCategories(expenseCategories: List<ExpenseCategory>): Result<Unit> {
        return try {
            expenseCategoryDao.insertExpenseCategories(expenseCategories)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertAccountTypes(accountTypes: List<AccountType>): Result<Unit> {
        return try {
            accountTypeDao.insertAccountTypes(accountTypes)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllExpense(): LiveData<List<Expense>> {
        return expenseDao.getAllExpense()
    }

    fun getAllExpenseCategories(): LiveData<List<ExpenseCategory>> {
        return expenseCategoryDao.getAllExpenseCategories()
    }

    fun getAllAccountTypes(): LiveData<List<AccountType>> {
        return accountTypeDao.getAllAccountTypes()
    }
}