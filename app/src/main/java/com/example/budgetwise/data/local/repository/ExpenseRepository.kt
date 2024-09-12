package com.example.budgetwise.data.local.repository

import androidx.lifecycle.LiveData
import com.example.budgetwise.dao.AccountTypeDao
import com.example.budgetwise.dao.ExpenseCategoryDao
import com.example.budgetwise.dao.ExpenseDao
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val expenseCategoryDao: ExpenseCategoryDao,
    private val accountTypeDao: AccountTypeDao
) {

    suspend fun insertExpense(expense: Expense) {
        withContext(Dispatchers.IO) {
            expenseDao.insertExpenses(expense)
        }
    }

    suspend fun updateExpense(
        id: Int,
        amount: Double?,
        date: Long,
        categoryId: Int,
        accountId: Int,
        note: String
    ) {
        withContext(Dispatchers.IO) {
            expenseDao.updateExpense(id, amount, date, categoryId, accountId, note)
        }
    }

    suspend fun deleteExpense(expense: Expense) {
        withContext(Dispatchers.IO) {
            expenseDao.deleteExpense(expense)
        }
    }

    suspend fun insertExpenseCategories(expenseCategories: List<ExpenseCategory>) {
        withContext(Dispatchers.IO) {
            expenseCategoryDao.insertExpenseCategories(expenseCategories)
        }
    }

    suspend fun insertAccountTypes(accountTypes: List<AccountType>) {
        withContext(Dispatchers.IO) {
            accountTypeDao.insertAccountTypes(accountTypes)
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

    suspend fun getExpenseById(expenseId: Int): Expense? {
        return expenseDao.getExpenseById(expenseId)
    }
}