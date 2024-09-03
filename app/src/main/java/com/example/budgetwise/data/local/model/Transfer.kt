package com.example.budgetwise.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transfer_table")
data class Transfer(
    @PrimaryKey(autoGenerate = true) override val id: Int? = null,
    @ColumnInfo(name = "date") override val date: Long,
    @ColumnInfo(name = "amount") override val amount: Double,
    @ColumnInfo(name = "sentFrom") val sentFrom: AccountType,
    @ColumnInfo(name = "sentTo") val sentTo: AccountType,
    @ColumnInfo(name = "note") override val note: String
) : BaseModel(id, date, amount, sentFrom, note)
