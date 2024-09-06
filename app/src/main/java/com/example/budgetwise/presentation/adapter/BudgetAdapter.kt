package com.example.budgetwise.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.databinding.IncomeLayoutDesignBinding
import javax.inject.Inject

private const val TAG = "BudgetAdapter"
class BudgetAdapter @Inject constructor(
    private var income: ArrayList<Income>, incomeCategories: List<IncomeCategory>,
    accountTypes: List<AccountType>
) : RecyclerView.Adapter<IncomeVH>() {

    private val incomeCategoryMapById = incomeCategories.associate { it.id to it.name }
    private val accountTypeMapById = accountTypes.associate { it.id to it.name }

    init {
        Log.d(TAG, "Income Category Map: $incomeCategoryMapById")
        Log.d(TAG, "Account Type Map: $accountTypeMapById")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeVH {
        val binding = IncomeLayoutDesignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IncomeVH(binding)
    }

    override fun getItemCount(): Int {
        return income.size
    }

    override fun onBindViewHolder(holder: IncomeVH, position: Int) {
        holder.onBind(income[position], incomeCategoryMapById, accountTypeMapById)
    }
}

