package com.example.budgetwise.data.local.repository

import androidx.lifecycle.LiveData
import com.example.budgetwise.dao.AccountTypeDao
import com.example.budgetwise.dao.ExpenseCategoryDao
import com.example.budgetwise.dao.ExpenseDao
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val expenseCategoryDao: ExpenseCategoryDao,
    private val accountTypeDao: AccountTypeDao
) {

    suspend fun insertExpense(
        amount: Double?,
        date: Long,
        categoryId: Int,
        accountId: Int,
        note: String
    ) : Result<Unit> {
         return if (amount != null && note.isNotEmpty()) {
            val expense = Expense(
                date = date,
                amount = amount,
                expCategoryId = categoryId,
                accountId = accountId,
                note = note
            )
            withContext(Dispatchers.IO) {
                expenseDao.insertExpenses(expense)
            }
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Invalid input data"))
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

    suspend fun getAllExpense(): LiveData<List<Expense>> {
        return withContext(Dispatchers.IO){
            expenseDao.getAllExpense()
        }
    }

    suspend fun getAllExpenseCategories(): LiveData<List<ExpenseCategory>> {
        return withContext(Dispatchers.IO) {
            expenseCategoryDao.getAllExpenseCategories()
        }
    }

    suspend fun getAllAccountTypes(): List<AccountType> {
        return withContext(Dispatchers.IO) {
            accountTypeDao.getAllAccountTypes()
        }
    }
}