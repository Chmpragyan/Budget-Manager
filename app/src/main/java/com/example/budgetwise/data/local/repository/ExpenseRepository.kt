package com.example.budgetwise.data.local.repository

import androidx.lifecycle.LiveData
import com.example.budgetwise.dao.AccountTypeDao
import com.example.budgetwise.dao.ExpenseCategoryDao
import com.example.budgetwise.dao.ExpenseDao
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.model.Income
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

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    suspend fun updateExpense(
        id: Int,
        amount: Double?,
        date: Long,
        categoryId: Int,
        accountId: Int,
        note: String
    ) {
        expenseDao.updateExpense(id, amount, date, categoryId, accountId, note)
    }

    suspend fun getExpenseById(expenseId: Int): Expense? {
        return expenseDao.getExpenseById(expenseId)
    }
}