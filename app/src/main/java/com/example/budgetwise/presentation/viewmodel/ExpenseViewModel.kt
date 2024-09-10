package com.example.budgetwise.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val expenseRepository: ExpenseRepository) :
    ViewModel() {
    private val _expenseInsertState = MutableLiveData<Result<Unit>>()
    val expenseInsertState: LiveData<Result<Unit>> = _expenseInsertState

    val expense: LiveData<List<Expense>> = expenseRepository.getAllExpense()

    val expenseCategories: LiveData<List<ExpenseCategory>> =
        expenseRepository.getAllExpenseCategories()

    val accountTypes: LiveData<List<AccountType>> = expenseRepository.getAllAccountTypes()

    fun insertExpense(amount: Double?, date: Long, categoryId: Int, accountId: Int, note: String?) {
        viewModelScope.launch {
            try {
                if (amount != null && note != null) {
                    val expense = Expense(
                        date = date,
                        amount = amount,
                        expCategoryId = categoryId,
                        accountId = accountId,
                        note = note
                    )
                    expenseRepository.insertExpense(expense)
                    _expenseInsertState.postValue(Result.success(Unit))
                } else {
                    _expenseInsertState.postValue(Result.failure(IllegalArgumentException("Invalid input data")))
                }
            } catch (e: Exception) {
                _expenseInsertState.postValue(Result.failure(e))
            }
        }
    }

    fun insertExpenseCategories(expenseCategories: List<ExpenseCategory>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                expenseRepository.insertExpenseCategories(expenseCategories)
            } catch (_: Exception) {

            }
        }
    }

    fun insertAccountTypes(accountTypes: List<AccountType>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                expenseRepository.insertAccountTypes(accountTypes)
            } catch (_: Exception) {

            }
        }
    }

    fun getExpenseByCategory(): Map<String, Double> {
        val categories = expenseCategories.value.orEmpty()
        val expenseList = expense.value.orEmpty()

        // Aggregate expense amounts by category
        return expenseList.groupBy { expenseItem ->
            categories.find { it.id == expenseItem.expCategoryId }?.name ?: "Unknown"
        }.mapValues { (_, expenses) ->
            expenses.sumOf { it.amount }
        }
    }
}