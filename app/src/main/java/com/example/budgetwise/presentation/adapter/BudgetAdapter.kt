package com.example.budgetwise.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.databinding.BudgetLayoutDesignBinding
import com.example.budgetwise.extensions.formatDate
import com.example.budgetwise.interfaces.ISelectList
import javax.inject.Inject

private const val TAG = "BudgetAdapter"

class BudgetAdapter @Inject constructor(
    private var income: ArrayList<Income>,
    private var expense: ArrayList<Expense>,
    incomeCategories: List<IncomeCategory>,
    expenseCategories: List<ExpenseCategory>,
    accountTypes: List<AccountType>,
    private val iSelectList: ISelectList,
    private val context: Context
) : RecyclerView.Adapter<BudgetVH>() {

    private val incomeCategoryMapById = incomeCategories.associate { it.id to it.name }
    private val expenseCategoryMapById = expenseCategories.associate { it.id to it.name }
    private val accountTypeMapById = accountTypes.associate { it.id to it.name }

    // Merged list of income and expense
    private val items: List<Any> = mergeIncomeAndExpense()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetVH {
        val binding = BudgetLayoutDesignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BudgetVH(binding, iSelectList, context)
    }

    override fun onBindViewHolder(holder: BudgetVH, position: Int) {
        val item = items[position]

        when (item) {
            is Income -> {
                val dailyTotalIncome = calculateDailyTotalIncome(item.date)
                val dailyTotalExpense = calculateDailyTotalExpense(item.date)

                holder.onBind(
                    income = item,
                    expense = null,
                    categoryMapById = incomeCategoryMapById,
                    accountTypeMapById = accountTypeMapById,
                    showDate = shouldShowDate(position),
                    dailyTotalIncome = dailyTotalIncome,
                    dailyTotalExpense = dailyTotalExpense
                )
            }

            is Expense -> {
                holder.onBind(
                    income = null,
                    expense = item,
                    categoryMapById = expenseCategoryMapById,
                    accountTypeMapById = accountTypeMapById,
                    showDate = shouldShowDate(position)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun mergeIncomeAndExpense(): List<Any> {
        val mergedList = ArrayList<Any>()
        mergedList.addAll(income)
        mergedList.addAll(expense)

        return mergedList.sortedBy {
            when (it) {
                is Income -> it.date
                is Expense -> it.date
                else -> Long.MAX_VALUE
            }
        }
    }

    private fun shouldShowDate(position: Int): Boolean {
        if (position == 0) return true
        val currentDate = getDateAtPosition(position)
        val previousDate = getDateAtPosition(position - 1)
        return currentDate != previousDate
    }

    private fun getDateAtPosition(position: Int): String {
        return when (val item = items[position]) {
            is Income -> item.date.formatDate()
            is Expense -> item.date.formatDate()
            else -> ""
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


//    private var expense: ArrayList<Expense>,
//    private var income: ArrayList<Income>,
//    incomeCategories: List<IncomeCategory>,
//    expenseCategory: List<ExpenseCategory>,
//    accountTypes: List<AccountType>,
//    private val iSelectList: ISelectList
//) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private val incomeCategoryMapById = incomeCategories.associate { it.id to it.name }
//    private val expenseCategoryMapById = expenseCategory.associate { it.id to it.name }
//    private val accountTypeMapById = accountTypes.associate { it.id to it.name }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            INCOME -> {
//                val binding = IncomeLayoutDesignBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//                IncomeVH(binding, iSelectList)
//            }
//
//            EXPENSE -> {
//                val binding = ExpenseLayoutDesignBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//                ExpenseVH(binding, iSelectList)
//            }
//
//            else -> throw IllegalArgumentException("Invalid view type")
//
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        when (holder) {
//            is IncomeVH -> {
//                if (position < income.size) {
//                    val incomeItem = income[position]
//                    val dailyTotalExpense = calculateDailyTotalExpense(incomeItem.date)
//                    val dailyTotalIncome = calculateDailyTotalIncome(incomeItem.date)
//                    holder.onBind(
//                        incomeItem, incomeCategoryMapById, accountTypeMapById,
//                        shouldShowDate(position),
//                        dailyTotalIncome,
//                        dailyTotalExpense
//                    )
//                } else {
//                    Log.e(TAG, "Position $position out of bounds for income list")
//                }
//            }
//
//            is ExpenseVH -> {
//                val expensePosition = position - income.size
//                if (expensePosition in expense.indices) {
//                    val expenseItem = expense[expensePosition]
//                    holder.onBind(
//                        expenseItem,
//                        expenseCategoryMapById,
//                        accountTypeMapById,
//                        shouldShowDate(position),
//                    )
//                } else {
//                    Log.e(TAG, "Position $position out of bounds for expense list")
//                }
//            }
//
//            else -> throw IllegalArgumentException("Invalid view holder type")
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return income.size + expense.size
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (position < income.size) {
//            INCOME
//        } else {
//            EXPENSE
//        }
//    }
//
//    private fun shouldShowDate(position: Int): Boolean {
//        if (position == 0) {
//            return true
//        }
//        val currentDate = getDateAtPosition(position)
//
//        val previousDate = getDateAtPosition(position - 1)
//
//        return currentDate != previousDate
//    }
//
//    private fun getDateAtPosition(position: Int): String {
//        return if (position < income.size) {
//            income[position].date.formatDate()
//        } else {
//            val adjustedPosition = position - income.size
//            expense[adjustedPosition].date.formatDate()
//        }
//    }
//
//    private fun calculateDailyTotalIncome(date: Long): Double {
//        return income.filter { it.date.formatDate() == date.formatDate() }
//            .sumOf { it.amount }
//    }
//
//    private fun calculateDailyTotalExpense(date: Long): Double {
//        return expense.filter { it.date.formatDate() == date.formatDate() }
//            .sumOf { it.amount }
//    }
//}

