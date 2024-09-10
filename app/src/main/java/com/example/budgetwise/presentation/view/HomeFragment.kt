package com.example.budgetwise.presentation.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

    private val incomeViewModel: IncomeViewModel by activityViewModels()

    private val expenseViewModel: ExpenseViewModel by activityViewModels()

    private lateinit var budgetAdapter: BudgetAdapter

    private lateinit var incomeList: ArrayList<Income>

    private lateinit var expenseList: ArrayList<Expense>

    private lateinit var swipeHelper: ItemTouchHelper

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

        setRecyclerView()


        incomeViewModel.income.observe(viewLifecycleOwner) { list ->
            list?.let {
                Log.d(TAG, "Income data received: $it")
                incomeList.clear()
                incomeList.addAll(it)
                budgetAdapter.notifyDataSetChanged()
                updateBudgetTotals()
                swipeToDelFeatures()
            }
        }

        expenseViewModel.expense.observe(viewLifecycleOwner) { list ->
            list?.let {
                Log.d(TAG, "Expense data received: $it")
                expenseList.clear()
                expenseList.addAll(it)
                budgetAdapter.notifyDataSetChanged()
                updateBudgetTotals()
                swipeToDelFeatures()
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
                val existingFragment =
                    parentFragmentManager.findFragmentByTag("AddBudgetBottomSheet")
                if (existingFragment == null) {
                    val bottomSheet = AddBudgetFragment()
                    bottomSheet.show(parentFragmentManager, "AddBudgetBottomSheet")
                } else {
                    (existingFragment as? AddBudgetFragment)?.dismiss()
                }
            }
        }
    }

    private fun setRecyclerView() {
        incomeList = ArrayList()
        expenseList = ArrayList()
        budgetAdapter =
            BudgetAdapter(expenseList, incomeList, incomeCatList, expenseCategory, accountType)
        binding.rvRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecyclerView.setHasFixedSize(true)
        binding.rvRecyclerView.adapter = budgetAdapter
    }

    private fun swipeToDelFeatures() {
        val recyclerList = binding.rvRecyclerView
        val adapter =
            BudgetAdapter(expenseList, incomeList, incomeCatList, expenseCategory, accountType)
        recyclerList.adapter = adapter

        swipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Are you sure you want to delete this?")
                builder.setPositiveButton("Yes") { dialog, _ ->
                    incomeList.removeAt(pos)
                    expenseList.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                    dialog.dismiss()
                }

                builder.setNegativeButton("No") { dialog, _ ->
                    adapter.notifyItemChanged(pos)
                    dialog.cancel()
                }
                val alertDialog = builder.create()
                alertDialog.show()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val p = Paint()
                    if (dX < 0) {
                        p.color = Color.RED
                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )

                        val cornerRadius = 20f
                        val path = Path().apply {
                            addRoundRect(background, cornerRadius, cornerRadius, Path.Direction.CW)
                        }

                        c.drawPath(path, p)
                        p.color = Color.WHITE
                        p.textSize = 40f
                        p.textAlign = Paint.Align.CENTER

                        val textX = background.centerX()
                        val textY = background.centerY() - ((p.descent() + p.ascent()) / 2)

                        c.drawText("DELETE", textX, textY, p)
                    }
                } else {
                    c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        })
        swipeHelper.attachToRecyclerView(recyclerList)
    }
}