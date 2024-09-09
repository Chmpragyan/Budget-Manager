package com.example.budgetwise.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetwise.dao.AccountTypeDao
import com.example.budgetwise.dao.ExpenseCategoryDao
import com.example.budgetwise.dao.ExpenseDao
import com.example.budgetwise.dao.IncomeCategoryDao
import com.example.budgetwise.dao.IncomeDao
import com.example.budgetwise.dao.TransferDao
import com.example.budgetwise.data.local.converter.Converters
import com.example.budgetwise.data.local.model.AccountType
import com.example.budgetwise.data.local.model.Expense
import com.example.budgetwise.data.local.model.ExpenseCategory
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory
import com.example.budgetwise.data.local.model.Transfer

@Database(
    entities = [Income::class, Expense::class, Transfer::class, IncomeCategory::class, ExpenseCategory::class, AccountType::class],
    version = 4
)
@TypeConverters(Converters::class)
abstract class BudgetDb : RoomDatabase() {
    abstract fun getIncome(): IncomeDao
    abstract fun getIncomeCategory(): IncomeCategoryDao
    abstract fun getExpenseCategory(): ExpenseCategoryDao
    abstract fun getExpense(): ExpenseDao
    abstract fun getTransfer(): TransferDao
    abstract fun getAccountType(): AccountTypeDao
}
