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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(private val incomeRepository: IncomeRepository) :
    ViewModel() {

    private val _incomeInsertState = MutableLiveData<Result<Unit>>()
    val incomeInsertState: LiveData<Result<Unit>> = _incomeInsertState

    var income: LiveData<List<Income>>? = null

    private val _incomeCategories = MutableLiveData<List<IncomeCategory>>()
    val incomeCategories: LiveData<List<IncomeCategory>> = _incomeCategories

    private val _accountTypes = MutableLiveData<List<AccountType>>()
    val accountTypes: LiveData<List<AccountType>> = _accountTypes

    init {
        viewModelScope.launch {
            income = incomeRepository.getAllIncome()
            _incomeCategories.value = incomeRepository.getAllIncomeCategories()
            _accountTypes.value = incomeRepository.getAllAccountTypes()
        }
    }

    fun insertIncome(amount: Double?, date: Long, categoryId: Int, accountId: Int, note: String) {
        viewModelScope.launch {
            val result = incomeRepository.insertIncome(amount, date, categoryId, accountId, note)
            _incomeInsertState.value = result
        }
    }

    fun insertIncomeCategories(incomeCategories: List<IncomeCategory>) {
        viewModelScope.launch {
            incomeRepository.insertIncomeCategories(incomeCategories)
        }
    }

    fun insertAccountTypes(accountTypes: List<AccountType>) {
        viewModelScope.launch {
            incomeRepository.insertAccountTypes(accountTypes)
        }
    }
}

