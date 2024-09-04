package com.example.budgetwise.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "income_table",
    foreignKeys = [ForeignKey(
        entity = IncomeCategory::class,
        parentColumns = ["id"],
        childColumns = ["incCategoryId"]
    ),
        ForeignKey(
            entity = AccountType::class,
            parentColumns = ["id"],
            childColumns = ["accountId"]
        )]
)
data class Income(
    @PrimaryKey(autoGenerate = true) override val id: Int? = null,
    @ColumnInfo(name = "date") override val date: Long,
    @ColumnInfo(name = "amount") override val amount: Double,
    @ColumnInfo(name = "incCategoryId") val incCategoryId: Int,
    @ColumnInfo(name = "accountId") override var accountId: Int,
    @ColumnInfo(name = "note") override val note: String
) : BaseModel(id, date, amount, accountId, note), Serializable
