package com.example.budgetwise.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.databinding.ExpenseLayoutDesignBinding
import com.example.budgetwise.extensions.formatDate
import com.example.budgetwise.interfaces.ISelectList

private const val TAG = "ExpenseVH"

class ExpenseVH(binding: ExpenseLayoutDesignBinding, private val iSelectList: ISelectList) : RecyclerView.ViewHolder(binding.root) {
    private val displayDate: TextView = binding.tvDisplayDate
    private val expenseCategory: TextView = binding.tvCategoryExpense
    private val expenseNote: TextView = binding.tvNoteExpense
    private val expenseAccount: TextView = binding.tvAccountTypeExpense
    private val expenseAmount: TextView = binding.tvAmountExpense

    private val expenseLayout: ConstraintLayout = binding.clExpenseLayout

    fun onBind(
        expense: Expense,
        expenseCategoryMapById: Map<Int?, String>,
        accountTypeMapById: Map<Int?, String>,
        showDate: Boolean,
    ) {
        val dates = expense.date.formatDate()

        if (showDate) {
            displayDate.text = dates
            displayDate.visibility = View.VISIBLE
        } else {
            displayDate.visibility = View.GONE
        }
        expenseCategory.text = expenseCategoryMapById[expense.expCategoryId] ?: "Unknown"
        expenseAccount.text = accountTypeMapById[expense.accountId] ?: "Unknown"
        expenseNote.text = expense.note
        expenseAmount.text = expense.amount.toString()


    }
}