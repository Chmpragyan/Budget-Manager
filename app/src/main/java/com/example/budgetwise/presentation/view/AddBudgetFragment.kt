package com.example.budgetwise.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import com.example.budgetwise.R
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.databinding.FragmentAddBudgetBinding
import com.example.budgetwise.extensions.formatDateTimeFromTimestamp
import com.example.budgetwise.presentation.viewmodel.IncomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AddBudgetFragment"

@AndroidEntryPoint
class AddBudgetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddBudgetBinding
    private val incomeViewModel: IncomeViewModel by activityViewModels()
    private var date: Long = 0

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

        observeViewModel()

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

        val currentDate = System.currentTimeMillis()
        val formattedDate = currentDate.formatDateTimeFromTimestamp()
        Log.d("FormattedDate", "Formatted Date: $formattedDate")
        binding.incomeLayout.dateLayout.etDate.setText(formattedDate)
        date= currentDate
    }

    private fun initializeButtonClick() {
        binding.addIncomeBtn.setOnClickListener(this)
        binding.addExpenseBtn.setOnClickListener(this)
        binding.addTransferBtn.setOnClickListener(this)
        binding.incomeLayout.saveButton.setOnClickListener(this)
    }

    private fun implementDropdown() {
        setupCategory(binding.expenseLayout.autoCompleteTextView, R.array.expense_category)
        setupCategory(binding.expenseLayout.accountCategory, R.array.account_category)

        setupCategory(binding.incomeLayout.autoCompleteTextView, R.array.income_category)
        setupCategory(binding.incomeLayout.accountCategory, R.array.account_category)

        setupCategory(binding.transferLayout.autoCompleteTextView, R.array.account_category)
        setupCategory(binding.transferLayout.accountCategory, R.array.account_category)
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

            R.id.save_button -> {
                saveIncome()
            }
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

    private val incomeCategoryMap = mapOf(
        "Salary" to 1,
        "Bonus" to 2,
        "Other" to 3
    )

    private val accountTypeMap = mapOf(
        "Cash" to 1,
        "Accounts" to 2,
        "Card" to 3
    )

    private fun saveIncome() {
        val categories = listOf(
            IncomeCategory(name = "Salary"),
            IncomeCategory(name = "Bonus"),
            IncomeCategory(name = "Other")
        )
        incomeViewModel.insertIncomeCategories(categories)

        val accountTypes = listOf(
            AccountType(name = "Cash"),
            AccountType(name = "Accounts"),
            AccountType(name = "Card")
        )
        incomeViewModel.insertAccountTypes(accountTypes)

        val amount = binding.incomeLayout.amountLayout.etAmount.text.toString().toDoubleOrNull()
        val categoryName = binding.incomeLayout.autoCompleteTextView.text.toString()
        val categoryId = incomeCategoryMap[categoryName] ?: return showError("Invalid category")
        val accountName = binding.incomeLayout.accountCategory.text.toString()
        val accountId = accountTypeMap[accountName] ?: return showError("Invalid account")
        val note = binding.incomeLayout.noteLayout.etNote.text.toString()

        incomeViewModel.insertIncome(amount, date, categoryId, accountId, note)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun observeViewModel() {
        incomeViewModel.incomeCategories.observe(viewLifecycleOwner) {
        }

        incomeViewModel.accountTypes.observe(viewLifecycleOwner) {
        }

        incomeViewModel.incomeInsertState.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                clearInputFields()
                Toast.makeText(requireContext(), "Income saved successfully!", Toast.LENGTH_SHORT)
                    .show()
            }.onFailure { exception ->
                Log.e(TAG, "observeViewModel: Failed to save income $exception")
                Toast.makeText(
                    requireContext(),
                    "Failed to save income: ${exception.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun clearInputFields() {
        binding.incomeLayout.amountLayout.etAmount.text?.clear()
        binding.incomeLayout.autoCompleteTextView.text.clear()
        binding.incomeLayout.accountCategory.text.clear()
        binding.incomeLayout.noteLayout.etNote.text?.clear()
    }

}