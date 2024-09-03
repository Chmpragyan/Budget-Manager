package com.example.budgetwise.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import com.example.budgetwise.R
import com.example.budgetwise.databinding.FragmentAddBudgetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddBudget : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddBudgetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBudgetBinding.inflate(layoutInflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayBottomSheet()

        handleButtonClick(
            selectedButton = binding.addIncomeBtn,
            deselectedButtons = listOf(binding.addExpenseBtn, binding.addTransferBtn),
            titleResId = R.string.add_income,
            visibleLayout = binding.flIncomeLayout,
            hiddenLayouts = listOf(binding.flExpenseLayout, binding.flTransferLayout)
        )

        initializeButtonClick()

        implementDropdown()
    }

    private fun setupCategory(autoCompleteTextView: AutoCompleteTextView, arrayResId: Int) {
        val category = resources.getStringArray(arrayResId)
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            category
        )
        autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun implementDropdown() {
        setupCategory(binding.expenseLayout.autoCompleteTextView, R.array.expense_category)
        setupCategory(binding.expenseLayout.accountCategory, R.array.account_category)

        setupCategory(binding.incomeLayout.autoCompleteTextView, R.array.income_category)
        setupCategory(binding.incomeLayout.accountCategory, R.array.account_category)

        setupCategory(binding.transferLayout.autoCompleteTextView, R.array.account_category)
        setupCategory(binding.transferLayout.accountCategory, R.array.account_category)
    }

    private fun initializeButtonClick() {
        binding.addIncomeBtn.setOnClickListener(this)
        binding.addExpenseBtn.setOnClickListener(this)
        binding.addTransferBtn.setOnClickListener(this)
    }

    private fun displayBottomSheet() {
        val bottomSheet: FrameLayout =
            dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!

        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.apply {
            peekHeight = resources.displayMetrics.heightPixels
            state = BottomSheetBehavior.STATE_EXPANDED

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.add_income_btn -> handleButtonClick(
                selectedButton = binding.addIncomeBtn,
                deselectedButtons = listOf(binding.addExpenseBtn, binding.addTransferBtn),
                titleResId = R.string.add_income,
                visibleLayout = binding.flIncomeLayout,
                hiddenLayouts = listOf(binding.flExpenseLayout, binding.flTransferLayout)
            )

            R.id.add_expense_btn -> handleButtonClick(
                selectedButton = binding.addExpenseBtn,
                deselectedButtons = listOf(binding.addIncomeBtn, binding.addTransferBtn),
                titleResId = R.string.add_expense,
                visibleLayout = binding.flExpenseLayout,
                hiddenLayouts = listOf(binding.flIncomeLayout, binding.flTransferLayout)
            )

            R.id.add_transfer_btn -> handleButtonClick(
                selectedButton = binding.addTransferBtn,
                deselectedButtons = listOf(binding.addIncomeBtn, binding.addExpenseBtn),
                titleResId = R.string.add_transfer,
                visibleLayout = binding.flTransferLayout,
                hiddenLayouts = listOf(binding.flIncomeLayout, binding.flExpenseLayout)
            )
        }
    }

    private fun handleButtonClick(
        selectedButton: AppCompatButton,
        deselectedButtons: List<AppCompatButton>,
        @StringRes titleResId: Int,
        visibleLayout: View,
        hiddenLayouts: List<View>
    ) {
        binding.apply {
            toolbar.title = requireContext().getString(titleResId)

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