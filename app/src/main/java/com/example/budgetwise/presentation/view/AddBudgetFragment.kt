package com.example.budgetwise.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import com.example.budgetwise.R
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.databinding.FragmentAddBudgetBinding
import com.example.budgetwise.extensions.formatDateTimeFromTimestamp
import com.example.budgetwise.presentation.viewmodel.ExpenseViewModel
import com.example.budgetwise.presentation.viewmodel.IncomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBudgetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddBudgetBinding
    private val incomeViewModel: IncomeViewModel by activityViewModels()
    private val expenseViewModel: ExpenseViewModel by activityViewModels()
    private var date: Long = 0
    private val accountTypes = listOf(
        AccountType(name = "Cash"),
        AccountType(name = "Accounts"),
        AccountType(name = "Card")
    )

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

        observeViewModelExpense()

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
        binding.incomeLayout.dateLayout.etDate.setText(formattedDate)
        date = currentDate

        val currentDateExp = System.currentTimeMillis()
        val formattedDateExp = currentDateExp.formatDateTimeFromTimestamp()
        binding.expenseLayout.dateLayout.etDate.setText(formattedDateExp)
        date = currentDate
    }

    private fun initializeButtonClick() {
        binding.addIncomeBtn.setOnClickListener(this)
        binding.addExpenseBtn.setOnClickListener(this)
        binding.addTransferBtn.setOnClickListener(this)
        binding.incomeLayout.saveButton.setOnClickListener(this)
        binding.expenseLayout.bnSaveExpense.setOnClickListener(this)
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

            R.id.bn_save_expense -> {
                saveExpense()
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

    private val expenseCategoryMap = mapOf(
        "Food" to 1,
        "Clothing" to 2,
        "Education" to 3,
        "Health" to 4,
        "Fun" to 5,
        "Travel" to 6
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

        incomeViewModel.insertAccountTypes(accountTypes)

        val amount = binding.incomeLayout.amountLayout.etAmount.text.toString().toDoubleOrNull()
        val categoryName = binding.incomeLayout.autoCompleteTextView.text.toString()
        val categoryId = incomeCategoryMap[categoryName] ?: return showError("Invalid category")
        val accountName = binding.incomeLayout.accountCategory.text.toString()
        val accountId = accountTypeMap[accountName] ?: return showError("Invalid account")
        val note = binding.incomeLayout.noteLayout.etNote.text.toString()

        incomeViewModel.insertIncome(amount, date, categoryId, accountId, note)
    }

    private fun saveExpense() {
        val categories = listOf(
            ExpenseCategory(name = "Food"),
            ExpenseCategory(name = "Clothing"),
            ExpenseCategory(name = "Education"),
            ExpenseCategory(name = "Health"),
            ExpenseCategory(name = "Entertainment"),
            ExpenseCategory(name = "Travel")
        )
        expenseViewModel.insertExpenseCategories(categories)

        expenseViewModel.insertAccountTypes(accountTypes)

        val amount = binding.expenseLayout.amountLayout.etAmount.text.toString().toDoubleOrNull()
        val categoryName = binding.expenseLayout.autoCompleteTextView.text.toString()
        val categoryId = expenseCategoryMap[categoryName] ?: return showError("Invalid category")
        val accountName = binding.expenseLayout.accountCategory.text.toString()
        val accountId = accountTypeMap[accountName] ?: return showError("Invalid account")
        val note = binding.expenseLayout.noteLayout.etNote.text.toString()

        expenseViewModel.insertExpense(amount, date, categoryId, accountId, note)
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
                clearInputFields(
                    binding.incomeLayout.amountLayout.etAmount,
                    binding.incomeLayout.autoCompleteTextView,
                    binding.incomeLayout.accountCategory,
                    binding.incomeLayout.noteLayout.etNote
                )

                Toast.makeText(requireContext(), "Income saved successfully!", Toast.LENGTH_SHORT)
                    .show()
            }.onFailure { exception ->
                Toast.makeText(
                    requireContext(),
                    "Failed to save income: ${exception.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeViewModelExpense() {
        expenseViewModel.expenseCategories.observe(viewLifecycleOwner) {
        }

        expenseViewModel.accountTypes.observe(viewLifecycleOwner) {
        }

        expenseViewModel.expenseInsertState.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                clearInputFields(
                    binding.expenseLayout.amountLayout.etAmount,
                    binding.expenseLayout.autoCompleteTextView,
                    binding.expenseLayout.accountCategory,
                    binding.expenseLayout.noteLayout.etNote
                )
                Toast.makeText(requireContext(), "Expense saved successfully!", Toast.LENGTH_SHORT)
                    .show()
            }.onFailure { exception ->
                Toast.makeText(
                    requireContext(),
                    "Failed to save expense: ${exception.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun clearInputFields(
        amountEditText: EditText,
        categoryAutoCompleteTextView: AutoCompleteTextView,
        accountEditText: EditText,
        noteEditText: EditText
    ) {
        amountEditText.text?.clear()
        categoryAutoCompleteTextView.text.clear()
        accountEditText.text.clear()
        noteEditText.text?.clear()
    }
}