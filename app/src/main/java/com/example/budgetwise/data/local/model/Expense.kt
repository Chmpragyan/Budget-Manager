package com.example.budgetwise.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class Expense(
    @PrimaryKey(autoGenerate = true) override val id: Int? = null,
    @ColumnInfo(name = "date") override val date: Long,
    @ColumnInfo(name = "amount") override val amount: Double,
    @ColumnInfo(name = "category") val category: ExpenseCategory,
    @ColumnInfo(name = "account") override val account: AccountType,
    @ColumnInfo(name = "note") override val note: String
) : BaseTransaction(id, date, amount, account, note)

data class ExpenseCategory(
    val id: Int,
    val name: String
)
