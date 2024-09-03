package com.example.budgetwise.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income_table")
data class Income(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "category") val category: ExpenseCategory,
    @ColumnInfo(name = "account") val account: AccountType,
    @ColumnInfo(name = "note") val note: String
)

data class ExpenseCategory(
    val id: Int,
    val name: String
)
