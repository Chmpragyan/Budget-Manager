package com.example.budgetwise.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "expense_table")
data class Expense(
    @PrimaryKey(autoGenerate = true) override val id: Int? = null,
    @ColumnInfo(name = "date") override val date: Long,
    @ColumnInfo(name = "amount") override val amount: Double,
    @ColumnInfo(name = "expCategoryId") val expCategoryId: Int,
    @ColumnInfo(name = "accountId") override var accountId: Int,
    @ColumnInfo(name = "note") override val note: String
) : BaseModel(id, date, amount, accountId, note), Serializable