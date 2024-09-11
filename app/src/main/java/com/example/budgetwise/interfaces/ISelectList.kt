package com.example.budgetwise.interfaces

import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.Income

interface ISelectList {
    fun onSelectItemListIncome(income: Income)
    fun onSelectItemListExpense(expense: Expense)
}