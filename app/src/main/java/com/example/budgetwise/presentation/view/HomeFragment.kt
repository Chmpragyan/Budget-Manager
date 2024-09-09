package com.example.budgetwise.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgetwise.R
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.databinding.FragmentHomeBinding
import com.example.budgetwise.presentation.adapter.BudgetAdapter
import com.example.budgetwise.presentation.viewmodel.ExpenseViewModel
import com.example.budgetwise.presentation.viewmodel.IncomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

    private val incomeViewModel: IncomeViewModel by activityViewModels()

    private val expenseViewModel: ExpenseViewModel by activityViewModels()

    private lateinit var budgetAdapter: BudgetAdapter

    private lateinit var incomeList: ArrayList<Income>

    private lateinit var expenseList: ArrayList<Expense>

    private val expenseCategory = listOf(
        ExpenseCategory(1, "Food"),
        ExpenseCategory(2, "Clothing"),
        ExpenseCategory(3, "Education"),
        ExpenseCategory(4, "Health"),
        ExpenseCategory(5, "Fun"),
        ExpenseCategory(6, "Travel")
    )

    private val incomeCatList = listOf(
        IncomeCategory(1, "Salary"),
        IncomeCategory(2, "Bonus"),
        IncomeCategory(3, "Other")
    )

    private val accountType = listOf(
        AccountType(1, "Cash"),
        AccountType(2, "Accounts"),
        AccountType(3, "Card")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeButtonClick()

        incomeList = ArrayList()
        expenseList = ArrayList()

        incomeViewModel.income?.observe(viewLifecycleOwner) { list ->
            list?.let {
                incomeList.addAll(it)
                setRecyclerView()
                budgetAdapter.notifyDataSetChanged()
                updateBudgetTotals()
            }
        }

        expenseViewModel.expense?.observe(viewLifecycleOwner) { list ->
            list?.let {
                expenseList.addAll(it)
                setRecyclerView()
                budgetAdapter.notifyDataSetChanged()
                updateBudgetTotals()
            }
        }
    }

    private fun updateBudgetTotals() {
        val totalIncome = incomeList.sumOf { it.amount }
        val totalExpense = expenseList.sumOf { it.amount }

        val difference = totalIncome - totalExpense

        binding.tvIncomeAmount.text = totalIncome.toString()
        binding.tvExpenseAmount.text = totalExpense.toString()
        binding.tvTotalRemainingAmount.text =
            difference.toString()
    }

    private fun initializeButtonClick() {
        binding.fbFloatingButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fb_floating_button -> {
                val bottomSheet = AddBudgetFragment()
                bottomSheet.show(parentFragmentManager, "AddBudgetBottomSheet")
            }
        }
    }

    private fun setRecyclerView() {
        budgetAdapter =
            BudgetAdapter(expenseList, incomeList, incomeCatList, expenseCategory, accountType)
        binding.rvRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecyclerView.setHasFixedSize(true)
        binding.rvRecyclerView.adapter = budgetAdapter
    }
}