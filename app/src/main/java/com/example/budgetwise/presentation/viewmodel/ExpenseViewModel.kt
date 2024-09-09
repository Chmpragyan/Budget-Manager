package com.example.budgetwise.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val expenseRepository: ExpenseRepository) :
    ViewModel() {
    private val _insertExpense = MutableLiveData<Result<Unit>>()
    val insertExpense: LiveData<Result<Unit>> = _insertExpense

    var expense: LiveData<List<Expense>>? = null

    var expenseCategory: LiveData<List<ExpenseCategory>>? = null

    private val _accountTypes = MutableLiveData<List<AccountType>>()
    val accountTypes: LiveData<List<AccountType>> = _accountTypes

    init {
        viewModelScope.launch {
            expense = expenseRepository.getAllExpense()
            expenseCategory = expenseRepository.getAllExpenseCategories()
            _accountTypes.value = expenseRepository.getAllAccountTypes()
        }
    }

    fun insertExpense(amount: Double?, date: Long, categoryId: Int, accountId: Int, note: String) {
        viewModelScope.launch {
            val result = expenseRepository.insertExpense(amount, date, categoryId, accountId, note)
            _insertExpense.value = result
        }
    }

    fun insertExpenseCategories(incomeCategories: List<ExpenseCategory>) {
        viewModelScope.launch {
            expenseRepository.insertExpenseCategories(incomeCategories)
        }
    }

    fun insertAccountTypes(accountTypes: List<AccountType>) {
        viewModelScope.launch {
            expenseRepository.insertAccountTypes(accountTypes)
        }
    }
}