package com.example.budgetwise.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income_table")
data class Income(
    @PrimaryKey(autoGenerate = true) override val id: Int? = null,
    @ColumnInfo(name = "date") override val date: Long,
    @ColumnInfo(name = "amount") override val amount: Double,
    @ColumnInfo(name = "category") val category: IncomeCategory,
    @ColumnInfo(name = "account") override val account: AccountType,
    @ColumnInfo(name = "note") override val note: String
) : BaseTransaction(id, date, amount, account, note)

data class IncomeCategory(
    val id: Int,
    val name: String
)
