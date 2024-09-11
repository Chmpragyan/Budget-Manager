package com.example.budgetwise.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetwise.R
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.databinding.BudgetLayoutDesignBinding
import com.example.budgetwise.extensions.formatDate
import com.example.budgetwise.interfaces.ISelectList

private const val TAG = "BudgetVH"

class BudgetVH(
    binding: BudgetLayoutDesignBinding,
    private val iSelectList: ISelectList,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {
    private val category: TextView = binding.tvCategory
    private val note: TextView = binding.tvNote
    private val account: TextView = binding.tvAccountType
    private val amount: TextView = binding.tvAmount
    private val displayDate: TextView = binding.tvDisplayDate
    private val displayDailyIncome: TextView = binding.tvDisplayIncome
    private val displayDailyExpense: TextView = binding.tvDisplayExpense

    private val budgetLayout: ConstraintLayout = binding.clBudgetLayout

    fun onBind(
        income: Income?,
        expense: Expense?,
        categoryMapById: Map<Int?, String>,
        accountTypeMapById: Map<Int?, String>,
        showDate: Boolean,
        dailyTotalIncome: Double = 0.0,
        dailyTotalExpense: Double = 0.0
    ) {
        if (income != null) {
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

            category.text = categoryMapById[income.incCategoryId] ?: "Unknown"
            account.text = accountTypeMapById[income.accountId] ?: "Unknown"
            note.text = income.note
            amount.text = income.amount.toString()
            amount.setTextColor(ContextCompat.getColor(context, R.color.button_pressed))

            // Handle click for income
            budgetLayout.setOnClickListener {
                iSelectList.onSelectItemListIncome(income)
                Log.d(TAG, "Income item clicked: ${income.id}")
            }

        } else if (expense != null) {
            val dates = expense.date.formatDate()

            if (showDate) {
                displayDate.text = dates
                displayDate.visibility = View.VISIBLE
            } else {
                displayDate.visibility = View.GONE
            }

            category.text = categoryMapById[expense.expCategoryId] ?: "Unknown"
            account.text = accountTypeMapById[expense.accountId] ?: "Unknown"
            note.text = expense.note
            amount.text = expense.amount.toString()
            amount.setTextColor(ContextCompat.getColor(context, R.color.red))

            budgetLayout.setOnClickListener {
                iSelectList.onSelectItemListExpense(expense)
                Log.d(TAG, "Expense item clicked: ${expense.id}")
            }
        }
    }
}