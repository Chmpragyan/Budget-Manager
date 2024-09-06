package com.example.budgetwise.data.local.repository

import androidx.lifecycle.LiveData
import com.example.budgetwise.dao.AccountTypeDao
import com.example.budgetwise.dao.IncomeCategoryDao
import com.example.budgetwise.dao.IncomeDao
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IncomeRepository @Inject constructor(
    private val incomeDao: IncomeDao,
    private val incomeCategoryDao: IncomeCategoryDao,
    private val accountTypeDao: AccountTypeDao
) {

    suspend fun insertIncome(
        amount: Double?,
        date: Long,
        categoryId: Int,
        accountId: Int,
        note: String
    ): Result<Unit> {
        return try {
            if (amount != null && note.isNotEmpty()) {
                val income = Income(
                    date = date,
                    amount = amount,
                    incCategoryId = categoryId,
                    accountId = accountId,
                    note = note
                )
                withContext(Dispatchers.IO) {
                    incomeDao.insertIncome(income)
                }
                Result.success(Unit)
            } else {
                Result.failure(IllegalArgumentException("Invalid input data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertIncomeCategories(incomeCategories: List<IncomeCategory>) {
        withContext(Dispatchers.IO) {
            incomeCategoryDao.insertIncomeCategories(incomeCategories)
        }
    }

    suspend fun insertAccountTypes(accountTypes: List<AccountType>) {
        withContext(Dispatchers.IO) {
            accountTypeDao.insertAccountTypes(accountTypes)
        }
    }

    suspend fun getAllIncome(): LiveData<List<Income>> {
        return withContext(Dispatchers.IO){
            incomeDao.getAllIncome()
        }
    }

    suspend fun getAllIncomeCategories(): List<IncomeCategory> {
        return withContext(Dispatchers.IO) {
            incomeCategoryDao.getAllIncomeCategories()
        }
    }

    suspend fun getAllAccountTypes(): List<AccountType> {
        return withContext(Dispatchers.IO) {
            accountTypeDao.getAllAccountTypes()
        }
    }
}
