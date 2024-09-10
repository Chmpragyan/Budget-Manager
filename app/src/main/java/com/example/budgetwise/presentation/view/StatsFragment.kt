package com.example.budgetwise.presentation.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import com.example.budgetwise.R
import com.example.budgetwise.databinding.FragmentStatsBinding
import com.example.budgetwise.presentation.viewmodel.ExpenseViewModel
import com.example.budgetwise.presentation.viewmodel.IncomeViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : Fragment(), View.OnClickListener {

    private val incomeViewModel: IncomeViewModel by activityViewModels()
    private val expenseViewModel: ExpenseViewModel by activityViewModels()
    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeButtonClick()

        setupObservers()

        handleButtonClick(
            selectedButton = binding.bnChartIncome,
            deselectedButtons = listOf(binding.bnChartExpense),
            visibleLayout = binding.flIncomeLayoutChart,
            hiddenLayouts = listOf(binding.flExpenseLayoutChart)
        )
    }

    private fun setupObservers() {
        incomeViewModel.income?.observe(viewLifecycleOwner) {
            updateIncomePieChart()
        }
        incomeViewModel.incomeCategories.observe(viewLifecycleOwner) {
            updateIncomePieChart()
        }

        expenseViewModel.expense?.observe(viewLifecycleOwner) {
            updateExpensePieChart()
        }
        expenseViewModel.expenseCategory?.observe(viewLifecycleOwner) {
            updateExpensePieChart()
        }
    }

    private fun updateIncomePieChart() {
        val incomeByCategory = incomeViewModel.getIncomeByCategory()
        val entries = incomeByCategory.map { (category, amount) ->
            PieEntry(amount.toFloat(), category)
        }

        val dataSet = PieDataSet(entries, "Income").apply {
            colors = listOf(
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.YELLOW,
                Color.CYAN
            )
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }

        val pieData = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter())
            setValueTextSize(15f)
            setValueTypeface(Typeface.DEFAULT_BOLD)
            setValueTextColor(Color.WHITE)
        }

        binding.incomeLayout.pieChartIncome.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            animateY(1400, Easing.EaseInOutQuad)
            legend.isEnabled = true
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
            setData(pieData)
            invalidate()
        }
    }

    private fun updateExpensePieChart() {
        val expenseByCategory = expenseViewModel.getExpenseByCategory()
        val entries = expenseByCategory.map { (category, amount) ->
            PieEntry(amount.toFloat(), category)
        }

        val dataSet = PieDataSet(entries, "Expense").apply {
            colors = listOf(
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.YELLOW,
                Color.CYAN
            )
            valueTextColor = Color.WHITE
            valueTextSize = 12f
        }

        val pieData = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter())
            setValueTextSize(15f)
            setValueTypeface(Typeface.DEFAULT_BOLD)
            setValueTextColor(Color.WHITE)
        }

        binding.expenseLayout.pieChartExpense.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            animateY(1400, Easing.EaseInOutQuad)
            legend.isEnabled = true
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
            setData(pieData)
            invalidate()
        }
    }

    private fun initializeButtonClick() {
        binding.bnChartIncome.setOnClickListener(this)
        binding.bnChartExpense.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.bn_chart_income -> {
                handleButtonClick(
                    selectedButton = binding.bnChartIncome,
                    deselectedButtons = listOf(binding.bnChartExpense),
                    visibleLayout = binding.flIncomeLayoutChart,
                    hiddenLayouts = listOf(binding.flExpenseLayoutChart)
                )
                updateIncomePieChart()
            }

            R.id.bn_chart_expense -> {
                handleButtonClick(
                    selectedButton = binding.bnChartExpense,
                    deselectedButtons = listOf(binding.bnChartIncome),
                    visibleLayout = binding.flExpenseLayoutChart,
                    hiddenLayouts = listOf(binding.flIncomeLayoutChart)
                )
                updateExpensePieChart()
            }
        }
    }

    private fun handleButtonClick(
        selectedButton: AppCompatButton,
        deselectedButtons: List<AppCompatButton>,
        visibleLayout: View,
        hiddenLayouts: List<View>
    ) {
        binding.apply {
            selectedButton.isSelected = true
            selectedButton.setTextColor(requireContext().getColor(R.color.button_pressed))

            deselectedButtons.forEach { button ->
                button.isSelected = false
                button.setTextColor(requireContext().getColor(R.color.white))
            }

            visibleLayout.visibility = View.VISIBLE
            hiddenLayouts.forEach { layout ->
                layout.visibility = View.GONE
            }
        }
    }
}