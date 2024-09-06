package com.example.budgetwise.presentation.adapter

import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.databinding.IncomeLayoutDesignBinding

private const val TAG = "IncomeVH"
class IncomeVH(binding: IncomeLayoutDesignBinding) : RecyclerView.ViewHolder(binding.root) {
    private val incomeCategory: TextView = binding.tvCategory
    private val incomeNote: TextView = binding.tvNote
    private val incomeAccount: TextView = binding.tvAccountType
    private val incomeAmount: TextView = binding.tvAmount

    fun onBind(
        income: Income,
        incomeCategoryMapById: Map<Int?, String>,
        accountTypeMapById: Map<Int?, String>
    ) {
        Log.d(TAG, "Category ID: ${income.incCategoryId}")
        Log.d(TAG, "Account ID: ${income.accountId}")
        Log.d(TAG, "Category Map: $incomeCategoryMapById")
        Log.d(TAG, "Account Map: $accountTypeMapById")
        incomeCategory.text = incomeCategoryMapById[income.incCategoryId] ?: "Unknown"
        incomeAccount.text = accountTypeMapById[income.accountId] ?: "Unknown"
        Log.d(TAG, "onBind: ${incomeCategory.text}")
        Log.d(TAG, "onBind: ${incomeAccount.text}")
        incomeNote.text = income.note
        incomeAmount.text = income.amount.toString()
    }
}
