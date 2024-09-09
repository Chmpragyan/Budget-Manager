package com.example.budgetwise.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.databinding.IncomeLayoutDesignBinding
import com.example.budgetwise.extensions.formatDate

private const val TAG = "IncomeVH"

class IncomeVH(binding: IncomeLayoutDesignBinding) : RecyclerView.ViewHolder(binding.root) {
    private val incomeCategory: TextView = binding.tvCategory
    private val incomeNote: TextView = binding.tvNote
    private val incomeAccount: TextView = binding.tvAccountType
    private val incomeAmount: TextView = binding.tvAmount
    private val displayDate: TextView = binding.tvDisplayDate
    private val displayDailyIncome: TextView = binding.tvDisplayIncDate
    private val displayDailyExpense: TextView = binding.tvDisplayExpDate

    fun onBind(
        income: Income,
        incomeCategoryMapById: Map<Int?, String>,
        accountTypeMapById: Map<Int?, String>,
        showDate: Boolean,
        dailyTotalIncome: Double,
        dailyTotalExpense: Double
    ) {
        val dates = income.date.formatDate()

        if (showDate) {
            displayDate.text = dates
            displayDate.visibility = View.VISIBLE

            displayDailyIncome.text = dailyTotalIncome.toString()
            displayDailyIncome.visibility = View.VISIBLE

            displayDailyExpense.text = dailyTotalExpense.toString()
            displayDailyExpense.visibility = View.VISIBLE

        } else {
            displayDate.visibility = View.GONE
            displayDailyIncome.visibility = View.GONE
            displayDailyExpense.visibility = View.GONE
        }

        incomeCategory.text = incomeCategoryMapById[income.incCategoryId] ?: "Unknown"
        incomeAccount.text = accountTypeMapById[income.accountId] ?: "Unknown"
        incomeNote.text = income.note
        incomeAmount.text = income.amount.toString()
    }
}
