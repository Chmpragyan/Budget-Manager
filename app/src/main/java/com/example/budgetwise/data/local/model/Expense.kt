package com.example.budgetwise.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_table")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "category") val category: IncomeCategory,
    @ColumnInfo(name = "account") val account: AccountType,
    @ColumnInfo(name = "note") val note: String
)

data class IncomeCategory(
    val id: Int,
    val name: String
)
