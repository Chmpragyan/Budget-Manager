package com.example.budgetwise.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.budgetwise.data.local.model.Income
import com.example.budgetwise.data.local.model.IncomeCategory

@Dao
interface IncomeDao {

    @Insert
    suspend fun insertIncome(income: Income)

    @Query("SELECT * FROM income_table")
    fun getAllIncome(): LiveData<List<Income>>

    @Delete
    suspend fun deleteIncome(income: Income)

    @Query("UPDATE income_table SET amount = :amount, date = :date, incCategoryId = :categoryId, accountId = :accountId, note = :note WHERE id = :id")
    suspend fun updateIncome(
        id: Int,
        amount: Double?,
        date: Long,
        categoryId: Int,
        accountId: Int,
        note: String
    )

    @Query("SELECT * FROM income_table WHERE id = :incomeId LIMIT 1")
    suspend fun getIncomeById(incomeId: Int): Income?
}