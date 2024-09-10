package com.example.budgetwise.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.data.local.repository.IncomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(private val incomeRepository: IncomeRepository) :
    ViewModel() {

    private val _incomeInsertState = MutableLiveData<Result<Unit>>()
    val incomeInsertState: LiveData<Result<Unit>> = _incomeInsertState

    val income: LiveData<List<Income>> = incomeRepository.getAllIncome()

    val incomeCategories: LiveData<List<IncomeCategory>> = incomeRepository.getAllIncomeCategories()

    val accountTypes: LiveData<List<AccountType>> = incomeRepository.getAllAccountTypes()

    fun insertIncome(amount: Double?, date: Long, categoryId: Int, accountId: Int, note: String?) {
        viewModelScope.launch {
            try {
                if (amount != null && note != null) {
                    val income = Income(
                        date = date,
                        amount = amount,
                        incCategoryId = categoryId,
                        accountId = accountId,
                        note = note
                    )
                    incomeRepository.insertIncome(income)
                    _incomeInsertState.postValue(Result.success(Unit))
                } else {
                    _incomeInsertState.postValue(Result.failure(IllegalArgumentException("Invalid input data")))
                }
            } catch (e: Exception) {
                _incomeInsertState.postValue(Result.failure(e))
            }
        }
    }

    fun insertIncomeCategories(incomeCategories: List<IncomeCategory>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                incomeRepository.insertIncomeCategories(incomeCategories)
            } catch (_: Exception) {

            }
        }
    }

    fun insertAccountTypes(accountTypes: List<AccountType>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                incomeRepository.insertAccountTypes(accountTypes)
            } catch (_: Exception) {

            }
        }
    }

    fun getIncomeByCategory(): Map<String, Double> {
        val categories = incomeCategories.value.orEmpty()
        val incomeList = income.value.orEmpty()

        // Aggregate income amounts by category
        return incomeList.groupBy { incomeItem ->
            categories.find { it.id == incomeItem.incCategoryId }?.name ?: "Unknown"
        }.mapValues { (_, incomes) ->
            incomes.sumOf { it.amount }
        }
    }
}

