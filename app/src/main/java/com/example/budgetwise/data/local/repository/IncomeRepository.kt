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

    suspend fun insertIncome(income: Income) {
        withContext(Dispatchers.IO) {
            incomeDao.insertIncome(income)
        }
    }

    suspend fun updateIncome(
        id: Int,
        amount: Double?,
        date: Long,
        categoryId: Int,
        accountId: Int,
        note: String
    ) {
        withContext(Dispatchers.IO) {
            incomeDao.updateIncome(id, amount, date, categoryId, accountId, note)
        }
    }

    suspend fun deleteIncome(income: Income) {
        withContext(Dispatchers.IO) {
            incomeDao.deleteIncome(income)
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

    suspend fun getIncomeById(incomeId: Int): Income? {
        return incomeDao.getIncomeById(incomeId)
    }
}