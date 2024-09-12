package com.example.budgetwise.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.data.local.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "IncomeViewModel"

@HiltViewModel
class IncomeViewModel @Inject constructor(private val incomeRepository: IncomeRepository) :
    ViewModel() {
    private val _incomeInsertState = MutableSharedFlow<Result<Unit>>()
    val incomeInsertState: SharedFlow<Result<Unit>> = _incomeInsertState.asSharedFlow()

    val income: LiveData<List<Income>> = incomeRepository.getAllIncome()

    val incomeCategories: LiveData<List<IncomeCategory>> = incomeRepository.getAllIncomeCategories()
    val accountTypes: LiveData<List<AccountType>> = incomeRepository.getAllAccountTypes()

    private val _incomeEvent = MutableSharedFlow<IncomeEvent>()
    val incomeEvent: SharedFlow<IncomeEvent> = _incomeEvent

    fun insertIncome(income: Income) {
        viewModelScope.launch {
            try {
                incomeRepository.insertIncome(income)
                _incomeEvent.emit(IncomeEvent.Success("Income updated successfully"))
            } catch (e: Exception) {
                _incomeEvent.emit(IncomeEvent.Failure("Failed to add expense: ${e.message}"))
                Log.d(TAG, "Failed to add expense:  ${e.message}")
            }
        }
    }

    fun updateIncome(
        id: Int,
        amount: Double?,
        date: Long,
        categoryId: Int,
        accountId: Int,
        note: String
    ) {
        viewModelScope.launch {
            try {
                incomeRepository.updateIncome(id, amount, date, categoryId, accountId, note)
                _incomeEvent.emit(IncomeEvent.Success("Income updated successfully"))
            } catch (e: Exception) {
                _incomeEvent.emit(IncomeEvent.Failure("Failed to update income: ${e.message}"))
            }
        }
    }

    fun deleteIncome(income: Income) {
        viewModelScope.launch {
            try {
                incomeRepository.deleteIncome(income)
                _incomeEvent.emit(IncomeEvent.Success("Income deleted successfully"))
            } catch (e: Exception) {
                _incomeEvent.emit(IncomeEvent.Failure("Failed to delete income: ${e.message}"))
            }
        }
    }

    fun insertIncomeCategories(incomeCategories: List<IncomeCategory>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                incomeRepository.insertIncomeCategories(incomeCategories)
            } catch (e: Exception) {
                // exception
            }
        }
    }

    fun insertAccountTypes(accountTypes: List<AccountType>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                incomeRepository.insertAccountTypes(accountTypes)
            } catch (e: Exception) {
                // exception
            }
        }
    }

    fun getIncomeByCategory(): Map<String, Double> {
        val categories = incomeCategories.value.orEmpty()
        val incomeList = income.value.orEmpty()

        // Aggregate expense amounts by category
        return incomeList.groupBy { incomeItem ->
            categories.find { it.id == incomeItem.incCategoryId }?.name ?: "Unknown"
        }.mapValues { (_, expenses) ->
            expenses.sumOf { it.amount }
        }
    }

    fun getIncomeById(incomeId: Int) {
        viewModelScope.launch {
            incomeRepository.getIncomeById(incomeId)
        }
    }
}

sealed class IncomeEvent {
    data class Success(val message: String) : IncomeEvent()
    data class Failure(val error: String) : IncomeEvent()
    data class IncomeLoaded(val income: Income) : IncomeEvent()
}