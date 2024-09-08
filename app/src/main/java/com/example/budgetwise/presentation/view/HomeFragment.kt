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
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.databinding.FragmentHomeBinding
import com.example.budgetwise.presentation.adapter.BudgetAdapter
import com.example.budgetwise.presentation.viewmodel.IncomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

    private val incomeViewModel: IncomeViewModel by activityViewModels()

    private lateinit var budgetAdapter: BudgetAdapter

    private lateinit var incomeList: ArrayList<Income>

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

        incomeViewModel.income?.observe(viewLifecycleOwner) { list ->
            list?.let {
                incomeList.addAll(it)
                setRecyclerView()
                getTotalIncome()
            }
        }
    }

    private fun getTotalIncome() {
        var totalIncome = 0.0
        for (income in incomeList) {
            totalIncome += income.amount
        }
        binding.tvIncomeAmount.text = totalIncome.toString()
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
        budgetAdapter = BudgetAdapter(incomeList, incomeCatList, accountType)
        binding.rvRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecyclerView.setHasFixedSize(true)
        binding.rvRecyclerView.adapter = budgetAdapter
    }
}