package com.example.budgetwise.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.databinding.ExpenseLayoutDesignBinding
import com.example.budgetwise.databinding.IncomeLayoutDesignBinding
import com.example.budgetwise.extensions.formatDate
import com.example.budgetwise.utils.Budget.EXPENSE
import com.example.budgetwise.utils.Budget.INCOME
import javax.inject.Inject

private const val TAG = "BudgetAdapter"

class BudgetAdapter @Inject constructor(
    private var expense: ArrayList<Expense>,
    private var income: ArrayList<Income>,
    incomeCategories: List<IncomeCategory>,
    expenseCategory: List<ExpenseCategory>,
    accountTypes: List<AccountType>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val incomeCategoryMapById = incomeCategories.associate { it.id to it.name }
    private val expenseCategoryMapById = expenseCategory.associate { it.id to it.name }
    private val accountTypeMapById = accountTypes.associate { it.id to it.name }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            INCOME -> {
                val binding = IncomeLayoutDesignBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                IncomeVH(binding)
            }

            EXPENSE -> {
                val binding = ExpenseLayoutDesignBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ExpenseVH(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IncomeVH -> {
                if (position < income.size) {
                    val incomeItem = income[position]
                    val dailyTotalExpense = calculateDailyTotalExpense(incomeItem.date)
                    val dailyTotalIncome = calculateDailyTotalIncome(incomeItem.date)
                    holder.onBind(
                        incomeItem, incomeCategoryMapById, accountTypeMapById,
                        shouldShowDate(position),
                        dailyTotalIncome,
                        dailyTotalExpense
                    )
                } else {
                    Log.e(TAG, "Position $position out of bounds for income list")
                }
            }

            is ExpenseVH -> {
                val expensePosition = position - income.size
                if (expensePosition in expense.indices) {
                    val expenseItem = expense[expensePosition]
                    holder.onBind(
                        expenseItem,
                        expenseCategoryMapById,
                        accountTypeMapById,
                        shouldShowDate(position),
                    )
                } else {
                    Log.e(TAG, "Position $position out of bounds for expense list")
                }
            }

            else -> throw IllegalArgumentException("Invalid view holder type")
        }
    }

    override fun getItemCount(): Int {
        return income.size + expense.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < income.size) {
            INCOME
        } else {
            EXPENSE
        }
    }

    private fun shouldShowDate(position: Int): Boolean {
        if (position == 0) {
            return true
        }
        val currentDate = getDateAtPosition(position)

        val previousDate = getDateAtPosition(position - 1)

        return currentDate != previousDate
    }

    private fun getDateAtPosition(position: Int): String {
        return if (position < income.size) {
            income[position].date.formatDate()
        } else {
            val adjustedPosition = position - income.size
            expense[adjustedPosition].date.formatDate()
        }
    }

    private fun calculateDailyTotalIncome(date: Long): Double {
        return income.filter { it.date.formatDate() == date.formatDate() }
            .sumOf { it.amount }
    }

    private fun calculateDailyTotalExpense(date: Long): Double {
        return expense.filter { it.date.formatDate() == date.formatDate() }
            .sumOf { it.amount }
    }
}

