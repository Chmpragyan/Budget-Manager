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

    suspend fun insertIncome(income: Income): Result<Unit> {
        return try {
            incomeDao.insertIncome(income)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertIncomeCategories(incomeCategories: List<IncomeCategory>): Result<Unit> {
        return try {
            incomeCategoryDao.insertIncomeCategories(incomeCategories)
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

    fun getAllIncome(): LiveData<List<Income>> {
        return incomeDao.getAllIncome()
    }

    fun getAllIncomeCategories(): LiveData<List<IncomeCategory>> {
        return incomeCategoryDao.getAllIncomeCategories()
    }

    fun getAllAccountTypes(): LiveData<List<AccountType>> {
        return accountTypeDao.getAllAccountTypes()
    }

    suspend fun deleteIncome(income: Income) {
        incomeDao.deleteIncome(income)
    }

    suspend fun updateIncome(
        id: Int,
        amount: Double?,
        date: Long,
        categoryId: Int,
        accountId: Int,
        note: String
    ) {
        incomeDao.updateIncome(id, amount, date, categoryId, accountId, note)
    }

    suspend fun getIncomeById(incomeId: Int): Income? {
        return incomeDao.getIncomeById(incomeId)
    }
}

