package com.example.budgetwise.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(private val expenseRepository: ExpenseRepository) :
    ViewModel() {
    private val _expenseInsertState = MutableStateFlow<Result<Unit>?>(null)
    val expenseInsertState: StateFlow<Result<Unit>?> = _expenseInsertState.asStateFlow()

    val expenses: LiveData<List<Expense>> = expenseRepository.getAllExpense()

    val expenseCategories: LiveData<List<ExpenseCategory>> =
        expenseRepository.getAllExpenseCategories()

    private val _expenseEvent = MutableSharedFlow<ExpenseEvent>()
    val expenseEvent: SharedFlow<ExpenseEvent> = _expenseEvent

    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.insertExpense(expense)
                _expenseEvent.emit(ExpenseEvent.Success("Expense added successfully"))
            } catch (e: Exception) {
                _expenseEvent.emit(ExpenseEvent.Failure("Failed to add expense: ${e.message}"))
            }
        }
    }

    fun updateExpense(
        id: Int,
        amount: Double?,
        date: Long,
        categoryId: Int,
        accountId: Int,
        note: String
    ) {
        viewModelScope.launch {
            try {
                expenseRepository.updateExpense(id, amount, date, categoryId, accountId, note)
                _expenseEvent.emit(ExpenseEvent.Success("Expense updated successfully"))
            } catch (e: Exception) {
                _expenseEvent.emit(ExpenseEvent.Failure("Failed to update expense: ${e.message}"))
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.deleteExpense(expense)
                _expenseEvent.emit(ExpenseEvent.Success("Expense deleted successfully"))
            } catch (e: Exception) {
                _expenseEvent.emit(ExpenseEvent.Failure("Failed to delete expense: ${e.message}"))
            }
        }
    }

    fun insertExpenseCategories(expenseCategories: List<ExpenseCategory>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                expenseRepository.insertExpenseCategories(expenseCategories)
            } catch (e: Exception) {
                // error
            }
        }
    }

    fun insertAccountTypes(accountTypes: List<AccountType>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                expenseRepository.insertAccountTypes(accountTypes)
            } catch (e: Exception) {
                // error
            }
        }
    }

    fun getExpenseById(expenseId: Int) {
        viewModelScope.launch {
            try {
                val expense = expenseRepository.getExpenseById(expenseId)
                if (expense != null) {
                    _expenseEvent.emit(ExpenseEvent.ExpenseLoaded(expense))
                } else {
                    _expenseEvent.emit(ExpenseEvent.Failure("Expense not found"))
                }
            } catch (e: Exception) {
                _expenseEvent.emit(ExpenseEvent.Failure("Failed to load expense: ${e.message}"))
            }
        }
    }

    fun getExpenseByCategory(): Map<String, Double> {
        val categories = expenseCategories.value.orEmpty()
        val expenseList = expenses.value.orEmpty()

        return expenseList.groupBy { expenseItem ->
            categories.find { it.id == expenseItem.expCategoryId }?.name ?: "Unknown"
        }.mapValues { (_, expenses) ->
            expenses.sumOf { it.amount }
        }
    }
}

sealed class ExpenseEvent {
    data class Success(val message: String) : ExpenseEvent()
    data class Failure(val error: String) : ExpenseEvent()
    data class ExpenseLoaded(val expense: Expense) : ExpenseEvent()
}