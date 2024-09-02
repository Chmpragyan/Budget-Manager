package com.example.budgetwise.presentation.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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

        initializeButtonClick()
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
            R.id.add_income_btn -> {
                binding.apply {
                    toolbar.title = requireContext().getString(R.string.add_income)
                    addIncomeBtn.isSelected = true
                    addExpenseBtn.isSelected = false
                    addTransferBtn.isSelected = false
                }
            }

            R.id.add_expense_btn -> {
                binding.apply {
                    toolbar.title = requireContext().getString(R.string.add_expense)
                    addIncomeBtn.isSelected = false
                    addExpenseBtn.isSelected = true
                    addTransferBtn.isSelected = false
                }
            }

            R.id.add_transfer_btn -> {
                binding.apply {
                    toolbar.title = requireContext().getString(R.string.add_transfer)
                    addIncomeBtn.isSelected = false
                    addExpenseBtn.isSelected = false
                    addTransferBtn.isSelected = true
                }
            }
        }
    }


}